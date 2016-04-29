package com.avorona.db;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * Created by com.avorona on 20.04.16.
 */
public class MongoCommandCodec implements MessageCodec<MongoCommand, MongoCommand> {

    @Override
    public void encodeToWire(Buffer buffer, MongoCommand mongoCommand) {
        buffer.appendInt(mongoCommand.getCommand().ordinal())
                .appendString(mongoCommand.getQuery().toString());
    }

    @Override
    public MongoCommand decodeFromWire(int pos, Buffer buffer) {
        JsonObject queryObj = buffer.slice(pos + 4, buffer.length()).toJsonObject();

        return new MongoCommand(MongoCommand.Command.values()[buffer.getInt(pos)], queryObj);
    }

    @Override
    public MongoCommand transform(MongoCommand mongoCommand) {
        return mongoCommand;
    }

    @Override
    public String name() {
        return "mongoCommand.codec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
