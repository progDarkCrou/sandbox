package com.avorona;

import com.avorona.helper.IpHelper;
import com.avorona.helper.NoSuchInterfaceException;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by avorona on 04.05.16.
 */
public class VertxConfigurer {

    private boolean hazelcastAutoconfig;
    private boolean hazelPortAutoincrement;
    private boolean vertxAutoConfig;
    private boolean vertxMetricsJmx;

    private String hazelTcpJoinEnvName = "HAZEL_TCP_JOIN";
    private String hazelMultiJoinEnvName = "HAZEL_MULTI_GROUP";
    private String hazelMultiJoinPortEnvName = "HAZEL_MULTI_PORT";
    private String hazelIfaceEnvName = "HAZEL_IFACE_NAME";
    private String hazelPublicHostEnvName = "HAZEL_PUBLIC_HOST";

    private String hazelPortEnvName = "HAZEL_PUBLIC_PORT";
    private String vertxPublicAddrEnvName = "VERTX_PUBLIC_ADDR";
    private String vertxAddrEnvName = "VERTX_ADDR";
    private String vertxPublicPortEnvName = "VERTX_PUBLIC_PORT";
    private String vertxPortEnvName = "VERTX_PORT";

    private JoinConfig joinConfig = new JoinConfig();
    private MulticastConfig multicastConfig = new MulticastConfig().setEnabled(true);
    private TcpIpConfig tcpIpConfig = new TcpIpConfig().setEnabled(false);
    private AwsConfig awsConfig = new AwsConfig().setEnabled(false);

    private VertxConfigurer() {
    }

    public static VertxConfigurer clustered() {
        return clustered(null);
    }

    public static VertxConfigurer clustered(Join join) {
        VertxConfigurer configurer = new VertxConfigurer();
        if (join != null && join.equals(Join.TCP_IP)) {
            configurer.multicastConfig.setEnabled(false);
            configurer.tcpIpConfig.setEnabled(true);
        }
        return configurer;
    }

    public VertxOptions build() throws SocketException, NoSuchInterfaceException {
        VertxOptions vertxOptions = new VertxOptions()
                .setClustered(true)
                .setClusterPingInterval(10000)
                .setClusterPingReplyInterval(10000);
        NetworkConfig nconfig = new NetworkConfig();
        Config config = new ClasspathXmlConfig("hazelcast.xml");

        String hazelIface = System.getenv(hazelIfaceEnvName);
        if (hazelIface == null) {
            throw new IllegalStateException("Please set environment variable HAZEL_IFACE_NAME, " +
                    "for application properly setup");
        }
        String hazelHost = IpHelper.ipForIface(hazelIface);
        InterfacesConfig interfaces = new InterfacesConfig()
                .setEnabled(true)
                .setInterfaces(Collections.singletonList(hazelHost));
        nconfig.setInterfaces(interfaces);

        String hazelPortEnv = System.getenv(hazelPortEnvName);
        if (hazelPortEnv != null) {
            nconfig.setPort(Integer.parseInt(hazelPortEnv));
        } else {
            throw new IllegalStateException("HAZEL_PUBLIC_PORT environment variable is not set or is wrong");
        }

        String hazelPublicHost = System.getenv(hazelPublicHostEnvName);
        if (hazelPublicHost == null && !hazelcastAutoconfig) {
            throw new IllegalStateException("Hazelcast public host environment variable is not set and " +
                    "autoconfiguration is disabled");
        } else if (hazelcastAutoconfig) {
            hazelPublicHost = hazelHost;
        }
        nconfig.setPublicAddress(hazelPublicHost);

        String vertxHost = System.getenv(vertxAddrEnvName);
        if (vertxHost == null && !vertxAutoConfig) {
            throw new IllegalStateException("Vertx host environment variable is not set");
        } else if (vertxAutoConfig) {
            vertxHost = hazelHost;
        }
        vertxOptions.setClusterHost(vertxHost);

        String vertxPortEnv = System.getenv(vertxPortEnvName);
        if (vertxPortEnv != null) {
            vertxOptions.setClusterPort(Integer.parseInt(vertxPortEnv));
        } else {
            throw new IllegalStateException("VERTX_PORT environment variable is not set or is set wrong");
        }

        String vertxPublicHost = System.getenv(vertxPublicAddrEnvName);
        if (vertxPublicHost == null && vertxAutoConfig) {
            vertxPublicHost = vertxHost;
        } else if (!vertxAutoConfig) {
            throw new IllegalStateException("Vertx public host environment variable is not set and autoconfigure is " +
                    "disabled");
        }
        vertxOptions.setClusterPublicHost(vertxPublicHost);

        String vertxPublicPortEnv = System.getenv(vertxPublicPortEnvName);
        if (vertxPublicPortEnv == null && vertxAutoConfig) {
            vertxPublicPortEnv = vertxPortEnv;
        } else if (!vertxAutoConfig) {
            throw new IllegalStateException("Vertx public port environment variable is not set and autoconfigure is " +
                    "disabled");
        }
        vertxOptions.setClusterPublicPort(Integer.parseInt(vertxPublicPortEnv));

        if (tcpIpConfig.isEnabled()) {
            tcpIpConfig.setMembers(Arrays.asList(System.getenv(hazelTcpJoinEnvName).split(",")));
        }

        if (multicastConfig.isEnabled()) {
            multicastConfig.setMulticastGroup(System.getenv(hazelMultiJoinEnvName))
                    .setMulticastPort(Integer.parseInt(System.getenv(hazelMultiJoinPortEnvName)))
                    .setMulticastTimeToLive(32)
                    .setMulticastTimeoutSeconds(5);
        }

        joinConfig.setMulticastConfig(multicastConfig)
                .setTcpIpConfig(tcpIpConfig)
                .setAwsConfig(awsConfig);

        nconfig.setJoin(joinConfig).setPortAutoIncrement(hazelPortAutoincrement);

        config.setProperty("hazelcast.local.localAddress", hazelHost)
                .setNetworkConfig(nconfig);

//        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance(config);
//        HazelcastClusterManager clusterManager = new HazelcastClusterManager(hazelcast);
        HazelcastClusterManager clusterManager = new HazelcastClusterManager(config);
        vertxOptions.setClusterManager(clusterManager);

        return vertxOptions;
    }

