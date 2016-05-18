package com.avorona;

import com.avorona.calculate.CalculateCommand;
import com.avorona.calculate.CalculateCommandCodec;
import com.avorona.calculate.CalculatorVerticle;
import com.avorona.db.MongoCommand;
import com.avorona.db.MongoCommandCodec;
import com.avorona.helper.NoSuchInterfaceException;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.net.SocketException;

/**
 * Created by com.avorona on 20.04.16.
 */
public class Main {
    public static void main(String[] args) throws SocketException, NoSuchInterfaceException {
        VertxConfigurer configurer = VertxConfigurer.clustered();
        configurer.setHazelcastAutoconfig(true)
                .setHazelPortAutoincrement(true)
                .setVertxMetricsJmx(true)
                .setVertxAutoConfig(true);

        VertxOptions vertxOptions = configurer.build();

        ApplicationContext context = new ApplicationContext()
                .addPreDeploy(v -> {
                    v.eventBus().registerDefaultCodec(CalculateCommand.class, new CalculateCommandCodec());
                    v.eventBus().registerDefaultCodec(MongoCommand.class, new MongoCommandCodec());
                })
                .addDeployment(new CalculatorVerticle());

        Vertx.clusteredVertx(vertxOptions, context);
    }
}
