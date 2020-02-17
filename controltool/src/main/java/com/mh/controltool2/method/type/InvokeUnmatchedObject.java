package com.mh.controltool2.method.type;

public class InvokeUnmatchedObject extends InvokeObjectInfo {
    @Override
    public TypeEnum getHandlerType() {
        return TypeEnum.Unmatched;
    }
}
