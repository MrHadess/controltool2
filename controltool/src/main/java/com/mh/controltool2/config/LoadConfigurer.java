package com.mh.controltool2.config;

import com.mh.controltool2.config.annotation.Configurer;
import com.mh.controltool2.handler.message.DefaultExceptionHandler;
import com.mh.controltool2.handler.message.ExceptionHandler;
import com.mh.controltool2.handler.message.HttpMessageRewrite;
import com.mh.controltool2.handler.message.JsonDefaultHttpMessageRewrite;
import com.mh.controltool2.serialize.json.DataObjectSerialize;
import com.mh.controltool2.serialize.json.DefaultDataObjectSerialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadConfigurer {

    // Must be not null object
    protected List<MappedInterceptor> mappedInterceptorList = new ArrayList<>();
    protected Map<String,Object> beanMap = new HashMap<>();
    protected DataObjectSerialize dataObjectSerialize = new DefaultDataObjectSerialize();
    protected HttpMessageRewrite httpMessageRewrite = new JsonDefaultHttpMessageRewrite(dataObjectSerialize);
    protected ExceptionHandler exceptionHandler = new DefaultExceptionHandler();

    public void load(Configurer configurer) {
        // load interceptor
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
        configurer.addInterceptors(interceptorRegistry);
        this.mappedInterceptorList = new ArrayList<>();
        for (InterceptorRegistration item:interceptorRegistry.getInterceptors()) {
            this.mappedInterceptorList.add(item.getInterceptor());
        }

        // load custom bean
        BeanRegistry beanRegistry = new BeanRegistry();
        configurer.registerBean(beanRegistry);
        this.beanMap = beanRegistry.getBeanMap();

        // load DataObjectSerialize
        DataObjectSerialize tempDataObjectSerialize = configurer.registerDataObjectSerialize();
        if (tempDataObjectSerialize != null) {
            dataObjectSerialize = tempDataObjectSerialize;
        }
        // load HttpMessageRewrite
        HttpMessageRewrite tempHttpMessageRewrite = configurer.registerHttpMessageRewrite(dataObjectSerialize);
        if (tempHttpMessageRewrite != null) {
            httpMessageRewrite = tempHttpMessageRewrite;
        }
        // load ExceptionHandler
        ExceptionHandler tempExceptionHandler = configurer.registerExceptionHandler();
        if (tempDataObjectSerialize != null) {
            exceptionHandler = tempExceptionHandler;
        }
    }

    public List<MappedInterceptor> getMappedInterceptorList() {
        return mappedInterceptorList;
    }

    public Map<String, Object> getBeanMap() {
        return beanMap;
    }

    public DataObjectSerialize getDataObjectSerialize() {
        return dataObjectSerialize;
    }

    public HttpMessageRewrite getHttpMessageRewrite() {
        return httpMessageRewrite;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public String toString() {
        return "LoadConfigurer{" +
                "mappedInterceptorList=" + mappedInterceptorList +
                ", beanMap=" + beanMap +
                ", dataObjectSerialize=" + dataObjectSerialize +
                ", httpMessageRewrite=" + httpMessageRewrite +
                ", exceptionHandler=" + exceptionHandler +
                '}';
    }
}
