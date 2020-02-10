package com.mh.controltool2.method.type;

public class InvokeConfigValue extends InvokeObjectInfo {

    private String key;

    @Override
    public TypeEnum getHandlerType() {
        return TypeEnum.ConfigValue;
    }

    @Override
    public Class<?> getArgClass() {
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
