package com.mh.controltool2.scan.annotation.handler.control;

import com.mh.controltool2.annotation.Value;
import com.mh.controltool2.method.type.InvokeConfigValue;
import com.mh.controltool2.method.type.InvokeObjectInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TypeValue implements AnnotationTypeHandler {
    @Override
    public Class<? extends Annotation> getMatchAnnotation() {
        return Value.class;
    }

    @Override
    public InvokeObjectInfo annotationType(String url, Method method, Parameter parameter) {
        Value value = parameter.getAnnotation(Value.class);
        if (value == null) return null;

        InvokeConfigValue invokeConfigValue = new InvokeConfigValue();
        invokeConfigValue.setArgToClass(parameter.getClass());
        invokeConfigValue.setKey(value.value());

        return invokeConfigValue;
    }
}
