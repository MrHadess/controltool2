package com.mh.controltool2.handler;


import com.mh.controltool2.ApplicationContext;
import com.mh.controltool2.Config;
import com.mh.controltool2.exceptions.invoke.BeanInstantiationException;
import com.mh.controltool2.exceptions.invoke.HandlerThrowException;
import com.mh.controltool2.handler.message.ExceptionHandler;
import com.mh.controltool2.handler.message.HttpMessageRewrite;
import com.mh.controltool2.handler.pojo.RequestMatchInfo;
import com.mh.controltool2.serialize.json.DataObjectSerialize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


/*
* must be use 'ThreadLocal' bean to first,global use to before
*
*
*
* */
public class DispatcherServlet {

    private ApplicationContext applicationContext;
    private DataObjectSerialize dataObjectSerialize;
    private HttpMessageRewrite httpMessageRewrite;

    private RequestInterceptorHandler requestInterceptorHandler;
    private RequestMappingHandler requestMappingHandler;
    private ExceptionHandler exceptionHandler;

    // init assembly
    public void init(Config config, ApplicationContext applicationContext) throws BeanInstantiationException {
        this.applicationContext = applicationContext;
        dataObjectSerialize = applicationContext.getBean(DataObjectSerialize.class);
        httpMessageRewrite = applicationContext.getBean(HttpMessageRewrite.class);
        exceptionHandler = applicationContext.getBean(ExceptionHandler.class);
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
            response.setStatus(404);
            httpMessageRewrite.responseRewriteMessage(response,"404 The origin server did not find a current representation for the target resource (ControlTool)");
            return;
        }

        // try handler interceptor
        try {
            requestInterceptorHandler.cleanInterceptorStack();
            boolean interceptorPreHandlerState = requestInterceptorHandler.request(requestMatchInfo.getMethodInvokeInfo().getTargetMethod());
            if (!interceptorPreHandlerState) return;// cut next handler
        } catch (HandlerThrowException e) {
            e.getCause().printStackTrace();
            Object returnMessage = exceptionHandler.resolveException(request,response,e);
            if (returnMessage != null) {
                httpMessageRewrite.responseRewriteMessage(response,returnMessage);
            }
            return;
        }

        Object reqReturnObject = null;
        try {
            reqReturnObject = requestMappingHandler.request(requestMatchInfo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
//            rewriteExceptionToClient(response,"Method access fail",e);
            Object returnMessage = exceptionHandler.resolveException(request,response,e);
            if (returnMessage != null) {
                httpMessageRewrite.responseRewriteMessage(response,returnMessage);
            }
            return;
        } catch (InvocationTargetException e) {
            // use to 'handler interceptor'
            e.getCause().printStackTrace();
            // use interceptor handler exception
            try {
                requestInterceptorHandler.requestHandlerAfter(requestMatchInfo.getMethodInvokeInfo().getTargetMethod(), (Exception) e.getCause());
            } catch (HandlerThrowException requestInterceptorHandlerEx) {
                e.getCause().addSuppressed(requestInterceptorHandlerEx.getCause());
            }
            Object returnMessage = exceptionHandler.resolveException(request,response,new HandlerThrowException("Control throw exception",e.getCause()));
            if (returnMessage != null) {
                httpMessageRewrite.responseRewriteMessage(response,returnMessage);
            }
            return;
        }

        try {
            requestInterceptorHandler.requestHandlerAfter(requestMatchInfo.getMethodInvokeInfo().getTargetMethod(),null);
        } catch (HandlerThrowException e) {
            // use to 'handler interceptor'
            e.getCause().printStackTrace();
            Object returnMessage = exceptionHandler.resolveException(request,response,new HandlerThrowException("Control throw exception",e));
            if (returnMessage != null) {
                httpMessageRewrite.responseRewriteMessage(response,returnMessage);
            }
            return;
        }


        if (reqReturnObject != null) {
            httpMessageRewrite.responseRewriteMessage(response, dataObjectSerialize.toJson(reqReturnObject));
        }
    }




}
