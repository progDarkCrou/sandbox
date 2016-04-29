package com.avorona;

import com.avorona.db.MongoCommand;
import com.avorona.db.MongoCommandCodec;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Created by com.avorona on 21.04.16.
 */
public class Main {
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

        VertxOptions vertxOptions = new VertxOptions();

        vertxOptions.setMetricsOptions(new DropwizardMetricsOptions()
                .setJmxDomain("demo.vertx.mongo-accessor")
                .setEnabled(true)
                .setJmxEnabled(true));

        vertxOptions.setClusterManager(clusterManager);

        ApplicationContext context = new ApplicationContext()
                .addDeployment(new MongoAccessorVerticle(System.getenv("MONGO_HOST")))
                .addPreDeploy(v -> v.eventBus().registerDefaultCodec(MongoCommand.class, new MongoCommandCodec()));

        Vertx.clusteredVertx(vertxOptions, context);
    }
}
