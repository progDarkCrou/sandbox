package com.avorona.calculate;

import com.avorona.db.MongoAccessorVerticle;
import com.avorona.db.MongoCommand;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.stream.IntStream;

/**
 * Created by avorona on 20.04.16.
 */
class CalculateCommandHandler implements Handler<Message<CalculateCommand>> {

    private CalculatorVerticle calculatorVerticle;

    public CalculateCommandHandler(CalculatorVerticle calculatorVerticle) {
        this.calculatorVerticle = calculatorVerticle;
    }

    @Override
    public void handle(Message<CalculateCommand> event) {
        CalculateCommand command = event.body();
        if (command != null) {
            if (command.getFrom() == null) {
                event.fail(1, "No \"From\" provided");
                return;
            }
            if (command.getTo() == null) {
                event.fail(2, "No \"To\" provided");
                return;
            }
            if (command.getFrom() >= command.getTo()) {
                event.fail(3, "\"From\" is greate or equal \"To\"");
                return;
            }
            JsonObject query = new JsonObject().put("from", command.getFrom()).put("to", command.getTo());
            MongoCommand mongoCommand = new MongoCommand(MongoCommand.Command.GET, query);
            calculatorVerticle.vertx.eventBus().send(MongoAccessorVerticle.EVENT_NAME, mongoCommand, queryDB(event, command));
        }
    }

    private Handler<AsyncResult<Message<JsonObject>>> queryDB(Message<CalculateCommand> event, CalculateCommand command) {
        return event1 -> {
            if (event1.succeeded()) {
                event.reply(event1.result().body().getInteger("res"));
            } else {
                calculatorVerticle.vertx.executeBlocking(future -> future.complete(calculateAndSave(command)), calculateResult(event));
            }
        };
    }

    private Handler<AsyncResult<Object>> calculateResult(Message<CalculateCommand> event) {
        return res -> {
            if (res.succeeded()) {
                event.reply(res.result());
            } else {
                event.fail(1, "Something bad occurred");
            }
        };
    }

    private Integer calculateAndSave(CalculateCommand command) {
        Integer result = calculate(command);
        JsonObject query = new JsonObject()
                .put("from", command.getFrom())
                .put("to", command.getTo())
                .put("res", result);
        calculatorVerticle.vertx.eventBus().send(MongoAccessorVerticle.EVENT_NAME,
                new MongoCommand(MongoCommand.Command.CREATE, query));
        return result;
    }

    private Integer calculate(CalculateCommand command) {
        return IntStream.range(command.getFrom(), command.getTo()).parallel().reduce(0, Integer::sum);
    }
}
