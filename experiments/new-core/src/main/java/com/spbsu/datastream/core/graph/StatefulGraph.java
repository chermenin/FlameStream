package com.spbsu.datastream.core.graph;

import com.spbsu.datastream.core.graph.ops.Hash;

/**
 * Created by marnikitta on 2/8/17.
 */
public interface StatefulGraph<T> {
  Hash<T> hash();
}