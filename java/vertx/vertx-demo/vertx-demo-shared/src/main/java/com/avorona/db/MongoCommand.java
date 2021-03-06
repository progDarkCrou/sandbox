package com.avorona.db;

import io.vertx.core.json.JsonObject;

/**
 * Created by com.avorona on 20.04.16.
 */
public class MongoCommand {
    public enum Command {
        GET_ONE,
        PUT,
        REMOVE,
        CREATE
    }

    private Command command;
    private JsonObject query;

    public MongoCommand(Command command, JsonObject query) {
        this.command = command;
        this.query = query;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public JsonObject getQuery() {
        return query;
    }

    public void setQuery(JsonObject query) {
        this.query = query;
    }
}
