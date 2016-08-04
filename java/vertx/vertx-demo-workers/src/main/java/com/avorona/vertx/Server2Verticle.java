package com.avorona.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by avorona on 2016-08-01.
 */
public class Server2Verticle extends AbstractVerticle {

  private static final Logger LOGGER = LogManager.getLogger(Server2Verticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    StaticHandler handler = StaticHandler.create();

    Router router = Router.router(vertx);
    router.route("/*")
        .handler(handler);

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8000, "0.0.0.0", event -> {
          if (event.failed()) {
            LOGGER.error("Server start failed with reason: ", event.cause());
            startFuture.fail(event.cause());
          } else {
            LOGGER.info("Server start listening on the: {}:{}", "0.0.0.0", 8001);
            startFuture.complete();
          }
        });

    vertx.setPeriodic(1000, event -> {
      LOGGER.info("Periodic");
    });
  }
}
