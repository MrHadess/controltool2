package com.mh.controltool2.scan.annotation.handler.control;

import com.mh.controltool2.method.type.InvokeDefaultValue;
import com.mh.controltool2.method.type.InvokeObjectInfo;
import com.mh.controltool2.method.type.InvokeUnmatchedObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class CheckMatchInvokeInfo {

    private static Logger logger = LoggerFactory.getLogger("CheckMatchInvokeInfo");

    private static final List<AnnotationTypeHandler> ANNOTATION_TYPE_HANDLERS = new ArrayList<AnnotationTypeHandler>(){{
        add(new TypePathVariable());
        add(new TypeRequestBody());
        add(new TypeRequestParam());
        add(new TypeRequestHeader());
    }};

    public InvokeObjectInfo[] checkToInvokeObjectGroup(String url,Method method) {
        if (method.getParameterCount() <= 0) {
            return new InvokeObjectInfo[0];
        }
        Parameter[] parameter = method.getParameters();
        InvokeObjectInfo[] invokeObjectInfoGroup = new InvokeObjectInfo[parameter.length];

        for (int i = 0;i < parameter.length;i++) {
            Annotation[] paramAnnotation = parameter[i].getAnnotations();
            if (paramAnnotation.length > 1) {
                logger.error(String.format("Method parameter has multiple annotation,will be use any one. -- %s",method.toString()));
            }

            if (paramAnnotation.length <= 0) {
                // use default value
                invokeObjectInfoGroup[i] = unknownAnnotationMatch(parameter[i]);
                continue;
            } else {
                // use framework handler (Match any one annotation logic)
//                parameter[i].getType();
                for (AnnotationTypeHandler item : ANNOTATION_TYPE_HANDLERS) {
                    if (!parameter[i].isAnnotationPresent(item.getMatchAnnotation())) {
                        continue;
                    }
                    invokeObjectInfoGroup[i] = item.annotationType(url,method,parameter[i]);
                    break;
                }

            }

            if (invokeObjectInfoGroup[i] == null) {
                invokeObjectInfoGroup[i] = unknownAnnotationMatch(parameter[i]);
            }

        }

        return invokeObjectInfoGroup;
    }

    private InvokeObjectInfo unknownAnnotationMatch(Parameter parameter) {
        if (supportDefaultParam(parameter.getType())) {
            InvokeDefaultValue invokeDefaultValue = new InvokeDefaultValue();
            invokeDefaultValue.setArgToClass(parameter.getType());
            return invokeDefaultValue;
        }

        InvokeUnmatchedObject invokeUnmatchedObject = new InvokeUnmatchedObject();
        invokeUnmatchedObject.setArgToClass(parameter.getType());
        return invokeUnmatchedObject;
    }

    private boolean supportDefaultParam(Class<?> tClass) {
        if (tClass.isAssignableFrom(HttpServletRequest.class)) return true;
        if (tClass.isAssignableFrom(HttpServletResponse.class)) return true;

        return false;
    }

}
