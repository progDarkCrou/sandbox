package com.avorona.calculate;

import java.io.Serializable;

/**
 * Created by avorona on 20.04.16.
 */
public class CalculateCommand implements Serializable {

    private Long from;
    private Long to;

    public CalculateCommand(Long from, Long to) {
        this.from = from;
        this.to = to;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "CalculateCommand{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
