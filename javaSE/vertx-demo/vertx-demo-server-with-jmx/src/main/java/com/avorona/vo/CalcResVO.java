package com.avorona.vo;

import io.vertx.core.json.JsonObject;

/**
 * Created by com.avorona on 21.04.16.
 */
public class CalcResVO {
    private JsonObject json = new JsonObject();

    public CalcResVO(Long from, Long to, Long res) {
        json.put("from", from);
        json.put("to", to);
        json.put("res", res);
    }

    @Override
    public String toString() {
        return json.toString();
    }
}
