package com.avorona.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by avorona on 2016-08-03.
 */
public class ServerVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LogManager.getLogger(ServerVerticle.class);

  private static final int DEFAULT_PORT = 8000;
  private static final boolean isRespond = true;
  private HttpServerOptions serverOptions;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    serverOptions = new HttpServerOptions();
    serverOptions
        .setPort(DEFAULT_PORT)
        .setHost("0.0.0.0");

    HttpServer httpServer = vertx
        .createHttpServer(serverOptions);

    Router router = Router.router(vertx);
//    GET /lajsdflj/asdlfjasld HTTP1.0
//    content-length: 100
//    \r\n
//    alsjdfalk;sjdfa a;sdj f;asdf

    router.get("/hello")
        .handler(requestEvent -> {
          vertx
              .eventBus()
              .send("say-hello", null, helloMessage -> {
                requestEvent
                    .response()
                    .end(helloMessage.result().body().toString());
              });
        });

    httpServer
        .requestHandler((HttpServerRequest requestEvent) -> router.accept(requestEvent))
        .listen(result -> {
          if (result.failed()) {
            LOGGER.error("Server start failed with case: ", result.cause());
            startFuture.fail(result.cause());
          } else {
            LOGGER.info("Server started listening on the: {}:{}", "0.0.0.0", DEFAULT_PORT);
            startFuture.complete();
          }
        });

  }
}
