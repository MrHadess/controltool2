package com.mh.controltool2.method.type;

public class InvokeRequestBody extends InvokeObjectInfo {
    @Override
    public TypeEnum getHandlerType() {
        return TypeEnum.RequestBody;
    }
}
