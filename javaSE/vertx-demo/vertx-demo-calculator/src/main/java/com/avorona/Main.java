package com.avorona;

import com.avorona.calculate.CalculateCommand;
import com.avorona.calculate.CalculateCommandCodec;
import com.avorona.calculate.CalculatorVerticle;
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
 * Created by avorona on 20.04.16.
 */
public class Main {
    public static void main(String[] args) {
        JoinConfig joinConfig = new JoinConfig();

        String joinAddress = System.getenv("CLUSTER_JOIN");
        if (joinAddress != null) {
            TcpIpConfig tcpIpConfig = new TcpIpConfig()
                    .addMember(joinAddress)
                    .setEnabled(true);

            joinConfig.setMulticastConfig(new MulticastConfig().setEnabled(false))
                    .setAwsConfig(new AwsConfig().setEnabled(false))
                    .setTcpIpConfig(tcpIpConfig);
        } else {
            MulticastConfig multicastConfig = new MulticastConfig()
                    .setEnabled(true)
                    .setMulticastGroup("172.18.0.255");

            joinConfig.setMulticastConfig(multicastConfig)
                    .setAwsConfig(new AwsConfig().setEnabled(false));
        }

        NetworkConfig nconfig = new NetworkConfig()
                .setReuseAddress(false)
                .setPortAutoIncrement(true)
                .setJoin(joinConfig);
        nconfig.setPortCount(100);

        Config config = new Config()
                .setNetworkConfig(nconfig);

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        HazelcastClusterManager clusterManager = new HazelcastClusterManager(hazelcastInstance);

        VertxOptions vertxOptions = new VertxOptions();

        vertxOptions.setMetricsOptions(new DropwizardMetricsOptions()
                .setJmxDomain("demo.vertx.calculator-handler")
                .setEnabled(true)
                .setJmxEnabled(true));

        vertxOptions.setClusterManager(clusterManager);

        ApplicationContext context = new ApplicationContext()
                .addPreDeploy(v -> {
                    v.eventBus().registerDefaultCodec(CalculateCommand.class, new CalculateCommandCodec());
                    v.eventBus().registerDefaultCodec(MongoCommand.class, new MongoCommandCodec());
                })
                .addDeployment(new CalculatorVerticle());

        Vertx.clusteredVertx(vertxOptions, context);
    }
}
