package com.spbsu.datastream.core.materializer.routing;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.spbsu.datastream.core.DataItem;
import com.spbsu.datastream.core.HashRange;
import com.spbsu.datastream.core.materializer.AddressedMessage;
import scala.Option;

import java.util.HashMap;
import java.util.Map;

public class RemoteRouter extends UntypedActor {
  private final LoggingAdapter LOG = Logging.getLogger(context().system(), self());

  private final Map<HashRange, ActorRef> routingTable;

  private RemoteRouter(final Map<HashRange, ActorRef> routingTable) {
    this.routingTable = routingTable;
  }

  public static Props props(final Map<HashRange, ActorRef> hashMapping) {
    return Props.create(RemoteRouter.class, new HashMap<>(hashMapping));
  }

  @Override
  public void preStart() throws Exception {
    LOG.info("Starting...");
    super.preStart();
  }

  @Override
  public void postStop() throws Exception {
    LOG.info("Stopped");
    super.postStop();
  }

  @Override
  public void preRestart(final Throwable reason, final Option<Object> message) throws Exception {
    LOG.error("Restarting, reason: {}, message: {}", reason, message);
    super.preRestart(reason, message);
  }

  @Override
  public void onReceive(final Object message) throws Throwable {
    if (message instanceof AddressedMessage) {
      final AddressedMessage<?> addressedMessage = (AddressedMessage<?>) message;
      if (addressedMessage.payload() instanceof DataItem) {
        int hash = ((DataItem) addressedMessage.payload()).hash();
        actorForHash(hash).tell(message, self());
      } else {
        routingTable.values().forEach(a -> a.tell(message, self()));
      }
    } else {
      unhandled(message);
    }
  }

  private ActorRef actorForHash(final int hash) {
    return routingTable.entrySet().stream().filter((e) -> e.getKey().isIn(hash))
            .map(Map.Entry::getValue).findAny()
            .orElse(context().system().deadLetters());
  }
}
