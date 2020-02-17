package com.mh.controltool2.scan.annotation.handler.control;

import com.mh.controltool2.annotation.RequestBody;
import com.mh.controltool2.method.type.InvokeObjectInfo;
import com.mh.controltool2.method.type.InvokeRequestBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TypeRequestBody implements AnnotationTypeHandler {
    @Override
    public Class<? extends Annotation> getMatchAnnotation() {
        return RequestBody.class;
    }

    @Override
    public InvokeObjectInfo annotationType(String url, Method method, Parameter parameter) {
        RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
        if (requestBody == null) return null;

        InvokeRequestBody invokeRequestBody = new InvokeRequestBody();
        invokeRequestBody.setParameterizedType(parameter.getParameterizedType());
        invokeRequestBody.setArgToClass(parameter.getType());

        return invokeRequestBody;
    }
}
