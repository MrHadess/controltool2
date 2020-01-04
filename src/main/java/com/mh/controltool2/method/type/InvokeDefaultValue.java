package com.mh.controltool2.method.type;

public class InvokeDefaultValue extends InvokeObjectInfo {
    @Override
    public TypeEnum getHandlerType() {
        return TypeEnum.SupportDefaultValue;
    }

    @Override
    public Class<?> getArgClass() {
        return null;
    }
}
