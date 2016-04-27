package com.avorona;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by avorona on 19.04.16.
 */

class ReceiverVerticle extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String increaseEventAddress = "com.avorona.actual-number.increase";

    private int messagesReceived = 0;

    private int actualNumber = 0;

    private MessageConsumer<Integer> consumer;

    private EventBus eventBus;

    @Override
    public void start(Future<Void> future) throws Exception {
        eventBus = vertx.eventBus();

        consumer = eventBus.consumer(increaseEventAddress, this::increaseConsumer);

        log.info("Receiver started");
        future.complete();
    }

    private void increaseConsumer(Message<Integer> msg) {
        Integer body = msg.body();
        if (body != null) {
            actualNumber += body;
        }
        messagesReceived++;
        msg.reply(actualNumber);
    }

    @Override
    public void stop(Future<Void> future) throws Exception {
        if (consumer != null) {
            consumer.unregister();
        }
        log.info("Message received: {}", messagesReceived);
    }
}
