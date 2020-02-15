package com.mh.controltool2.config;

import com.mh.controltool2.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {

    private final List<InterceptorRegistration> registrations = new ArrayList<>();


    public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor) {
        InterceptorRegistration registration = new InterceptorRegistration(interceptor);
        this.registrations.add(registration);
        return registration;
    }

    /**
     * Return all registered interceptors.
     */
    protected List<InterceptorRegistration> getInterceptors() {
        return this.registrations;
    }



}
