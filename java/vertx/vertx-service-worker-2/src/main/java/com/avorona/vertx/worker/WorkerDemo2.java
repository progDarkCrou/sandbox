package com.avorona.vertx.worker;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Created by avorona on 2016-08-04.
 */
public class WorkerDemo2 {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Router router = Router.router(vertx);

    router.route()
        .handler(StaticHandler.create());

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8000, "0.0.0.0");
  }
}
