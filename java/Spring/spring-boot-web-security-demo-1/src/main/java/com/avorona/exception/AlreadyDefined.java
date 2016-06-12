package com.avorona.exception;

/**
 * Created by avorona on 01.06.16.
 */
public class AlreadyDefined extends Exception {
    public AlreadyDefined() {
    }

    public AlreadyDefined(String message) {
        super(message);
    }
}
