package com.mh.controltool2.scan.annotation.handler.control;

import com.mh.controltool2.annotation.Bean;
import com.mh.controltool2.method.type.InvokeBeanObject;
import com.mh.controltool2.method.type.InvokeObjectInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TypeBean implements AnnotationTypeHandler {
    @Override
    public Class<? extends Annotation> getMatchAnnotation() {
        return Bean.class;
    }

    @Override
    public InvokeObjectInfo annotationType(String url, Method method, Parameter parameter) {
        Bean bean = parameter.getAnnotation(Bean.class);
        if (bean == null) return null;

        InvokeBeanObject invokeBeanObject = new InvokeBeanObject();
        if("".equals(bean.name())) {
            invokeBeanObject.setBeanName(bean.name());
        }
        invokeBeanObject.setArgToClass(parameter.getType());
        return invokeBeanObject;
    }
}
