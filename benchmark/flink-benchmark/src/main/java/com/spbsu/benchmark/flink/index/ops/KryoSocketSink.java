package com.spbsu.benchmark.flink.index.ops;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.spbsu.benchmark.flink.index.Result;
import com.spbsu.flamestream.example.bl.index.model.WordIndexAdd;
import com.spbsu.flamestream.example.bl.index.model.WordIndexRemove;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.jetbrains.annotations.Nullable;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KryoSocketSink extends RichSinkFunction<Result> {
  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(KryoSocketSink.class);
  public static final int OUTPUT_BUFFER_SIZE = 1_000_000;
  public static final int CONNECTION_AWAIT_TIMEOUT = 5000;

  private final String hostName;
  private final int port;

  @Nullable
  private transient Client client = null;

  public KryoSocketSink(String hostName, int port) {
    this.hostName = hostName;
    this.port = port;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    client = new Client(OUTPUT_BUFFER_SIZE, 1234);
    client.getKryo().register(WordIndexAdd.class);
    client.getKryo().register(WordIndexRemove.class);
    client.getKryo().register(long[].class);
    ((Kryo.DefaultInstantiatorStrategy) client.getKryo()
            .getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());

    client.addListener(new Listener() {
      @Override
      public void disconnected(Connection connection) {
        LOG.warn("Sink has been disconnected {}", connection);
      }
    });

    LOG.info("Connecting to {}:{}", hostName, port);
    client.start();
    client.connect(CONNECTION_AWAIT_TIMEOUT, hostName, port);
    LOG.info("Connected to {}:{}", hostName, port);
  }

  @Override
  public void invoke(Result value) {
    if (client != null && client.isConnected()) {
      client.sendTCP(value.wordIndexAdd());
      if (value.wordIndexRemove() != null) {
        client.sendTCP(value.wordIndexRemove());
      }
    } else {
      LOG.warn("Writing to closed log");
    }
  }

  @Override
  public void close() {
    if (client != null) {
      LOG.info("Closing sink connection");
      client.close();
      client.stop();
    }
  }
}

