package com.avorona.vertx;

import com.avorona.calculate.CalculateCommand;
import com.avorona.calculate.CalculateCommandCodec;
import com.hazelcast.config.*;
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
public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        JoinConfig joinConfig = new JoinConfig()
                .setMulticastConfig(new MulticastConfig().setEnabled(false));

        String joinAddress = System.getProperty("cluster.join");
        if (joinAddress != null) {
            TcpIpConfig tcpIpConfig = new TcpIpConfig()
                    .addMember(joinAddress)
                    .setEnabled(true);
            joinConfig.setTcpIpConfig(tcpIpConfig);
        }

        NetworkConfig nconfig = new NetworkConfig()
                .setPortAutoIncrement(true)
                .setJoin(joinConfig);

        Config config = new Config()
                .setNetworkConfig(nconfig);

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
