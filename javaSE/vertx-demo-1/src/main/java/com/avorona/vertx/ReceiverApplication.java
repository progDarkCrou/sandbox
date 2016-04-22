package com.avorona.vertx;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.web.Router;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Created by avorona on 18.04.16.
 */
public class ReceiverApplication {
    private static Logger log = LoggerFactory.getLogger(ReceiverApplication.class);

    public static void main(String[] args) {
        NetworkConfig nconfig = new NetworkConfig();
        nconfig.setPort(9001);
        nconfig.setPublicAddress("172.30.30.64");

        Config config = new Config();
        config.setNetworkConfig(nconfig);

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        HazelcastClusterManager clusterManager = new HazelcastClusterManager(hazelcastInstance);

        VertxOptions options = new VertxOptions();

        options.setMetricsOptions(new DropwizardMetricsOptions()
                .setEnabled(true)
                .setJmxEnabled(true)
                .setJmxDomain("demo.vertx.request-handler"))
                .setClusterManager(clusterManager);

        Vertx.clusteredVertx(options, var -> {
            if (var.succeeded()) {
                log.info("Start succeed");
                Vertx vertx = var.result();
                vertx.eventBus().registerDefaultCodec(CalculateCommand.class, new CalculateCommandCodec());

                HttpServer httpServer = vertx.createHttpServer();
                Router router = Router.router(vertx);
                CalculateRequestHandler handler = new CalculateRequestHandler(hazelcastInstance, vertx);
                router.route().method(HttpMethod.GET).path("/calculate").handler(handler);

                httpServer.requestHandler(router::accept).listen(8000);

            } else if (var.failed()) {
                log.info("Start failed");
            }
        });
    }
}
