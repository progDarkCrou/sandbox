package com.avorona.db;

import com.sun.istack.internal.NotNull;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by avorona on 20.04.16.
 */
public class MongoAccessorVerticle extends AbstractVerticle {

    public static final String DB_MODEL = "calculation";
    public static final String EVENT_NAME = "mongo.calculation";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JsonObject mongoConfig = new JsonObject();

    public MongoAccessorVerticle() {
        this("127.0.0.1", null);
    }

    public MongoAccessorVerticle(@NotNull String host, Integer port) {
        if (port != null) {
            mongoConfig.put("port", port);
        }
        mongoConfig.put("host", host);
    }

    public MongoAccessorVerticle(String host) {
        this(host, null);
    }

    @Override
    public void start() throws Exception {
        MongoClient mongoClient = MongoClient.createNonShared(vertx, mongoConfig);
        vertx.eventBus().consumer(EVENT_NAME, new MongoCommandProcessor(mongoClient));
        logger.info("Mongo accessor started");
    }

    @Override
    public void stop() throws Exception {
        logger.info("Mongo accessor stoped");
    }

}