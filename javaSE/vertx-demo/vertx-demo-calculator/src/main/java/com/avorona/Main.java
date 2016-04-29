package com.avorona;

import com.avorona.calculate.CalculateCommand;
import com.avorona.calculate.CalculateCommandCodec;
import com.avorona.calculate.CalculatorVerticle;
import com.avorona.db.MongoCommand;
import com.avorona.db.MongoCommandCodec;
import com.avorona.helper.IpHelper;
import com.avorona.helper.NoSuchInterfaceException;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by com.avorona on 20.04.16.
 */
public class Main {
    public static void main(String[] args) throws SocketException, NoSuchInterfaceException {
        String clusterIface = System.getenv("CLUSTER_IFACE");

        if (clusterIface == null) {
            throw new RuntimeException("Please set ENV CLUSTER_IFACE, that application properly setup");
        }

        String localAddr = IpHelper.ipForIface(clusterIface);
        String clusterPublicHost = System.getenv("CLUSTER_PUBLIC_HOST");
        clusterPublicHost = clusterPublicHost == null || clusterPublicHost.length() == 0 ? localAddr : clusterPublicHost;
        int clusterPublicPort = Integer.parseInt(System.getenv("CLUSTER_PUBLIC_PORT"));
        int vertxPort = Integer.parseInt(System.getenv("VERTX_PORT"));

        JoinConfig joinConfig = new JoinConfig();

        TcpIpConfig tcpIpConfig = new TcpIpConfig()
                .setEnabled(true)
                .setMembers(Arrays.asList(System.getenv("CLUSTER_JOIN").split(",")));

        joinConfig.setMulticastConfig(new MulticastConfig().setEnabled(false))
                .setTcpIpConfig(tcpIpConfig)
                .setAwsConfig(new AwsConfig().setEnabled(false));

        NetworkConfig nconfig = new NetworkConfig()
                .setJoin(joinConfig)
                .setInterfaces(new InterfacesConfig().setInterfaces(Collections.singletonList(localAddr)))
                .setPublicAddress(localAddr)
                .setPort(vertxPort)
                .setPortAutoIncrement(true);

        Config config = new Config()
                .setProperty("hazelcast.local.localAddress", localAddr)
                .setProperty("hazelcast.socket.server.bind.any", "false")
                .setProperty("hazelcast.socket.client.bind", "false")
                .setNetworkConfig(nconfig);

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        HazelcastClusterManager clusterManager = new HazelcastClusterManager(hazelcastInstance);

        VertxOptions vertxOptions = new VertxOptions()
                .setClusterPublicPort(clusterPublicPort)
                .setClusterPublicHost(clusterPublicHost)
                .setClusterPort(clusterPublicPort)
                .setClusterHost(clusterPublicHost)
                .setMetricsOptions(new DropwizardMetricsOptions()
                        .setJmxDomain("demo.vertx.calculator-handler")
                        .setEnabled(true)
                        .setJmxEnabled(true))
                .setClusterManager(clusterManager);

        ApplicationContext context = new ApplicationContext()
                .addPreDeploy(v -> {
                    v.eventBus().registerDefaultCodec(CalculateCommand.class, new CalculateCommandCodec());
                    v.eventBus().registerDefaultCodec(MongoCommand.class, new MongoCommandCodec());
                })
                .addDeployment(new CalculatorVerticle());

        Vertx.clusteredVertx(vertxOptions, context);
    }
}
