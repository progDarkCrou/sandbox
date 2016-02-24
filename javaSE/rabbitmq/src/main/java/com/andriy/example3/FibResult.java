package com.andriy.example3;

import java.math.BigInteger;

/**
 * Created by crou on 07.11.15.
 */
public class FibResult {

    private BigInteger result;
    private int id;

    public FibResult(BigInteger result, int id) {
        this.result = result;
        this.id = id;
    }

    public FibResult(String result, int id) {
        this(new BigInteger(result), id);
    }

    public BigInteger getResult() {
        return result;
    }

    public void setResult(BigInteger result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FibResult -> id: " + id + ", result: " + result;
    }
}
