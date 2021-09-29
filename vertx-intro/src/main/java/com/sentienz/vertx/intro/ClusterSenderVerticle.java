package com.sentienz.vertx.intro;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class ClusterSenderVerticle {

  public static void main(String[] args) {
    // Config hazelcastConfig = new Config();
    // ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
    ClusterManager mgr = new HazelcastClusterManager();
    Vertx.clusteredVertx(new VertxOptions().setClusterManager(mgr).setClustered(true).setClusterHost("localhost"),
        cluster -> {
      if (cluster.succeeded()) {
        cluster.result().deployVerticle(new EventBusSenderVerticle(), res -> {
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

}
