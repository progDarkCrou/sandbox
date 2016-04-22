package com.avorona.db;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * Created by avorona on 20.04.16.
 */
public class MongoCommandCodec implements MessageCodec<MongoCommand, MongoCommand> {

    @Override
    public void encodeToWire(Buffer buffer, MongoCommand mongoCommand) {
    }

    @Override
    public MongoCommand decodeFromWire(int pos, Buffer buffer) {
        return null;
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
