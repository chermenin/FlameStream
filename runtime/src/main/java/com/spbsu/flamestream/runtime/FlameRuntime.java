package com.spbsu.flamestream.runtime;

import com.spbsu.flamestream.runtime.edge.Front;
import com.spbsu.flamestream.core.Graph;
import com.spbsu.flamestream.runtime.edge.Rear;
import com.spbsu.flamestream.runtime.edge.EdgeContext;

import java.util.stream.Stream;

public interface FlameRuntime extends AutoCloseable {
  int DEFAULT_MAX_ELEMENTS_IN_GRAPH = 500;
  int DEFAULT_MILLIS_BETWEEN_COMMITS = 100;
  int DEFAULT_PARALLELISM = 4;

  Flame run(Graph g);

  @Override
  default void close() {
  }

  interface Flame extends AutoCloseable {
    <F extends Front, H> Stream<H> attachFront(String id, FrontType<F, H> type);

    <R extends Rear, H> Stream<H> attachRear(String id, RearType<R, H> type);

    @Override
    default void close() {
    }
  }

  interface FrontType<F extends Front, H> {
    FrontInstance<F> instance();

    H handle(EdgeContext context);
  }

  interface RearType<R extends Rear, H> {
    RearInstance<R> instance();

    H handle(EdgeContext context);
  }

  interface FrontInstance<F extends Front> {
    Class<F> clazz();

    Object[] params();
  }

  interface RearInstance<R extends Rear> {
    Class<R> clazz();

    Object[] params();
  }
}
