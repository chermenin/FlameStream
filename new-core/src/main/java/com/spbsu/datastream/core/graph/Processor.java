package com.spbsu.datastream.core.graph;

import com.spbsu.datastream.core.HashFunction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Processor<T, R> implements AtomicGraph {
  private final InPort inPort;
  private final OutPort outPort = new OutPort();

  protected Processor(final HashFunction<? super T> hash) {
    this.inPort = new InPort(hash);
  }

  protected Processor() {
    this.inPort = new InPort(HashFunction.OBJECT_HASH);
  }

  public InPort inPort() {
    return inPort;
  }

  public OutPort outPort() {
    return outPort;
  }

  @Override
  public List<InPort> inPorts() {
    return Collections.singletonList(inPort);
  }

  @Override
  public List<OutPort> outPorts() {
    return Collections.singletonList(outPort);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Processor processor = (Processor) o;
    return Objects.equals(inPort, processor.inPort) &&
            Objects.equals(outPort, processor.outPort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inPort, outPort);
  }

  @Override
  public String toString() {
    return "Processor{" + "inPort=" + inPort +
            ", outPort=" + outPort +
            '}';
  }
}
