package com.avorona.vertx;

import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.StaticHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by avorona on 2016-08-01.
 */
public class Server1Verticle extends AbstractVerticle {

  private static final Logger LOGGER = LogManager.getLogger(Server1Verticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    LOGGER.debug("Starting configuration of the server");
    StaticHandler staticHandler = StaticHandler.create();
    staticHandler.setCachingEnabled(false);

    Router router = Router.router(vertx);
    router.route("/*").handler(staticHandler);

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8000, "0.0.0.0", r -> {
          if (r.failed()) {
            LOGGER.error("Server failed to start on the: {}:{}", "0.0.0.0", 8000);
            LOGGER.error("Server start fail reason: ", r.cause());
            startFuture.fail(r.cause());
          } else {
            LOGGER.info("Server successfully started listening on the: {}:{}", "0.0.0.0", 8000);
            startFuture.complete();
          }
        });
  }
}
