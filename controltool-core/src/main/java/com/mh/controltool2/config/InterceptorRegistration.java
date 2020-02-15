package com.mh.controltool2.config;

import com.mh.controltool2.servlet.HandlerInterceptor;
import com.mh.controltool2.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterceptorRegistration {

    private final HandlerInterceptor interceptor;

    private final List<String> includePatterns = new ArrayList<>();

    private final List<String> excludePatterns = new ArrayList<>();


    /**
     * Create an {@link InterceptorRegistration} instance.
     */
    public InterceptorRegistration(HandlerInterceptor interceptor) {
        Assert.notNull(interceptor, "Interceptor is required");
        this.interceptor = interceptor;
    }


    /**
     * Add URL patterns to which the registered interceptor should apply to.
     */
    public InterceptorRegistration addPathPatterns(String... patterns) {
        return addPathPatterns(Arrays.asList(patterns));
    }

    /**
     * List-based variant of {@link #addPathPatterns(String...)}.
     * @since 5.0.3
     */
    public InterceptorRegistration addPathPatterns(List<String> patterns) {
        this.includePatterns.addAll(patterns);
        return this;
    }

    /**
     * Add URL patterns to which the registered interceptor should not apply to.
     */
    public InterceptorRegistration excludePathPatterns(String... patterns) {
        return excludePathPatterns(Arrays.asList(patterns));
    }

    /**
     * List-based variant of {@link #excludePathPatterns(String...)}.
     * @since 5.0.3
     */
    public InterceptorRegistration excludePathPatterns(List<String> patterns) {
        this.excludePatterns.addAll(patterns);
        return this;
    }

    protected MappedInterceptor getInterceptor() {
        return new MappedInterceptor(
                interceptor,
                includePatterns,
                excludePatterns
        );
    }

}
