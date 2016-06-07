package com.avorona;

import com.avorona.calculate.CalculateCommand;
import com.avorona.calculate.CalculateCommandCodec;
import com.avorona.calculate.CalculatorVerticle;
import com.avorona.db.MongoCommand;
import com.avorona.db.MongoCommandCodec;
import com.avorona.helper.NoSuchInterfaceException;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.hawkular.VertxHawkularOptions;

import java.net.SocketException;

/**
 * Created by com.avorona on 20.04.16.
 */
public class Main {
    public static void main(String[] args) throws SocketException, NoSuchInterfaceException {

        VertxConfigurer configurer = VertxConfigurer.clustered();
        configurer.setHazelcastAutoconfig(true)
                .setHazelPortAutoincrement(true)
                .setVertxAutoConfig(true);

        //Hawkular metrics options configuration
        VertxHawkularOptions metricsOptions = new VertxHawkularOptions()
                .setTenant(System.getenv("VERTX_HAWKULAR_TENANT"))
                .setHost(System.getenv("VERTX_HAWKULAR_HOST"))
                .setPort(Integer.parseInt(System.getenv("VERTX_HAWKULAR_PORT")))
                .setEnabled(true);

        VertxOptions vertxOptions = configurer.build()
                .setMetricsOptions(metricsOptions);

        ApplicationContext context = new ApplicationContext()
                .addPreDeploy(v -> {
                    v.eventBus().registerDefaultCodec(CalculateCommand.class, new CalculateCommandCodec());
                    v.eventBus().registerDefaultCodec(MongoCommand.class, new MongoCommandCodec());
                })
                .addDeployment(new CalculatorVerticle());

        Vertx.clusteredVertx(vertxOptions, context);
    }
}
