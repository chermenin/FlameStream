//package com.spbsu.datastream.core;
//
//import com.spbsu.datastream.core.graph.Source;
//import com.spbsu.datastream.core.tick.atomic.AtomicHandle;
//
//public class ConstantSource<T> extends Source<T> {
//  private final T value;
//
//  public ConstantSource(final T value) {
//    this.value = value;
//  }
//
//  @Override
//  public void onStart(final AtomicHandle handle) {
//    //noinspection InfiniteLoopStatement
//    while (true) {
//      handle.push(outPort(), new PayloadDataItem<>(Meta.now(), value));
//    }
//  }
//}
