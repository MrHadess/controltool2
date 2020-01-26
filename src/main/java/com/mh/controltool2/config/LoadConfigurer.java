package com.mh.controltool2.config;

import com.mh.controltool2.config.annotation.Configurer2;

import java.util.ArrayList;
import java.util.List;

public class LoadConfigurer {

    private List<MappedInterceptor> mappedInterceptorList;

    public LoadConfigurer(Configurer2 configurer) {
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
        configurer.addInterceptors(interceptorRegistry);

        mappedInterceptorList = new ArrayList<>();
        for (InterceptorRegistration item:interceptorRegistry.getInterceptors()) {
            mappedInterceptorList.add(item.getInterceptor());
        }
    }

    public List<MappedInterceptor> getMappedInterceptorList() {
        return mappedInterceptorList;
    }

    @Override
    public String toString() {
        return "LoadConfigurer{" +
                "mappedInterceptorList=" + mappedInterceptorList +
                '}';
    }
}
