package com.mh.controltool2.exceptions.invoke;

public class JsonParseException extends RuntimeException {

    public JsonParseException(Throwable throwable) {
        super("Invoke json package exception",throwable);
    }
}
