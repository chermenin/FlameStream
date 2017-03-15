package com.spbsu.datastream.core.graph;

import java.util.HashSet;
import java.util.List;

/**
 * Created by marnikitta on 2/5/17.
 * inspired by akka-stream
 */
public interface Graph {
  List<InPort> inPorts();

  List<OutPort> outPorts();

  /**
   * Fuses this Graph to `that` Graph by wiring together `from` and `to`,
   *
   * @param that a Graph to fuse with
   * @param from the data source to wire
   * @param to   the data sink to wire
   * @return a Graph representing the fusion of `this` and `that`
   **/

  default Graph fuse(final Graph that, final OutPort from, final InPort to) {
    return compose(that).wire(from, to);
  }

  /**
   * Creates a new Graph which is `this` Graph composed with `that` Graph.
   *
   * @param that a Graph to be composed with (cannot be itself)
   * @return a Graph that represents the composition of `this` and `that`
   **/

  default Graph compose(final Graph that) {
    final HashSet<Graph> graphs = new HashSet<>();
    graphs.add(this);
    graphs.add(that);
    return new ComposedGraphImpl(graphs);
  }

  /**
   * Creates a new Graph based on the current Graph but with
   * the given OutPort wired to the given InPort.
   *
   * @param from the OutPort to wire
   * @param to   the InPort to wire
   * @return a new Graph with the ports wired
   */
  default Graph wire(final OutPort from, final InPort to) {
    return new ComposedGraphImpl(this, from, to);
  }

  default boolean isSource() {
    return outPorts().size() == 1 && inPorts().isEmpty();
  }

  default boolean isSink() {
    return inPorts().size() == 1 && outPorts().isEmpty();
  }

  default boolean isFlow() {
    return inPorts().size() == 1 && outPorts().size() == 1;
  }

  default boolean isClosed() {
    return inPorts().isEmpty() && outPorts().isEmpty();
  }

  /**
   * Creates deepCopy of graph
   * Order of inputs/outputs is preserved
   *
   * @return copy of this Graph with new ports
   */
  Graph deepCopy();
}