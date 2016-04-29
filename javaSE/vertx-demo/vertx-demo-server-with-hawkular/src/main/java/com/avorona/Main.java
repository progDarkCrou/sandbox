package com.avorona;

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
import io.vertx.ext.hawkular.VertxHawkularOptions;
import io.vertx.ext.web.Router;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Created by com.avorona on 18.04.16.
 */
public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        JoinConfig joinConfig = new JoinConfig()
                .setMulticastConfig(new MulticastConfig().setEnabled(false));

        String joinAddress = System.getenv("CLUSTER_JOIN");
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

        VertxHawkularOptions metrics = new VertxHawkularOptions()
                .setHost(System.getenv("HAWKULAR_HOST"))
                .setPort(Integer.valueOf(System.getenv("HAWKULAR_PORT")))
                .setEnabled(true)
                .setMetricsBridgeAddress("metrics")
                .setMetricsBridgeEnabled(true);

        VertxOptions options = new VertxOptions()
                .setMetricsOptions(metrics)
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

                httpServer.requestHandler(router::accept).listen(8080);

            } else if (var.failed()) {
                log.info("Start failed");
            }
        });
    }
}
