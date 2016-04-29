package com.avorona.calculate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;

/**
 * Created by com.avorona on 20.04.16.
 */
public class CalculatorVerticle extends AbstractVerticle {

    private String eventName = "calculate";
    private MessageConsumer<CalculateCommand> consumer;

    @Override
    public void start() throws Exception {
        consumer = vertx.eventBus().consumer(eventName, new CalculateCommandHandler(this));
    }

    @Override
    public void stop() throws Exception {
        if (consumer != null) {
            consumer.unregister();
        }
    }

}
