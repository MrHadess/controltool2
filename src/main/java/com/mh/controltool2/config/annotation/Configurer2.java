package com.mh.controltool2.config.annotation;

import com.mh.controltool2.config.InterceptorRegistry;

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


}
