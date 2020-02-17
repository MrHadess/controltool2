package com.mh.controltool2.context;

import com.mh.controltool2.ApplicationContext;
import com.mh.controltool2.Config;
import com.mh.controltool2.handler.message.ExceptionHandler;
import com.mh.controltool2.handler.message.HttpMessageRewrite;
import com.mh.controltool2.serialize.json.DataObjectSerialize;

/*
* Load framework assembly
*
* */
public class AssemblyBeanLoader {

    public static void loadConfigToContext(ApplicationContext applicationContext, Config config) {
        // load data object serialize
        applicationContext.addBean(DataObjectSerialize.class,config.getHandlerConfig().getDataObjectSerialize());
        applicationContext.addBean(HttpMessageRewrite.class,config.getHandlerConfig().getHttpMessageRewrite());
        // create config reader bean
        applicationContext.addBean(
                ConfigReader.class,
                new ConfigReaderToFramework(config.getBeanPropertiesFileName())
        );
        // load exception handler bean
        applicationContext.addBean(ExceptionHandler.class,config.getHandlerConfig().getExceptionHandler());
    }

}
