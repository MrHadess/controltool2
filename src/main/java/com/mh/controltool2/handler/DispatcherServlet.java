package com.mh.controltool2.handler;


import com.mh.controltool2.ApplicationContext;
import com.mh.controltool2.Config;
import com.mh.controltool2.handler.pojo.RequestMatchInfo;
import com.mh.controltool2.serialize.json.LoadJsonToClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/*
* must be use 'ThreadLocal' bean to first,global use to before
*
*
*
* */
public class DispatcherServlet {

    private ApplicationContext applicationContext;

    private static final LoadJsonToClass LOAD_JSON_TO_CLASS = new LoadJsonToClass();

    private RequestMappingHandler requestMappingHandler;

    // init assembly
    public void init(Config config, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
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
        RequestMatchInfo requestMatchInfo = requestMappingHandler.requestMatchMethodInvokeInfo();

        if (requestMatchInfo == null) {
            response.getWriter().print("404 The origin server did not find a current representation for the target resource (ControlTool)");
            response.setStatus(404);
            return;
        }

        Object reqReturnObject = null;
        try {
            reqReturnObject = requestMappingHandler.request(requestMatchInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        rewriteToClient(response, LOAD_JSON_TO_CLASS.toJson(reqReturnObject));

        RequestContextHolder.remove();
    }

    private void rewriteToClient(HttpServletResponse response,String data) throws IOException {
        PrintWriter respWriter = response.getWriter();
        respWriter.print(data);
        respWriter.flush();
    }




}
