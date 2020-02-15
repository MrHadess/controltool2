package com.mh.controltool2.method;

import com.mh.controltool2.method.type.InvokeObjectInfo;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodInvokeInfo {

    private Class<?> targetClass;
    private Method targetMethod;
    private InvokeObjectInfo[] invokeObjectInfoGroup;

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(Method targetMethod) {
        this.targetMethod = targetMethod;
    }

    public InvokeObjectInfo[] getInvokeObjectInfoGroup() {
        return invokeObjectInfoGroup;
    }

    public void setInvokeObjectInfoGroup(InvokeObjectInfo[] invokeObjectInfoGroup) {
        this.invokeObjectInfoGroup = invokeObjectInfoGroup;
    }

    @Override
    public String toString() {
        return "MethodInvokeInfo{" +
                "targetClass='" + targetClass + '\'' +
                ", targetMethod='" + targetMethod + '\'' +
                ", invokeObjectInfoGroup=" + Arrays.toString(invokeObjectInfoGroup) +
                '}';
    }
}
