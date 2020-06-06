package com.mh.controltool2.sample;

import com.mh.controltool2.annotation.Configuration;
import com.mh.controltool2.config.InterceptorRegistry;
import com.mh.controltool2.config.annotation.Configurer;
import com.mh.controltool2.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Use configurer,must be impl 'Configurer' and annotation 'Configuration'
 * @see Configurer
 * @see Configuration
 *
 * Then focuse: Once instance servlet,just use any one use annotation 'Configuration'
 *
 * */
@Configuration
public class Config implements Configurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                System.out.println(request.getLocalAddr());
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                // Generally used to recovery resource
            }
        }).addPathPatterns("/test/**");
    }

}
