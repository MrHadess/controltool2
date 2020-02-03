package com.mh.controltool2.config;

import com.mh.controltool2.config.annotation.Configurer2;
import com.mh.controltool2.handler.message.ExceptionHandler;
import com.mh.controltool2.handler.message.HttpMessageRewrite;
import com.mh.controltool2.serialize.json.DataObjectSerialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadConfigurer {

    protected List<MappedInterceptor> mappedInterceptorList;
    protected Map<String,Object> beanMap;
    protected DataObjectSerialize dataObjectSerialize;
    protected HttpMessageRewrite httpMessageRewrite;
    protected ExceptionHandler exceptionHandler;

    public void load(Configurer2 configurer) {
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
        dataObjectSerialize = configurer.registerDataObjectSerialize();
        // load HttpMessageRewrite
        httpMessageRewrite = configurer.registerHttpMessageRewrite(dataObjectSerialize);
        // load ExceptionHandler
        exceptionHandler = configurer.registerExceptionHandler();
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
