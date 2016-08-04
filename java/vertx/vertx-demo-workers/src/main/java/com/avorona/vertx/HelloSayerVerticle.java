package com.avorona.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by avorona on 2016-08-03.
 */
public class HelloSayerVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LogManager.getLogger(HelloSayerVerticle.class);

  private HelloSayerMessageHandler messageHandler;

  public HelloSayerVerticle(HelloSayerMessageHandler helloSayerMessageHandler) {
    this.messageHandler = helloSayerMessageHandler;
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    MessageConsumer<Void> consumer = vertx
        .eventBus()
        .consumer("say-hello");

    consumer
        .handler(messageHandler);

    LOGGER.info("Hello sayer successfully deployed");
  }

  public static class HelloSayerMessageHandler implements Handler<Message<Void>> {

    private int privateCounter = 0;

    @Override
    public void handle(Message<Void> event) {
      LOGGER.info("Received \"say-hello\" request");
      event.reply(String.format("Hello number: %d", ++privateCounter));
    }

  }
}
