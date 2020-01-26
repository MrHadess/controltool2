package com.mh.controltool2.exceptions.invoke;

public class HandlerThrowException extends RuntimeException {

    public HandlerThrowException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerThrowException(Throwable cause) {
        super(cause);
    }

}
