package com.avorona.calculate;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.util.UUID;

/**
 * Created by com.avorona on 20.04.16.
 */
public class CalculateCommandCodec implements MessageCodec<CalculateCommand, CalculateCommand> {

    public static final String NAME = CalculateCommandCodec.class.getSimpleName();
    private static final byte id = ((byte) UUID.randomUUID().version());

    @Override
    public void encodeToWire(Buffer buffer, CalculateCommand calculateCommand) {
        buffer.appendLong(calculateCommand.getFrom())
                .appendLong(calculateCommand.getTo());
    }

    @Override
    public CalculateCommand decodeFromWire(int pos, Buffer buffer) {
        return new CalculateCommand(buffer.getLong(pos), buffer.getLong(pos + 8));
    }

    @Override
    public CalculateCommand transform(CalculateCommand calculateCommand) {
        return calculateCommand;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
