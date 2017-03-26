package com.spbsu.datastream.core.graph.ops;

import com.spbsu.datastream.core.DataItem;
import com.spbsu.datastream.core.HashFunction;
import com.spbsu.datastream.core.Meta;
import com.spbsu.datastream.core.PayloadDataItem;
import com.spbsu.datastream.core.graph.InPort;
import com.spbsu.datastream.core.graph.Processor;
import com.spbsu.datastream.core.materializer.atomic.AtomicHandle;

import java.util.function.Function;

public class StatelessFilter<T, R> extends Processor<T, R> {
  private final Function<T, R> function;

  public StatelessFilter(final Function<T, R> function, final HashFunction<T> hash) {
    super(hash);
    this.function = function;
  }

  public StatelessFilter(final Function<T, R> function) {
    super();
    this.function = function;
  }

  public Function<T, R> function() {
    return function;
  }

  @Override
  public void onPush(final InPort inPort, final DataItem<?> item, final AtomicHandle handler) {
    @SuppressWarnings("unchecked")
    final R res = function.apply((T) item.payload());

    handler.push(outPort(), new PayloadDataItem<>(new Meta(item.meta(), this.hashCode()), res, item.rootId()));
    handler.ack(inPort, item);
  }
}

