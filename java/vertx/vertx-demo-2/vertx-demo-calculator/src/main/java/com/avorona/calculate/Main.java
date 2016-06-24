package com.avorona.calculate;

import com.hazelcast.config.ClasspathXmlConfig;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Created by avorona on 09.05.16.
 */
public class Main {
    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        HazelcastClusterManager clusterManager = new HazelcastClusterManager();
        clusterManager.setConfig(new ClasspathXmlConfig("hazelcast.xml"));
        options.setClusterManager(clusterManager);
        Vertx.clusteredVertx(options, event -> {
            if (event.succeeded()) {
                System.out.println("Clustered");
                event.result().deployVerticle(new CalculatorVerticle());
            }
        });
    }
}
