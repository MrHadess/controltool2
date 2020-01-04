package com.mh.controltool2.scan;


import com.mh.controltool2.annotation.PathVariable;
import com.mh.controltool2.annotation.RequestParam;
import com.mh.controltool2.exceptions.scan.ParameterNameNotStringException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CheckParameterType {

    private static final String HttpServletRequestName =  javax.servlet.http.HttpServletRequest.class.getName();
    private static final String HttpServletResponseName =  javax.servlet.http.HttpServletResponse.class.getName();

    //检查数据注入的类型
    public InvokeMsg.ParameterNamesMatch[] getParameterTypes(Method method){

        return getParameterTypes(method.getParameterTypes(),method.getParameterAnnotations());
    }

    @SuppressWarnings("WeakerAccess")
    public InvokeMsg.ParameterNamesMatch[] getParameterTypes(Class<?>[] methodParameterTypes,Annotation[][] methodParameterAnnotations) throws ParameterNameNotStringException {

        //声明各个参数类型的数组
        InvokeMsg.ParameterNamesMatch[] parameterNamesMatchNum = new InvokeMsg.ParameterNamesMatch[methodParameterTypes.length];

        for (int parameterTypeLocal = 0; parameterTypeLocal < methodParameterTypes.length; parameterTypeLocal++){


            /*
            * 优先检查注解
            * 由于当前的实现限制，在方法中的参数注解中不得多于一个，以避免注入冲突
            * */
            Annotation[] aParameterAnnotation = methodParameterAnnotations[parameterTypeLocal];
            //满足只包含一个注解时
            if (aParameterAnnotation.length == 1) {
                Annotation item = aParameterAnnotation[0];
                if (item.annotationType().equals(PathVariable.class)) {
                    parameterNamesMatchNum[parameterTypeLocal] = InvokeMsg.ParameterNamesMatch.PathVariable;
                    continue;
                }
                if (item.annotationType().equals(RequestParam.class)) {
                    if (!methodParameterTypes[parameterTypeLocal].getName().equals("java.lang.String"))
                        throw new ParameterNameNotStringException();
                    parameterNamesMatchNum[parameterTypeLocal] = InvokeMsg.ParameterNamesMatch.RequestParam;
                    continue;
                }

            }


            //开始匹配无注解类型的参数
            String aParameterName = methodParameterTypes[parameterTypeLocal].getName();

            if(aParameterName.equals(HttpServletRequestName)){
                parameterNamesMatchNum[parameterTypeLocal] = InvokeMsg.ParameterNamesMatch.Request;
                continue;
            }
            if (aParameterName.equals(HttpServletResponseName)){
                parameterNamesMatchNum[parameterTypeLocal] = InvokeMsg.ParameterNamesMatch.Response;
                continue;
            }

            //判断是否为其子类
//            if (methodParameterTypes[parameterTypeLocal].isAssignableFrom(HttpServletRequest.class)){
//                parameterNamesMatchNum[parameterTypeLocal] = InvokeMsg.ParameterNamesMatch.Request;
//                continue;
//            }
//            if (methodParameterTypes[parameterTypeLocal].isAssignableFrom(HttpServletResponse.class)){
//                parameterNamesMatchNum[parameterTypeLocal] = InvokeMsg.ParameterNamesMatch.Response;
//                continue;
//            }


            parameterNamesMatchNum[parameterTypeLocal] = InvokeMsg.ParameterNamesMatch.Unknown;

        }

        return parameterNamesMatchNum;


    }

    //检查Request请求参数的数据
    public InvokeMsg.ParameterNamesMatch[] getRequestParamTypes(Method method){

        return getParameterTypes(method.getParameterTypes(),method.getParameterAnnotations());
    }

    @SuppressWarnings("WeakerAccess")
    public String[] getRequestParamTypes(Class<?>[] methodParameterTypes,Annotation[][] methodParameterAnnotations){

        //声明各个参数类型的数组
        String[] parameterNamesMatchNum = new String[methodParameterTypes.length];

        for (int parameterTypeLocal = 0; parameterTypeLocal < methodParameterTypes.length; parameterTypeLocal++){


            /*
            * 优先检查注解
            * 由于当前的实现限制，在方法中的参数注解中不得多于一个，以避免注入冲突
            * */
            Annotation[] aParameterAnnotation = methodParameterAnnotations[parameterTypeLocal];
            //满足只包含一个注解时
            if (aParameterAnnotation.length == 1) {
                Annotation item = aParameterAnnotation[0];
                if (item.annotationType().equals(RequestParam.class)) {
                    RequestParam reqParam = (RequestParam) item;
                    parameterNamesMatchNum[parameterTypeLocal] = reqParam.value();;
                }

            }



        }

        return parameterNamesMatchNum;


    }


}
