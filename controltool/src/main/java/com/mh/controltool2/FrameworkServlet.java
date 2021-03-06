package com.mh.controltool2;

import com.mh.controltool2.context.AssemblyBeanLoader;
import com.mh.controltool2.context.BeanUtil;
import com.mh.controltool2.context.FullApplicationContext;
import com.mh.controltool2.handler.DispatcherServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrameworkServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger("FrameworkServlet");

    private Config config;
    private ApplicationContext applicationContext = new FullApplicationContext();
    private DispatcherServlet dispatcherServlet = new DispatcherServlet();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatcherServlet.handlerRequest(req,resp);
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        logger.info(LOGO);
        logger.info("Control Tool version:2.1.2");
        logger.info("FrameworkServlet initiation");

        config = Config.create(servletConfig);
        // load default assembly
        AssemblyBeanLoader.loadConfigToContext(applicationContext,config);
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

    private static final String LOGO =
                            "\n" +
                            "   _____            _             _ _______          _ \n" +
                            "  / ____|          | |           | |__   __|        | |\n" +
                            " | |     ___  _ __ | |_ _ __ ___ | |  | | ___   ___ | |\n" +
                            " | |    / _ \\| '_ \\| __| '__/ _ \\| |  | |/ _ \\ / _ \\| |\n" +
                            " | |___| (_) | | | | |_| | | (_) | |  | | (_) | (_) | |\n" +
                            "  \\_____\\___/|_| |_|\\__|_|  \\___/|_|  |_|\\___/ \\___/|_|\n" +
                            "                                                       \n" +
                            "                                                       ";
}
