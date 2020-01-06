package com.mh.controltool2.scan.annotation.handler.control;

import com.mh.controltool2.annotation.PathVariable;
import com.mh.controltool2.method.type.InvokeObjectInfo;
import com.mh.controltool2.method.type.InvokePathVariable;
import com.mh.controltool2.method.type.InvokeUnmatchObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypePathVariable implements AnnotationTypeHandler {

    private static Pattern CheckItemMatch = Pattern.compile("\\{.+?}");

    @Override
    public Class<? extends Annotation> getMatchAnnotation() {
        return PathVariable.class;
    }

    @Override
    public InvokeObjectInfo annotationType(String url, Method method, Parameter parameter) {
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        if (pathVariable == null) return null;

        int matchLocalIndex = 0;
        String matchKey = pathVariable.value();
        Matcher matcher = CheckItemMatch.matcher(url);
        while (matcher.find()) {

            String matchContentGroup = matcher.group();
            String matchContent = matchContentGroup.substring(1,matchContentGroup.length() - 1);
            if (Objects.equals(matchKey,matchContent)) {
                InvokePathVariable invokePathVariable = new InvokePathVariable();
                invokePathVariable.setMatchLocalIndex(matchLocalIndex);
                invokePathVariable.setArgToClass(parameter.getType());
                return invokePathVariable;
            }
            matchLocalIndex++;
        }

        return new InvokeUnmatchObject();
    }
}
