package com.spbsu.flamestream.runtime.master.acker.api.registry;

import akka.actor.ActorRef;

public class RegisteredAlreadyRegisteredFront {
  public FrontTicket frontTicket;
  public ActorRef sender;

  public RegisteredAlreadyRegisteredFront(FrontTicket frontTicket, ActorRef sender) {
    this.frontTicket = frontTicket;
    this.sender = sender;
  }
}

