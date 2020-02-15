package com.mh.controltool2.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestContextHolder {

    private static ThreadLocal<HttpServletRequest> httpServletRequestThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> httpServletResponseThreadLocal = new ThreadLocal<>();

    protected static void update(HttpServletRequest request,HttpServletResponse response) {
        httpServletRequestThreadLocal.set(request);
        httpServletResponseThreadLocal.set(response);
    }

    protected static void remove() {
        httpServletRequestThreadLocal.remove();
        httpServletResponseThreadLocal.remove();
    }

    public static HttpServletRequest getHttpServletRequest() {
        return httpServletRequestThreadLocal.get();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return httpServletResponseThreadLocal.get();
    }

}
