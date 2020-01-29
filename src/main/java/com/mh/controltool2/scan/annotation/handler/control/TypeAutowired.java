package com.mh.controltool2.scan.annotation.handler.control;

import com.mh.controltool2.annotation.Autowired;
import com.mh.controltool2.method.type.InvokeBeanObject;
import com.mh.controltool2.method.type.InvokeObjectInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TypeAutowired implements AnnotationTypeHandler {
    @Override
    public Class<? extends Annotation> getMatchAnnotation() {
        return Autowired.class;
    }

    @Override
    public InvokeObjectInfo annotationType(String url, Method method, Parameter parameter) {
        Autowired autowired = parameter.getAnnotation(Autowired.class);
        if (autowired == null) return null;

        InvokeBeanObject invokeBeanObject = new InvokeBeanObject();
        invokeBeanObject.setArgToClass(parameter.getType());
        return invokeBeanObject;
    }
}
