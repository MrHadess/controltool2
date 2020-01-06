package com.mh.controltool2.scan.annotation.handler.control;

import com.mh.controltool2.annotation.RequestParam;
import com.mh.controltool2.method.type.InvokeObjectInfo;
import com.mh.controltool2.method.type.InvokeRequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TypeRequestParam implements AnnotationTypeHandler {
    @Override
    public Class<? extends Annotation> getMatchAnnotation() {
        return RequestParam.class;
    }

    @Override
    public InvokeObjectInfo annotationType(String url, Method method, Parameter parameter) {

        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        if (requestParam == null) return null;

        String reqParamString = requestParam.value();
        InvokeRequestParam invokeRequestParam = new InvokeRequestParam();
        invokeRequestParam.setParamKey(reqParamString);
        invokeRequestParam.setArgToClass(parameter.getType());

        return invokeRequestParam;
    }
}
