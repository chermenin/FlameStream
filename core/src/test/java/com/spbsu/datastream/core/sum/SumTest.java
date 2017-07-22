package com.spbsu.datastream.core.sum;

import akka.actor.ActorPath;
import com.spbsu.datastream.core.HashFunction;
import com.spbsu.datastream.core.TestStand;
import com.spbsu.datastream.core.barrier.PreSinkMetaFilter;
import com.spbsu.datastream.core.barrier.RemoteActorConsumer;
import com.spbsu.datastream.core.graph.Graph;
import com.spbsu.datastream.core.graph.InPort;
import com.spbsu.datastream.core.graph.TheGraph;
import com.spbsu.datastream.core.graph.ops.Broadcast;
import com.spbsu.datastream.core.graph.ops.Filter;
import com.spbsu.datastream.core.graph.ops.Grouping;
import com.spbsu.datastream.core.graph.ops.Merge;
import com.spbsu.datastream.core.graph.ops.StatelessMap;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SumTest {

  @Test
  public void testSingleFront() throws InterruptedException {
    this.test(20, 1000, 1, 123);
  }

  @Test
  public void testMultipleFronts() throws InterruptedException {
    this.test(30, 1000, 4, 123);
  }

  @Test
  public void shortRepeatedTests() throws InterruptedException {
    for (int i = 0; i < 10; ++i) {
      this.test(5, 10, 4, i);
    }
  }

  private void test(int tickLength, int inputSize, int fronts, int seed) throws InterruptedException {
    try (TestStand stage = new TestStand(4, fronts)) {

      final Deque<Sum> result = new ArrayDeque<>();

      stage.deploy(SumTest.sumGraph(stage.fronts(), stage.wrap(k -> result.add((Sum) k))), tickLength, TimeUnit.SECONDS);

      final List<LongNumb> source = new Random(seed).ints(inputSize).map(i -> i % 100).map(Math::abs).mapToObj(LongNumb::new).collect(Collectors.toList());
      final Consumer<Object> sink = stage.randomFrontConsumer(seed);
      source.forEach(sink);
      stage.waitTick(tickLength + 5, TimeUnit.SECONDS);

      final long expected = source.stream().reduce(new LongNumb(0L), (a, b) -> new LongNumb(a.value() + b.value())).value();
      final long actual = result.stream().mapToLong(Sum::value).max().orElseThrow(NoSuchElementException::new);

      Assert.assertEquals(actual, expected);
    }
  }

  private static TheGraph sumGraph(Collection<Integer> fronts, ActorPath consumer) {
    final HashFunction<Numb> identity = HashFunction.constantHash(1);
    final HashFunction<List<Numb>> groupIdentity = HashFunction.constantHash(1);

    final Merge<Numb> merge = new Merge<>(Arrays.asList(identity, identity));
    final Grouping<Numb> grouping = new Grouping<>(identity, 2);
    final StatelessMap<List<Numb>, List<Numb>> enricher = new StatelessMap<>(new IdentityEnricher(), groupIdentity);
    final Filter<List<Numb>> junkFilter = new Filter<>(new WrongOrderingFilter(), groupIdentity);
    final StatelessMap<List<Numb>, Sum> reducer = new StatelessMap<>(new Reduce(), groupIdentity);
    final Broadcast<Sum> broadcast = new Broadcast<>(identity, 2);

    final PreSinkMetaFilter<Sum> metaFilter = new PreSinkMetaFilter<>(identity);
    final RemoteActorConsumer<Integer> sink = new RemoteActorConsumer<>(consumer);

    final Graph graph = merge.fuse(grouping, merge.outPort(), grouping.inPort())
            .fuse(enricher, grouping.outPort(), enricher.inPort())
            .fuse(junkFilter, enricher.outPort(), junkFilter.inPort())
            .fuse(reducer, junkFilter.outPort(), reducer.inPort())
            .fuse(broadcast, reducer.outPort(), broadcast.inPort())
            .fuse(metaFilter, broadcast.outPorts().get(0), metaFilter.inPort())
            .fuse(sink, metaFilter.outPort(), sink.inPort())
            .wire(broadcast.outPorts().get(1), merge.inPorts().get(1));

    final Map<Integer, InPort> frontBindings = fronts.stream()
            .collect(Collectors.toMap(Function.identity(), e -> merge.inPorts().get(0)));
    return new TheGraph(graph, frontBindings);
  }
}