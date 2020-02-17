package com.mh.controltool2.method.type;


/*
* get request param value
* need:
* param for key
* */
public class InvokeRequestParam extends InvokeObjectInfo {

    private String paramKey;

    @Override
    public TypeEnum getHandlerType() {
        return TypeEnum.RequestParam;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }
}