    public String getHazelTcpJoinEnvName() {
        return hazelTcpJoinEnvName;
    }

    public VertxConfigurer setHazelTcpJoinEnvName(String hazelTcpJoinEnvName) {
        this.hazelTcpJoinEnvName = hazelTcpJoinEnvName;
        return this;
    }

    public String getHazelIfaceEnvName() {
        return hazelIfaceEnvName;
    }

    public VertxConfigurer setHazelIfaceEnvName(String hazelIfaceEnvName) {
        this.hazelIfaceEnvName = hazelIfaceEnvName;
        return this;
    }

    public String getHazelPublicHostEnvName() {
        return hazelPublicHostEnvName;
    }

    public VertxConfigurer setHazelPublicHostEnvName(String hazelPublicHostEnvName) {
        this.hazelPublicHostEnvName = hazelPublicHostEnvName;
        return this;
    }

    public String getVertxPublicAddrEnvName() {
        return vertxPublicAddrEnvName;
    }

    public VertxConfigurer setVertxPublicAddrEnvName(String vertxPublicAddrEnvName) {
        this.vertxPublicAddrEnvName = vertxPublicAddrEnvName;
        return this;
    }

    public String getVertxAddrEnvName() {
        return vertxAddrEnvName;
    }

    public VertxConfigurer setVertxAddrEnvName(String vertxAddrEnvName) {
        this.vertxAddrEnvName = vertxAddrEnvName;
        return this;
    }

    public String getVertxPublicPortEnvName() {
        return vertxPublicPortEnvName;
    }

    public VertxConfigurer setVertxPublicPortEnvName(String vertxPublicPortEnvName) {
        this.vertxPublicPortEnvName = vertxPublicPortEnvName;
        return this;
    }

    public String getVertxPortEnvName() {
        return vertxPortEnvName;
    }

    public VertxConfigurer setVertxPortEnvName(String vertxPortEnvName) {
        this.vertxPortEnvName = vertxPortEnvName;
        return this;
    }

    public boolean isHazelcastAutoconfig() {
        return hazelcastAutoconfig;
    }

    public VertxConfigurer setHazelcastAutoconfig(boolean hazelcastAutoconfig) {
        this.hazelcastAutoconfig = hazelcastAutoconfig;
        return this;
    }

    public String getHazelPortEnvName() {
        return hazelPortEnvName;
    }

    public VertxConfigurer setHazelPortEnvName(String hazelPortEnvName) {
        this.hazelPortEnvName = hazelPortEnvName;
        return this;
    }

    public VertxConfigurer setHazelPortAutoincrement(boolean hazelPortAutoincrement) {
        this.hazelPortAutoincrement = hazelPortAutoincrement;
        return this;
    }

    public VertxConfigurer setVertxAutoConfig(boolean vertxAutoConfig) {
        this.vertxAutoConfig = vertxAutoConfig;
        return this;
    }

    public VertxConfigurer setVertxMetricsJmx(boolean vertxMetricsJmx) {
        this.vertxMetricsJmx = vertxMetricsJmx;
        return this;
    }

    public enum Join {
        TCP_IP,
        MULTICAST;
    }
}
