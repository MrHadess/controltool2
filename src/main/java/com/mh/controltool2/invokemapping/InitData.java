package com.mh.controltool2.invokemapping;

public class InitData {

    private Class classForName;
    private Object classNewInstance;

    public InitData(Class classForName, Object classNewInstance) {
        this.classForName = classForName;
        this.classNewInstance = classNewInstance;
    }

    public Class getClassForName() {
        return classForName;
    }

    public void setClassForName(Class classForName) {
        this.classForName = classForName;
    }

    public Object getClassNewInstance() {
        return classNewInstance;
    }

    public void setClassNewInstance(Object classNewInstance) {
        this.classNewInstance = classNewInstance;
    }
}
