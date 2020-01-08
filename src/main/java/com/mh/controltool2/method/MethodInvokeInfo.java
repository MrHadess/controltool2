package com.mh.controltool2.method;

import com.mh.controltool2.method.type.InvokeObjectInfo;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodInvokeInfo {

    private Class<?> classname;
    private Method methodName;
    private InvokeObjectInfo[] invokeObjectInfoGroup;

    public Class<?> getClassname() {
        return classname;
    }

    public void setClassname(Class<?> classname) {
        this.classname = classname;
    }

    public Method getMethodName() {
        return methodName;
    }

    public void setMethodName(Method methodName) {
        this.methodName = methodName;
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
                "classname='" + classname + '\'' +
                ", methodName='" + methodName + '\'' +
                ", invokeObjectInfoGroup=" + Arrays.toString(invokeObjectInfoGroup) +
                '}';
    }
}
