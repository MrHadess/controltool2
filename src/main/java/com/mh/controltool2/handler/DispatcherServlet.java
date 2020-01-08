package com.mh.controltool2.handler;


import com.mh.controltool2.Config;
import com.mh.controltool2.handler.pojo.RequestMatchInfo;
import com.mh.controltool2.method.MethodInvokeInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*
* must be use 'ThreadLocal' bean to first,global use to before
*
*
*
* */
public class DispatcherServlet {

    private RequestMappingHandler requestMappingHandler;

    // init assembly
    public void init(Config config) {
        // bean group (Unrealized)
        // request mapping
        requestMappingHandler = new RequestMappingHandler(
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

        try {
            requestMappingHandler.request(requestMatchInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestContextHolder.remove();
    }




}
