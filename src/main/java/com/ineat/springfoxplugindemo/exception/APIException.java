package com.ineat.springfoxplugindemo.exception;

import com.ineat.springfoxplugindemo.config.swagger.ExceptionErrorCode;

public abstract class APIException extends Exception {
    APIException(String msg) {
        super(msg);
    }

    /**
     * Return a unique error code that identifies this error.
     */
    public String getCode() {
        return ExceptionErrorCode.of(getClass()).name();
    }
}
