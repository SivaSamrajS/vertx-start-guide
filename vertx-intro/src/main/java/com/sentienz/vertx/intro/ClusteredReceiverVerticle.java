package com.sentienz.vertx.intro;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class ClusteredReceiverVerticle extends AbstractVerticle {

  private String name;

  public ClusteredReceiverVerticle(String name) {
    this.name = name;
  }

  public static void main(String[] args) {
    ClusterManager mgr = new HazelcastClusterManager();
    Vertx.clusteredVertx(new VertxOptions().setClusterManager(mgr).setClustered(true), cluster -> {
      if (cluster.succeeded()) {
        cluster.result().deployVerticle(new ClusteredReceiverVerticle("V2R3"), res -> {
          if (res.succeeded()) {
            System.out.println("Deployment Sender id is: " + res.result());
          } else {
            System.out.println("Deployment Sender failed!");
            System.out.println(res.cause());
          }
        });
      }
    });
  }

  public void start() {
    System.out.println("Event Bus Reciever for Vertex2 initialised");
    vertx.eventBus().consumer("anAddress", message -> {
      System.out.println(this.name + " received message for Clustered Vertex: " + message.body());
    });
  }
}


