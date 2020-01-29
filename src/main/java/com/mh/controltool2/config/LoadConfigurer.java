package com.mh.controltool2.config;

import com.mh.controltool2.config.annotation.Configurer2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadConfigurer {

    private List<MappedInterceptor> mappedInterceptorList;
    private Map<String,Object> beanMap;

    public LoadConfigurer(Configurer2 configurer) {
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
        configurer.addInterceptors(interceptorRegistry);
        this.mappedInterceptorList = new ArrayList<>();
        for (InterceptorRegistration item:interceptorRegistry.getInterceptors()) {
            this.mappedInterceptorList.add(item.getInterceptor());
        }

        BeanRegistry beanRegistry = new BeanRegistry();
        configurer.registerBean(beanRegistry);
        this.beanMap = beanRegistry.getBeanMap();

    }

    public List<MappedInterceptor> getMappedInterceptorList() {
        return mappedInterceptorList;
    }

    public Map<String, Object> getBeanMap() {
        return beanMap;
    }

    @Override
    public String toString() {
        return "LoadConfigurer{" +
                "mappedInterceptorList=" + mappedInterceptorList +
                ", beanMap=" + beanMap +
                '}';
    }
}
