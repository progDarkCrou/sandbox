package com.avorona;

import com.avorona.db.MongoCommand;
import com.avorona.db.MongoCommandCodec;
import com.avorona.helper.NoSuchInterfaceException;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.net.SocketException;

/**
 * Created by com.avorona on 21.04.16.
 */
public class Main {
    public static void main(String[] args) throws SocketException, NoSuchInterfaceException {
        VertxConfigurer configurer = VertxConfigurer.clustered(VertxConfigurer.Join.TCP_IP);
        configurer.setHazelcastAutoconfig(true)
                .setHazelPortAutoincrement(true)
                .setVertxMetricsJmx(true)
                .setVertxAutoConfig(true);

        ApplicationContext context = new ApplicationContext()
                .addDeployment(new MongoAccessorVerticle(System.getenv("MONGO_HOST")))
                .addPreDeploy(v -> v.eventBus().registerDefaultCodec(MongoCommand.class, new MongoCommandCodec()));

        Vertx.clusteredVertx(configurer.build(), context);
    }
}
