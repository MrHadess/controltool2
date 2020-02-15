package com.mh.controltool2.scan.annotation.handler.control;

import com.mh.controltool2.annotation.RequestHeader;
import com.mh.controltool2.method.type.InvokeObjectInfo;
import com.mh.controltool2.method.type.InvokeRequestHeader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TypeRequestHeader implements AnnotationTypeHandler {
    @Override
    public Class<? extends Annotation> getMatchAnnotation() {
        return RequestHeader.class;
    }

    @Override
    public InvokeObjectInfo annotationType(String url, Method method, Parameter parameter) {

        RequestHeader requestParam = parameter.getAnnotation(RequestHeader.class);
        if (requestParam == null) return null;

        String reqParamString = requestParam.value();
        InvokeRequestHeader invokeRequestHeader = new InvokeRequestHeader();
        invokeRequestHeader.setParamKey(reqParamString);
        invokeRequestHeader.setArgToClass(parameter.getType());

        return invokeRequestHeader;
    }
}
