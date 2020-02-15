package com.mh.controltool2.method.type;


import java.lang.reflect.Type;

/*
* use request body to object
* need:
* param parameterized type
* */
public class InvokeRequestBody extends InvokeObjectInfo {

    private Type parameterizedType;

    @Override
    public TypeEnum getHandlerType() {
        return TypeEnum.RequestBody;
    }

    public Type getParameterizedType() {
        return parameterizedType;
    }

    public void setParameterizedType(Type parameterizedType) {
        this.parameterizedType = parameterizedType;
    }
}
