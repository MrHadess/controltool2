package com.mh.controltool2.method.type;

public class InvokeBeanObject extends InvokeObjectInfo {

    private String beanName;

    @Override
    public TypeEnum getHandlerType() {
        return TypeEnum.InputObject;
    }

    @Override
    public Class<?> getArgClass() {
        return null;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
