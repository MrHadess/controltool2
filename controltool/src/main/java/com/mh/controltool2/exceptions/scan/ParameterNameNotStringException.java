package com.mh.controltool2.exceptions.scan;

import java.lang.reflect.Method;

/**
 * 一般为使用‘RequestParam’注解后发现参数并不为‘String’时，此异常将会被触发
 * */
public class ParameterNameNotStringException extends RuntimeException {

    private Method errMethod;

    public ParameterNameNotStringException() {
        super("This parameter name is not 'String'");
    }

    /**
     * method to throw exception details
     * */
    public ParameterNameNotStringException(Method method) {
        super("This parameter name is not 'String'");
        this.errMethod = method;
    }

    @Override
    public String toString() {
        return "This parameter name is not 'String'\n" + errMethod.toString();
    }
}
