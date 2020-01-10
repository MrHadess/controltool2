package com.mh.controltool2.dto;

import java.io.PrintWriter;
import java.io.StringWriter;

public class HandlerErrorInfo {

    private String message;
    private String[] throwableStack;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getThrowableStack() {
        return throwableStack;
    }

    public void setThrowableStack(Throwable throwableStack) {
        this.throwableStack = getTrace(throwableStack);
    }

    private static String[] getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString().split("\r\n");
    }

    @Override
    public String toString() {
        return "HandlerErrorInfo{" +
                "message='" + message + '\'' +
                ", throwableStack=" + throwableStack +
                '}';
    }

}
