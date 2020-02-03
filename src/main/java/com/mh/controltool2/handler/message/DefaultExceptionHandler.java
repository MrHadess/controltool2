package com.mh.controltool2.handler.message;

import com.mh.controltool2.dto.HandlerErrorInfo;
import com.mh.controltool2.exceptions.invoke.HandlerThrowException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public Object resolveException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        response.setStatus(500);
        HandlerErrorInfo handlerErrorInfo = new HandlerErrorInfo();
        handlerErrorInfo.setMessage(exception.getMessage());
        if (HandlerThrowException.class.equals(exception.getClass())) {
            handlerErrorInfo.setThrowableStack(exception.getCause());
        } else {
            handlerErrorInfo.setThrowableStack(exception);
        }

        return handlerErrorInfo;
    }

}
