package com.spbsu.datastream.core.graph;

import java.util.*;
import java.util.stream.Collectors;

public class TheGraph extends AbstractComposedGraph<ShardMappedGraph> {
  TheGraph(final Set<ShardMappedGraph> graph) {
    super(graph);
  }

  TheGraph(final ShardMappedGraph graph, final OutPort from, final InPort to) {
    super(graph, from, to);
  }

  TheGraph(final Set<ShardMappedGraph> graphs,
           final Map<OutPort, InPort> wires) {
    super(graphs, wires);
  }

  //For deep copy only
  private TheGraph(
          final Map<InPort, OutPort> upstreams,
          final Map<OutPort, InPort> downstreams, final List<InPort> inPorts,
          final List<OutPort> outPorts,
          final Set<ShardMappedGraph> subGraphs) {
    super(upstreams, downstreams, inPorts, outPorts, subGraphs);
  }

  @Override
  public Graph deepCopy() {
    final List<ShardMappedGraph> subGraphs = new ArrayList<>(subGraphs());
    final List<ShardMappedGraph> subGraphsCopy = subGraphs.stream().map(Graph::deepCopy)
            .map(ShardMappedGraph.class::cast)
            .collect(Collectors.toList());
    final Map<InPort, InPort> inPortsMapping = inPortsMapping(subGraphs, subGraphsCopy);
    final Map<OutPort, OutPort> outPortsMapping = outPortsMapping(subGraphs, subGraphsCopy);

    final Map<InPort, OutPort> upstreams = mappedUpstreams(upstreams(), inPortsMapping, outPortsMapping);
    final Map<OutPort, InPort> downstreams = mappedDownstreams(downstreams(), inPortsMapping, outPortsMapping);
    final List<InPort> inPorts = mappedInPorts(inPorts(), inPortsMapping);
    final List<OutPort> outPorts = mappedOutPorts(outPorts(), outPortsMapping);
    return new TheGraph(upstreams, downstreams, inPorts, outPorts, new HashSet<>(subGraphsCopy));
  }

  @Override
  public boolean equals(final Object o) {
    return super.equals(o);
  }

  @Override
  public String toString() {
    return "TheGraph{" + "upstreams=" + upstreams() +
            ", downstreams=" + downstreams() +
            ", inPorts=" + inPorts() +
            ", outPorts=" + outPorts() +
            ", subGraphs=" + subGraphs() +
            '}';
  }
}