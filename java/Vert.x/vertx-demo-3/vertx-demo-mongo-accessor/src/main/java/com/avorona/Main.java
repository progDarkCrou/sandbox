package com.avorona;

import com.avorona.db.MongoCommand;
import com.avorona.db.MongoCommandCodec;
import com.avorona.helper.NoSuchInterfaceException;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

import java.net.SocketException;

/**
 * Created by com.avorona on 21.04.16.
 */
public class Main {
    public static void main(String[] args) throws SocketException, NoSuchInterfaceException {
        VertxConfigurer configurer = VertxConfigurer.clustered();
        configurer.setHazelcastAutoconfig(true)
                .setHazelPortAutoincrement(true)
                .setVertxMetricsJmx(true)
                .setVertxAutoConfig(true);

        ApplicationContext context = new ApplicationContext()
                .addDeployment(new MongoAccessorVerticle(System.getenv("MONGO_HOST")))
                .addPreDeploy(v -> v.eventBus().registerDefaultCodec(MongoCommand.class, new MongoCommandCodec()));

        //Metrics default configuration
        DropwizardMetricsOptions metricsOptions = new DropwizardMetricsOptions();
        metricsOptions.setJmxEnabled(true)
                .setEnabled(true);

        VertxOptions vertxOptions = configurer.build()
                .setMetricsOptions(metricsOptions);

        Vertx.clusteredVertx(vertxOptions, context);
    }
}
