package com.mh.controltool2.handler;


import com.mh.controltool2.ApplicationContext;
import com.mh.controltool2.Config;
import com.mh.controltool2.dto.HandlerErrorInfo;
import com.mh.controltool2.exceptions.invoke.BeanInstantiationException;
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

    private RequestMappingHandler requestMappingHandler;

    // init assembly
    public void init(Config config, ApplicationContext applicationContext) throws BeanInstantiationException {
        this.applicationContext = applicationContext;
        dataObjectSerialize = applicationContext.tryGetBean(DefaultDataObjectSerialize.class);
        // bean group (Unrealized)
        // request mapping
        requestMappingHandler = new RequestMappingHandler(
                applicationContext,
                config.getHandlerControl().getUrlAbsolutelyMap(),
                config.getHandlerControl().getUrlFuzzyMap()
        );
        // handler interceptor (Unrealized)
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

        Object reqReturnObject = null;
        try {
            reqReturnObject = requestMappingHandler.request(requestMatchInfo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            response.setContentType(CONTENT_TYPE_APPLICATION_JSON_UTF_8);
            HandlerErrorInfo handlerErrorInfo = new HandlerErrorInfo();
            handlerErrorInfo.setMessage("Method access fail");
            handlerErrorInfo.setThrowableStack(e);
            rewriteToClient(response, dataObjectSerialize.toJson(handlerErrorInfo));
            return;
        } catch (InvocationTargetException e) {
            // use to 'handler interceptor'
            e.getCause().printStackTrace();
            response.setContentType(CONTENT_TYPE_APPLICATION_JSON_UTF_8);
            HandlerErrorInfo handlerErrorInfo = new HandlerErrorInfo();
            handlerErrorInfo.setMessage("Control throw exception");
            handlerErrorInfo.setThrowableStack(e.getCause());
            rewriteToClient(response, dataObjectSerialize.toJson(handlerErrorInfo));
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




}
