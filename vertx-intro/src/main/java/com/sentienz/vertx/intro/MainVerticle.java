package com.sentienz.vertx.intro;

import com.hazelcast.config.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() throws InterruptedException {

    // 1st simple example
    log.info("Hello, Welcome to the Vert.x World....");

    // 2nd Example: HTTP Server
    vertx.createHttpServer(new HttpServerOptions().setPort(8080)).requestHandler(req -> {
      req.response().end("<body><h1>Welcome to Vertx WebPage!</h1></body>");
    }).listen();

    // 3rd Example: Verticle Inside a verticle
    vertx.deployVerticle(new SecondVerticle());

    //5th Example: Cluster Event Bus
    Thread.sleep(9000);
    vertx.deployVerticle(new EventBusSenderVerticle());
  }


  public static void main(String[] args) throws InterruptedException {

    Vertx vertx = Vertx.vertx();

    // 1st, 2nd , 3rd example
    vertx.deployVerticle(new MainVerticle());

    // 4th Example: Event Bus
    vertx.deployVerticle(new EventBusReceiverVerticle("RV1"));
    vertx.deployVerticle(new EventBusReceiverVerticle("RV2"));
    Thread.sleep(3000);
    vertx.deployVerticle(new EventBusSenderVerticle());

    // 5th Example: HazelCast Configuration
    Config hazelcastConfig = new Config();
    ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
    Vertx.clusteredVertx(
        new VertxOptions().setClusterManager(mgr).setClustered(true).setClusterHost("localhost"),
        cluster -> {
          try {
            if (cluster.succeeded()) {
              cluster.result().deployVerticle(new MainVerticle(), res -> {
                if (res.succeeded()) {
                  System.out.println("Deployment1 id is: " + res.result());
                } else {
                  System.out.println("Deployment1 failed!");
                }
              });
              cluster.result().deployVerticle(new EventBusReceiverVerticle("RV1"), res -> {
                if (res.succeeded()) {
                  System.out.println("Deployment2 id is: " + res.result());
                } else {
                  System.out.println("Deployment2 failed!");
                }
              });
              cluster.result().deployVerticle(new EventBusReceiverVerticle("RV2"), res -> {
                if (res.succeeded()) {
                  System.out.println("Deployment3 id is: " + res.result());
                } else {
                  System.out.println("Deployment3 failed!");
                }
              });
            } else {
              System.out.println("Cluster up failed: " + cluster.cause());
            }
          } catch (Exception e) {
          }
        });

  }
}
