package com.avorona.calculate;

import com.avorona.db.MongoCommand;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.stream.LongStream;

/**
 * Created by avorona on 20.04.16.
 */
class CalculateCommandHandler implements Handler<Message<CalculateCommand>> {

    private static final String MONGO_ADDRESS = "vertx.demo.mongo-calculate";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private CalculatorVerticle calculatorVerticle;

    public CalculateCommandHandler(CalculatorVerticle calculatorVerticle) {
        this.calculatorVerticle = calculatorVerticle;
    }

    @Override
    public void handle(Message<CalculateCommand> event) {
        CalculateCommand command = event.body();
        if (command != null) {
            logger.info("Received calculation request: " + command);
            if (command.getFrom() == null) {
                event.fail(1, "No \"From\" provided");
                return;
            }
            if (command.getTo() == null) {
                event.fail(2, "No \"To\" provided");
                return;
            }
            if (command.getFrom() >= command.getTo()) {
                event.fail(3, "\"From\" is greater or equal \"To\"");
                return;
            }
            JsonObject query = new JsonObject().put("from", command.getFrom()).put("to", command.getTo());
            MongoCommand mongoCommand = new MongoCommand(MongoCommand.Command.GET_ONE, query);
            calculatorVerticle.getVertx()
                    .eventBus()
                    .send(MONGO_ADDRESS, mongoCommand, queryDB(event, command));
        }
    }

    private Handler<AsyncResult<Message<JsonObject>>> queryDB(Message<CalculateCommand> event, CalculateCommand command) {
        return event1 -> {
            if (event1.succeeded() && event1.result().body() != null) {
                logger.info("Found in DB: " + event1.result().body());
                event.reply(event1.result().body().getLong("res"));
            } else {
                logger.info("Did not found in DB, Going to calculate request manually: " + command);
                calculatorVerticle.getVertx().executeBlocking(future -> future.complete(calculateAndSave(command)), calculationResult(event));
            }
        };
    }

    private Handler<AsyncResult<Object>> calculationResult(Message<CalculateCommand> event) {
        return res -> {
            if (res.succeeded()) {
                event.reply(res.result());
            } else {
                event.fail(1, "Something bad occurred");
            }
        };
    }

    private Long calculateAndSave(CalculateCommand command) {
        Long result = calculate(command);
        JsonObject query = new JsonObject()
                .put("from", command.getFrom())
                .put("to", command.getTo())
                .put("res", result);
        logger.info("Going to save calculated result: " + result);
        calculatorVerticle.getVertx().eventBus().send(MONGO_ADDRESS,
                new MongoCommand(MongoCommand.Command.CREATE, query), event -> {
                    if (event.succeeded()) {
                        logger.info("Saved successfully result: " + result);
                    } else {
                        logger.info("Failed to save result: " + result + event.result());
                    }
                });
        return result;
    }

    private Long calculate(CalculateCommand command) {
        return LongStream.range(command.getFrom(), command.getTo()).parallel().reduce(0, Long::sum);
    }
}
