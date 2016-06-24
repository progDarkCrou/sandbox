package com.avorona.exception;

/**
 * Created by avorona on 02.06.16.
 */
public class UserAlreadyDefined extends AlreadyDefined {
    public UserAlreadyDefined(String username) {
        super(String.format("User with username \"%s\" already defined", username));
    }
}
