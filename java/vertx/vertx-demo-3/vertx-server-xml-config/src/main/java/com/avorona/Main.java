package com.avorona;

import com.avorona.calculate.CalculateCommand;
import com.avorona.calculate.CalculateCommandCodec;
import com.avorona.helper.NoSuchInterfaceException;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

import java.net.SocketException;

/**
 * Created by com.avorona on 18.04.16.
 */
public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SocketException, NoSuchInterfaceException {
        VertxConfigurer configurer = VertxConfigurer.clustered();
        configurer.setHazelcastAutoconfig(true)
                .setHazelPortAutoincrement(true)
                .setVertxMetricsJmx(true)
                .setVertxAutoConfig(true);

        VertxOptions vertxOptions = configurer.build();

        Vertx.clusteredVertx(vertxOptions, var -> {
            if (var.succeeded()) {
                Vertx vertx = var.result();
                vertx.eventBus().registerDefaultCodec(CalculateCommand.class, new CalculateCommandCodec());

                HttpServer httpServer = vertx.createHttpServer();
                Router router = Router.router(vertx);
                CalculateRequestHandler handler = new CalculateRequestHandler(vertx);
                router.route().method(HttpMethod.GET).path("/calculate").handler(handler);

                httpServer.requestHandler(router::accept).listen(8080);
                log.info("Start succeed");
            } else if (var.failed()) {
                log.info("Start failed");
            }
        });
    }

}
