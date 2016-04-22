package com.avorona.calculate;

import java.io.Serializable;

/**
 * Created by avorona on 20.04.16.
 */
public class CalculateCommand implements Serializable {

    private Integer from;
    private Integer to;

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }
}
