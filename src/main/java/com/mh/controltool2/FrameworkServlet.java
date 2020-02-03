package com.mh.controltool2;

import com.mh.controltool2.context.BeanUtil;
import com.mh.controltool2.context.ConfigReader;
import com.mh.controltool2.context.ConfigReaderToFramework;
import com.mh.controltool2.context.FullApplicationContext;
import com.mh.controltool2.handler.DispatcherServlet;
import com.mh.controltool2.handler.message.ExceptionHandler;
import com.mh.controltool2.handler.message.HttpMessageRewrite;
import com.mh.controltool2.serialize.json.DataObjectSerialize;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
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
        System.out.println("Control Tool version:2.0.0");
//        System.out.println(logo);
        LogOut.i("FrameworkServlet initiation");

        config = Config.create(servletConfig);
        // load default assembly
        applicationContext.addBean(DataObjectSerialize.class,config.getHandlerConfig().getDataObjectSerialize());
        applicationContext.addBean(HttpMessageRewrite.class,config.getHandlerConfig().getHttpMessageRewrite());
        // create config reader bean
        applicationContext.addBean(
                ConfigReader.class,
                new ConfigReaderToFramework(config.getBeanPropertiesFileName())
        );
        // load exception handler bean
        applicationContext.addBean(ExceptionHandler.class,config.getHandlerConfig().getExceptionHandler());
        // load bean from config
        BeanUtil.tryBeanMapToContext(applicationContext,config.getHandlerConfig().getBeanMap());
        // load full bean
        BeanUtil.tryLoadBeansToContext(applicationContext,config.getHandlerBean().getScanBeanMap());
        // init dispatch servlet
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
