package com.avorona.db;

import com.avorona.MongoAccessorVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by com.avorona on 21.04.16.
 */
public class MongoCommandHandler implements Handler<Message<MongoCommand>> {

    private MongoClient mongoClient;

    public MongoCommandHandler(MongoClient client) {
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
        } else {
            event.fail(400, "DB request is NULL");
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
