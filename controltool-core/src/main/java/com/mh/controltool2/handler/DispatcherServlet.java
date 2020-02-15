package com.mh.controltool2.handler;


import com.mh.controltool2.ApplicationContext;
import com.mh.controltool2.Config;
import com.mh.controltool2.exceptions.invoke.BeanInstantiationException;
import com.mh.controltool2.exceptions.invoke.HandlerThrowException;
import com.mh.controltool2.exceptions.invoke.ParamDataIsEmptyException;
import com.mh.controltool2.exceptions.invoke.UnsupportedSerializeObjectException;
import com.mh.controltool2.exceptions.serialize.JsonHandlerException;
import com.mh.controltool2.handler.message.ExceptionHandler;
import com.mh.controltool2.handler.message.HttpMessageRewrite;
import com.mh.controltool2.handler.pojo.RequestMatchInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private ApplicationContext applicationContext;
    private HttpMessageRewrite httpMessageRewrite;

    private RequestInterceptorHandler requestInterceptorHandler;
    private RequestMappingHandler requestMappingHandler;
    private ExceptionHandler exceptionHandler;

    // init assembly
    public void init(Config config, ApplicationContext applicationContext) throws BeanInstantiationException {
        this.applicationContext = applicationContext;
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
            Object handlerReturnObject = startHandlerLine(request,response);
            if (handlerReturnObject != null) {
                httpMessageRewrite.responseRewriteMessage(response, handlerReturnObject);
            }
        } finally {
            RequestContextHolder.remove();
        }

    }

    private Object startHandlerLine(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestMatchInfo requestMatchInfo = requestMappingHandler.requestMatchMethodInvokeInfo();

        if (requestMatchInfo == null || requestMatchInfo.getMethodInvokeInfo() == null) {
            response.setStatus(404);
            httpMessageRewrite.responseRewriteMessage(response,"404 The origin server did not find a current representation for the target resource (ControlTool)");
            return null;
        }

        // try handler interceptor
        try {
            requestInterceptorHandler.cleanInterceptorStack();
            boolean interceptorPreHandlerState = requestInterceptorHandler.request(requestMatchInfo.getMethodInvokeInfo().getTargetMethod());
            if (!interceptorPreHandlerState) {
                return null;// cut next handler
            }
        } catch (HandlerThrowException e) {
            logger.error("Interceptor handler fail",e.getCause());
            return exceptionHandler.resolveException(request,response,e);
        }

        Object reqReturnObject = null;
        try {
            reqReturnObject = requestMappingHandler.request(requestMatchInfo);
        } catch (IllegalAccessException e) {
            logger.error("Invoke object access exception",e);
//            rewriteExceptionToClient(response,"Method access fail",e);
            return exceptionHandler.resolveException(request,response,e);
        } catch (JsonHandlerException e) {
            return exceptionHandler.resolveException(request, response, new HandlerThrowException("Json deserialize from request fail", e));
        } catch (UnsupportedSerializeObjectException | ParamDataIsEmptyException | NumberFormatException e) {
            logger.error("Please change logic code",e);
            return exceptionHandler.resolveException(request, response, new HandlerThrowException("Request handler fail", e));
        } catch (InvocationTargetException e) {
            // use to 'handler interceptor'
            logger.error("Invoke throw exception",e.getCause());
            // use interceptor handler exception
            try {
                requestInterceptorHandler.requestHandlerAfter(requestMatchInfo.getMethodInvokeInfo().getTargetMethod(), (Exception) e.getCause());
            } catch (HandlerThrowException requestInterceptorHandlerEx) {
                e.getCause().addSuppressed(requestInterceptorHandlerEx.getCause());
            }
            return exceptionHandler.resolveException(request,response,new HandlerThrowException("Control throw exception",e.getCause()));
        } catch (Exception e) { // Unhandled exception use that last catch
            logger.error("Unhandled exception exception",e);
            return exceptionHandler.resolveException(request, response, e);
        }

        try {
            requestInterceptorHandler.requestHandlerAfter(requestMatchInfo.getMethodInvokeInfo().getTargetMethod(),null);
        } catch (HandlerThrowException e) {
            // use to 'handler interceptor'
            logger.error("Unhandled exception exception",e.getCause());
            return exceptionHandler.resolveException(request,response,new HandlerThrowException("Control throw exception",e));
        }

        return reqReturnObject;
    }




}
