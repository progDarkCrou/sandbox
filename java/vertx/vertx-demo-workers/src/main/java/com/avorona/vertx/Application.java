package com.avorona.vertx;

import io.vertx.core.Vertx;

/**
 * Created by avorona on 2016-08-01.
 */
public class Application {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    HelloSayerVerticle.HelloSayerMessageHandler helloSayerMessageHandler =
        new HelloSayerVerticle.HelloSayerMessageHandler();

    HelloSayerVerticle verticle2 = new HelloSayerVerticle(helloSayerMessageHandler);
    HelloSayerVerticle verticle1 = new HelloSayerVerticle(helloSayerMessageHandler);

    vertx.deployVerticle(verticle1);
    vertx.deployVerticle(verticle2);
    vertx.deployVerticle(ServerVerticle.class.getName());
  }
}
