package com.mh.controltool2.method.type;

public abstract class InvokeObjectInfo {

    private Class<?> argToClass;

    public TypeEnum getHandlerType() {
      return null;
    }

    public Class<?> getArgClass() {
        return argToClass;
    }

    public void setArgToClass(Class<?> argToClass) {
        this.argToClass = argToClass;
    }
}
