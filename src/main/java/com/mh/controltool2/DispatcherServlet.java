package com.mh.controltool2;

import com.mh.controltool2.invokemapping.UseToControl;
import com.mh.controltool2.scan.AnnotationScan;
import com.mh.controltool2.scan.InvokeMsg;
import com.mh.controltool2.scan.LocalParameterData;
import com.mh.controltool2.scan.PackageScan;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DispatcherServlet implements Servlet {

    private UseToControl useToControl;
    private String requestCharacterEncoding = "utf-8";


    private void logg(Object o){
        System.out.println(o);
    }


    ///Servlet
    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
//        logg("-------------------" + new Date().toString());
//        logg("ContextPatch:" + req.getContextPath());
//        logg("ServletPath:" + req.getServletPath());
//        logg("getRequestURI:" + req.getRequestURI());---
//        logg("getPathInfo:" + req.getPathInfo());---
//        logg("getMethod:" + req.getMethod());

        servletRequest.setCharacterEncoding(requestCharacterEncoding);//设置默认的解码方式 URL Encode & GetParam

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;



        if (useToControl == null) return;

        try {
            useToControl.run(req.getPathInfo(),req,resp);
        } catch (Error e) {
            //拦截所有意外状态
            e.printStackTrace();
            resp.getWriter().print("500 Server handler fall. (ControlTool)");
            resp.setStatus(500);
        }

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("Control Tool version:1.2.9");
//        System.out.println(logo);
        LogOut.i("DispatchServlet initiation");

//        Servlet获取当前Tomcat的Web目录
//        System.out.println(config.getServletContext().getRealPath("/"));

        String pkg = servletConfig.getInitParameter("ScanPkg");
        String reqCharacterEncoding = servletConfig.getInitParameter("RequestCharacterEncoding");
        if (reqCharacterEncoding != null && !reqCharacterEncoding.trim().isEmpty()) {
            requestCharacterEncoding = reqCharacterEncoding;
        }

        List<Class<?>> listClasss = PackageScan.nowScanPackage(pkg);

        //Check package
        if (listClasss.size() <= 0){
            LogOut.e("Scan package is empty");
            useToControl = new UseToControl(new HashMap<String, InvokeMsg>(),new HashMap<String, InvokeMsg>());
            return;
        }

        //Log out
        LocalParameterData localParameterData = new AnnotationScan().scanAnnotation(listClasss);

        useToControl = new UseToControl(localParameterData.getAbsolutelyMap(), localParameterData.getFuzzyMap());
    }

    @Override
    public void destroy() {

    }

    private static final String logo =
                            "   _____            _             _ _______          _ \n" +
                            "  / ____|          | |           | |__   __|        | |\n" +
                            " | |     ___  _ __ | |_ _ __ ___ | |  | | ___   ___ | |\n" +
                            " | |    / _ \\| '_ \\| __| '__/ _ \\| |  | |/ _ \\ / _ \\| |\n" +
                            " | |___| (_) | | | | |_| | | (_) | |  | | (_) | (_) | |\n" +
                            "  \\_____\\___/|_| |_|\\__|_|  \\___/|_|  |_|\\___/ \\___/|_|\n" +
                            "                                                       \n" +
                            "                                                       ";
}
