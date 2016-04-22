package com.avorona.vertx;

import com.avorona.vertx.vo.CalcResVO;
import com.avorona.vertx.vo.ErrorRespVO;
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
 * Created by avorona on 21.04.16.
 */
public class CalculateRequestHandler implements Handler<RoutingContext> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private HazelcastInstance hazelcast;
    private Vertx vertx;

    private int nodeCount = 0;

    public CalculateRequestHandler(HazelcastInstance hazelcast, Vertx vertx) {
        this.hazelcast = hazelcast;
        this.vertx = vertx;
        this.nodeCount = hazelcast.getCluster().getMembers().size();
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

    private Future<Long> parallelCalculate(Long from, Long to) {
        Future<Long> resFuture = Future.future();
        List<Future> futures = new ArrayList<>();

        long batchSize = (to - from) / nodeCount;

        for (long i = 0; i < nodeCount; i++) {
            long start = from + nodeCount * batchSize;
            long end = start + batchSize;
            futures.add(sendCalculate(start, end));
        }

        CompositeFuture.all(futures).setHandler(event -> {
            if (event.succeeded()) {
                resFuture.complete(IntStream.range(0, event.result().size() - 1).mapToLong(value -> {
                    return event.result().result(value);
                }).reduce(0, Long::sum));
            }
        });

        return resFuture;
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

    synchronized void addNode() {
        nodeCount++;
        log.info("1 ");
    }

    synchronized void removeNode() {
        nodeCount--;
        if (nodeCount < 0) {
            nodeCount = 0;
        }
    }

}
