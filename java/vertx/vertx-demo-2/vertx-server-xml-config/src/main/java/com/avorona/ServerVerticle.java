package com.avorona;

import com.avorona.calculate.CalculateCommand;
import com.avorona.calculate.CalculateCommandCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

/**
 * Created by avorona on 09.05.16.
 */
public class ServerVerticle extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().registerDefaultCodec(CalculateCommand.class, new CalculateCommandCodec());

        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        CalculateRequestHandler handler = new CalculateRequestHandler(vertx);
        router.route().method(HttpMethod.GET).path("/calculate").handler(handler);

        httpServer.requestHandler(router::accept).listen(8080);

        vertx.deployVerticle(new ReceiverVerticle(), e -> {
            startFuture.complete();
            log.info("Start succeed");
        });
    }
}
