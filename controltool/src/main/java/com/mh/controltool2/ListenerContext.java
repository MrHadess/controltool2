package com.mh.controltool2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ListenerContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        System.out.println("监听初始化");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        System.out.println("监听被销毁");
    }
}
