package com.avorona;

import com.avorona.calculate.CalculateCommand;
import com.avorona.vo.CalcResVO;
import com.avorona.vo.ErrorRespVO;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by com.avorona on 21.04.16.
 */
public class CalculateRequestHandler implements Handler<RoutingContext> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Vertx vertx;

    public CalculateRequestHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void handle(RoutingContext event) {
        log.info("Received calculate request");

        String fromParam = event.request().getParam("from");
        String toParam = event.request().getParam("to");

        if (fromParam != null && toParam != null) {
            Long from = Long.valueOf(fromParam);
            Long to = Long.valueOf(toParam);
            if (to <= from) {
                event.response().end(
                        new ErrorRespVO("\"From\" has to be less than \"to\" and and both have to be > 0").toString());
            } else {
                Future<Long> resFuture = sendCalculate(from, to);
                resFuture.setHandler(res -> {
                    if (res.succeeded()) {
                        event.response().end(new CalcResVO(from, to, res.result()).toString());
                    } else {
                        event.response().end(new ErrorRespVO(res.result()).toString());
                    }
                });
            }
        } else {
            event.response().end("Cannot calculate, please provide \"from\" and \"to\" query parameters");
        }
    }

    private Future<Long> sendCalculate(Long from, Long to) {
        Future<Long> future = Future.future();
        vertx.eventBus().send("calculate", new CalculateCommand(from, to), handleCalculateResult(future));
        return future;
    }

    private Handler<AsyncResult<Message<Long>>> handleCalculateResult(Future<Long> toResolve) {
        return event -> {
            if (event.succeeded()) {
                toResolve.complete(event.result().body());
            } else {
                toResolve.fail("Calculation failed");
            }
        };
    }
}
