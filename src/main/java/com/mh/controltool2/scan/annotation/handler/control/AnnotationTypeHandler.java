package com.mh.controltool2.scan.annotation.handler.control;

import com.mh.controltool2.method.type.InvokeObjectInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface AnnotationTypeHandler {

    Class<? extends Annotation> getMatchAnnotation();

    InvokeObjectInfo annotationType(String url, Method method, Parameter parameter);

}
