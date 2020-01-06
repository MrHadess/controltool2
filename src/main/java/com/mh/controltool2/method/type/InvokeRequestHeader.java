package com.mh.controltool2.method.type;


/*
* get request header value
* need:
* param for key
* */
public class InvokeRequestHeader extends InvokeObjectInfo {

    private String paramKey;

    @Override
    public TypeEnum getHandlerType() {
        return TypeEnum.RequestHeader;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }
}
