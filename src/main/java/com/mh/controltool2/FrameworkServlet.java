package com.mh.controltool2;

import com.mh.controltool2.context.FullApplicationContext;
import com.mh.controltool2.handler.DispatcherServlet;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrameworkServlet extends HttpServlet {

    private Config config;
    private ApplicationContext applicationContext = new FullApplicationContext();
    private DispatcherServlet dispatcherServlet = new DispatcherServlet();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatcherServlet.handlerRequest(req,resp);
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("Control Tool version:2.0.0Bate");
//        System.out.println(logo);
        LogOut.i("FrameworkServlet initiation");

        config = Config.create(servletConfig);
        dispatcherServlet.init(config,applicationContext);

    }

    @Override
    public void destroy() {

    }

//    private static final String logo =
//                            "   _____            _             _ _______          _ \n" +
//                            "  / ____|          | |           | |__   __|        | |\n" +
//                            " | |     ___  _ __ | |_ _ __ ___ | |  | | ___   ___ | |\n" +
//                            " | |    / _ \\| '_ \\| __| '__/ _ \\| |  | |/ _ \\ / _ \\| |\n" +
//                            " | |___| (_) | | | | |_| | | (_) | |  | | (_) | (_) | |\n" +
//                            "  \\_____\\___/|_| |_|\\__|_|  \\___/|_|  |_|\\___/ \\___/|_|\n" +
//                            "                                                       \n" +
//                            "                                                       ";
}
