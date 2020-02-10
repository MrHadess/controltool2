package com.mh.controltool2.config.annotation;

import com.mh.controltool2.config.BeanRegistry;
import com.mh.controltool2.config.InterceptorRegistry;
import com.mh.controltool2.handler.message.ExceptionHandler;
import com.mh.controltool2.handler.message.HttpMessageRewrite;
import com.mh.controltool2.serialize.json.DataObjectSerialize;

public interface Configurer2 {

    // HandlerInterceptor
    default void addInterceptors(InterceptorRegistry registry) { }

    default void registerBean(BeanRegistry beanRegistry) {}

    default DataObjectSerialize registerDataObjectSerialize() {
        return null;
    }

    default HttpMessageRewrite registerHttpMessageRewrite(DataObjectSerialize dataObjectSerialize) {
        return null;
    }

    default ExceptionHandler registerExceptionHandler() {
        return null;
    }


}
