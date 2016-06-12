package com.avorona.vertx.vo;

import io.vertx.core.json.JsonObject;

import java.util.Objects;

/**
 * Created by com.avorona on 21.04.16.
 */
public class ErrorRespVO {
    private JsonObject json = new JsonObject();

    public ErrorRespVO(String message) {
        json.put("error", message);
    }

    public ErrorRespVO(Object message) {
        this(Objects.toString(message));
    }

    @Override
    public String toString() {
        return json.toString();
    }
}
