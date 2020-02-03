package com.mh.controltool2.handler.message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExceptionHandler {

    Object resolveException(HttpServletRequest request, HttpServletResponse response, Exception exception);

}
