package com.sentienz.vertx.intro;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class EventBusSenderVerticle extends AbstractVerticle {

  public void start(Future<Void> startFuture) {

   // Will be sent to atmost one of the handlers registered to the address.
    vertx.eventBus().send("anAddress", "message send");

  }

}
