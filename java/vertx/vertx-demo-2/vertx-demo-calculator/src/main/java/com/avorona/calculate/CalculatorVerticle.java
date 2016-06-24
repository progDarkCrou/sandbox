package com.avorona.calculate;

import com.avorona.db.MongoCommand;
import com.avorona.db.MongoCommandCodec;
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
        vertx.eventBus().registerDefaultCodec(CalculateCommand.class, new CalculateCommandCodec());
        vertx.eventBus().registerDefaultCodec(MongoCommand.class, new MongoCommandCodec());
        consumer = vertx.eventBus().consumer(eventName, new CalculateCommandHandler(this));
    }

    @Override
    public void stop() throws Exception {
        if (consumer != null) {
            consumer.unregister();
        }
    }

}
