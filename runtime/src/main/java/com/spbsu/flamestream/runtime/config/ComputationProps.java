package com.spbsu.flamestream.runtime.config;

import java.util.Map;

public class ComputationProps {
  private final int maxElementsInGraph;
  private final Map<String, HashGroup> hashGroups;

  public ComputationProps(Map<String, HashGroup> hashGroups, int maxElementsInGraph) {
    this.hashGroups = hashGroups;
    this.maxElementsInGraph = maxElementsInGraph;
  }

  public Map<String, HashGroup> hashGroups() {
    return hashGroups;
  }

  public int maxElementsInGraph() {
    return this.maxElementsInGraph;
  }

  @Override
  public String toString() {
    return "ComputationLayout{" +
            "hashGroups=" + hashGroups +
            '}';
  }
}
