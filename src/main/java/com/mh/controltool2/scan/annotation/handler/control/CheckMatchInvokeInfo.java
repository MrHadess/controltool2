package com.mh.controltool2.scan.annotation.handler.control;

import com.mh.controltool2.exceptions.scan.MultipleAnnotationException;
import com.mh.controltool2.method.type.InvokeObjectInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class CheckMatchInvokeInfo {

    public InvokeObjectInfo[] checkToInvokeObjectGroup(String url,Method method) throws MultipleAnnotationException {
        if (method.getParameterCount() <= 0) {
            return new InvokeObjectInfo[0];
        }
        Annotation[][] annotationGroup = method.getParameterAnnotations();
        Parameter[] parameter = method.getParameters();

        for (int i = 0;i < parameter.length;i++) {
            Annotation[] paramAnnotation = parameter[i].getAnnotations();
            if (paramAnnotation.length > 1) {
                throw new MultipleAnnotationException();
            } else if (paramAnnotation.length <= 0) {
                // use default value
            } else {
                // use framework handler
                parameter[i].getType();
            }


        }

        InvokeObjectInfo[] invokeObjectInfos = new InvokeObjectInfo[method.getParameterCount()];

        for (int i = 0;i < annotationGroup.length;i++) {
            if (annotationGroup[i].length > 1) {
                throw new MultipleAnnotationException();
            } else if (annotationGroup[i].length < 0) {
                // use default value
            } else {
                // use framework handler

            }


        }



        return null;
    }

    private InvokeObjectInfo get(String url,Method method,Parameter parameter) {





        return null;
    }

}
