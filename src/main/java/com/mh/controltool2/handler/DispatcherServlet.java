package com.mh.controltool2.handler;


import com.mh.controltool2.ApplicationContext;
import com.mh.controltool2.Config;
import com.mh.controltool2.dto.HandlerErrorInfo;
import com.mh.controltool2.exceptions.invoke.BeanInstantiationException;
import com.mh.controltool2.exceptions.invoke.HandlerThrowException;
import com.mh.controltool2.handler.pojo.RequestMatchInfo;
import com.mh.controltool2.serialize.json.DataObjectSerialize;
import com.mh.controltool2.serialize.json.DefaultDataObjectSerialize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;


/*
* must be use 'ThreadLocal' bean to first,global use to before
*
*
*
* */
public class DispatcherServlet {

    private static final String CONTENT_TYPE_APPLICATION_JSON_UTF_8 = "application/json;utf-8";

    private ApplicationContext applicationContext;
    private DataObjectSerialize dataObjectSerialize;

    private RequestInterceptorHandler requestInterceptorHandler;
    private RequestMappingHandler requestMappingHandler;

    // init assembly
    public void init(Config config, ApplicationContext applicationContext) throws BeanInstantiationException {
        this.applicationContext = applicationContext;
        dataObjectSerialize = applicationContext.tryGetBean(DefaultDataObjectSerialize.class);
        // bean group (Unrealized)
        // handler interceptor (Unrealized)
        requestInterceptorHandler = new RequestInterceptorHandler(config.getHandlerConfig().getMappedInterceptorList());
        // request mapping
        requestMappingHandler = new RequestMappingHandler(
                applicationContext,
                config.getHandlerControl().getUrlAbsolutelyMap(),
                config.getHandlerControl().getUrlFuzzyMap()
        );
    }

    public void handlerRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        RequestContextHolder.update(request,response);

        try {
            startHandlerLine(request,response);
        } finally {
            RequestContextHolder.remove();
        }

    }

    private void startHandlerLine(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestMatchInfo requestMatchInfo = requestMappingHandler.requestMatchMethodInvokeInfo();

        if (requestMatchInfo == null || requestMatchInfo.getMethodInvokeInfo() == null) {
            response.getWriter().print("404 The origin server did not find a current representation for the target resource (ControlTool)");
            response.setStatus(404);
            return;
        }

        // try handler interceptor
        try {
            requestInterceptorHandler.cleanInterceptorStack();
            boolean interceptorPreHandlerState = requestInterceptorHandler.request(requestMatchInfo.getMethodInvokeInfo().getMethodName());
            if (!interceptorPreHandlerState) return;// cut next handler
        } catch (HandlerThrowException e) {
            e.getCause().printStackTrace();
            rewriteExceptionToClient(response,"Interceptor handler throw exception",e.getCause());
            return;
        }

        Object reqReturnObject = null;
        try {
            reqReturnObject = requestMappingHandler.request(requestMatchInfo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            rewriteExceptionToClient(response,"Method access fail",e);
            return;
        } catch (InvocationTargetException e) {
            // use to 'handler interceptor'
            e.getCause().printStackTrace();
            // use interceptor handler exception
            try {
                requestInterceptorHandler.requestHandlerAfter(requestMatchInfo.getMethodInvokeInfo().getMethodName(), (Exception) e.getCause());
            } catch (HandlerThrowException requestInterceptorHandlerEx) {
                e.getCause().addSuppressed(requestInterceptorHandlerEx.getCause());
            }
            rewriteExceptionToClient(response,"Control throw exception",e.getCause());
            return;
        }

        try {
            requestInterceptorHandler.requestHandlerAfter(requestMatchInfo.getMethodInvokeInfo().getMethodName(),null);
        } catch (HandlerThrowException e) {
            // use to 'handler interceptor'
            e.getCause().printStackTrace();
            rewriteExceptionToClient(response,"Interceptor throw exception",e.getCause());
            return;
        }


        if (reqReturnObject != null) {
            rewriteToClient(response, dataObjectSerialize.toJson(reqReturnObject));
        }
    }

    private void rewriteToClient(HttpServletResponse response,String data) throws IOException {
        PrintWriter respWriter = response.getWriter();
        respWriter.print(data);
        respWriter.flush();
    }

    private void rewriteExceptionToClient(HttpServletResponse response,String msg,Throwable e) throws IOException {
        response.setContentType(CONTENT_TYPE_APPLICATION_JSON_UTF_8);
        HandlerErrorInfo handlerErrorInfo = new HandlerErrorInfo();
        handlerErrorInfo.setMessage(msg);
        handlerErrorInfo.setThrowableStack(e);
        rewriteToClient(response, dataObjectSerialize.toJson(handlerErrorInfo));
    }



}
