package com.avorona;

import com.avorona.db.MongoCommandHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by com.avorona on 20.04.16.
 */
public class MongoAccessorVerticle extends AbstractVerticle {

    public static final String DB_MODEL = "calculation";
    public static final String MONGO_ADDRESS = "vertx.demo.mongo-calculate";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Integer port;
    private String host;

    private boolean deployed;

    public MongoAccessorVerticle() {
    }

    public MongoAccessorVerticle(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public MongoAccessorVerticle(String host) {
        this.host = host;
    }

    @Override
    public void start() throws Exception {
        MongoClient mongoClient = MongoClient.createNonShared(vertx, getMongoConfig());
        vertx.eventBus().consumer(MONGO_ADDRESS, new MongoCommandHandler(mongoClient));
        deployed = true;
        logger.info("Mongo Accessor started");
    }

    @Override
    public void stop() throws Exception {
        logger.info("Mongo accessor stopped");
    }

    private JsonObject getMongoConfig() {
        JsonObject mongoConfig = new JsonObject();
        if (port != null) {
            mongoConfig.put("port", port);
        }
        if (host != null) {
            mongoConfig.put("host", host);
        }
        return mongoConfig;
    }

    public void setHost(String host) {
        if (deployed) {
            throw new IllegalStateException("Verticle is deployed, cannot set new host");
        }
        this.host = host;
    }

    public void setPort(int port) {
        if (deployed) {
            throw new IllegalStateException("Verticle is deployed, cannot set new port");
        }
        this.port = port;
    }
}