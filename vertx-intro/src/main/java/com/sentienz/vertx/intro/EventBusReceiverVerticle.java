package com.sentienz.vertx.intro;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class EventBusReceiverVerticle extends AbstractVerticle {

  private String name;

  public EventBusReceiverVerticle(String name) {
    this.name = name;
  }

  public void start(Future<Void> startFuture) {

    System.out.println("Event Bus Receiver for App initialising");

    vertx.eventBus().consumer("anAddress", message -> {
      System.out.println(this.name + " received message: " + message.body());
    });
  }

}
