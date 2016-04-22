package com.avorona.calculate;

import io.vertx.core.AbstractVerticle;

/**
 * Created by avorona on 20.04.16.
 */
public class CalculatorVerticle extends AbstractVerticle {

    private String eventName = "calculate";

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(eventName, event -> {
            event.
        })
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
