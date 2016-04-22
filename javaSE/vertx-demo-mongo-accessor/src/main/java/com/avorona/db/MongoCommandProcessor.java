package com.avorona.db;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by avorona on 21.04.16.
 */
class MongoCommandProcessor implements Handler<Message<MongoCommand>> {

    private MongoClient mongoClient;

    public MongoCommandProcessor(MongoClient client) {
        mongoClient = client;
    }

    @Override
    public void handle(Message<MongoCommand> event) {
        MongoCommand body = event.body();

        if (body != null) {
            switch (body.getCommand()) {
                case GET_ONE:
                    get(event);
                    break;
                case PUT:
                    put(event);
                    break;
                case REMOVE:
                    remove(event);
                    break;
                case CREATE:
                    create(event);
                    break;
            }
        }
    }

    private void get(Message<MongoCommand> event) {
        mongoClient.findOne(MongoAccessorVerticle.DB_MODEL, event.body().getQuery(), new JsonObject(), event1 -> {
            if (event1.succeeded()) {
                event.reply(event1.result());
            } else {
                event.fail(1, "Error");
            }
        });
    }

    private void put(Message<MongoCommand> event) {
        mongoClient.insert(MongoAccessorVerticle.DB_MODEL, event.body().getQuery(), e -> {
            if (e.succeeded()) {
                event.reply(e.result());
            } else {
                event.fail(1, "Not found");
            }
        });
    }

    private void remove(Message<MongoCommand> event) {
        mongoClient.remove(MongoAccessorVerticle.DB_MODEL, event.body().getQuery(), e -> {
            if (e.succeeded()) {
                event.reply(e.result());
            } else {
                event.fail(1, "Not found");
            }
        });
    }

    private void create(Message<MongoCommand> event) {
        mongoClient.save(MongoAccessorVerticle.DB_MODEL, event.body().getQuery(), e -> {
            if (e.succeeded()) {
                event.reply(e.result());
            } else {
                event.fail(1, "Error");
            }
        });
    }
}
