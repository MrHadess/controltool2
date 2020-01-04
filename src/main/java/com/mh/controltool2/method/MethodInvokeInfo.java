package com.mh.controltool2.method;

import com.mh.controltool2.method.type.InvokeObjectInfo;

import java.util.Arrays;

public class MethodInvokeInfo {

    private String classname;
    private String methodName;
    private InvokeObjectInfo[] invokeObjectInfoGroup;

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
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
