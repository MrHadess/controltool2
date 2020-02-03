package com.mh.controltool2.config.annotation;

import com.mh.controltool2.config.BeanRegistry;
import com.mh.controltool2.config.InterceptorRegistry;
import com.mh.controltool2.handler.message.DefaultExceptionHandler;
import com.mh.controltool2.handler.message.ExceptionHandler;
import com.mh.controltool2.handler.message.HttpMessageRewrite;
import com.mh.controltool2.handler.message.JsonDefaultHttpMessageRewrite;
import com.mh.controltool2.serialize.json.DataObjectSerialize;
import com.mh.controltool2.serialize.json.DefaultDataObjectSerialize;

public interface Configurer2 {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new CheckAccessAdderInterceptor())
//                .addPathPatterns("/AyCode/AdminControl/**")
//                .excludePathPatterns("/AyCode/AdminControl/getProjectToFirstCodeList/**");
//
//        InterceptorRegistration registration = registry.addInterceptor(getCheckAccessAdderInterceptor());
////        registration.addPathPatterns("/AyCode/AdminControl/**");
//        registration.addPathPatterns("/**");
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/res/**").addResourceLocations("/AyCode/");
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
////        registry.addRedirectViewController("/AyCodeAdmin/**","/AyCode/AdminControl/**");
//
//    }


    // HandlerInterceptor
    default void addInterceptors(InterceptorRegistry registry) { }

    default void registerBean(BeanRegistry beanRegistry) {}

    default DataObjectSerialize registerDataObjectSerialize() {
        return new DefaultDataObjectSerialize();
    }

    default HttpMessageRewrite registerHttpMessageRewrite(DataObjectSerialize dataObjectSerialize) {
        return new JsonDefaultHttpMessageRewrite(dataObjectSerialize);
    }

    default ExceptionHandler registerExceptionHandler() {
        return new DefaultExceptionHandler();
    }


}
