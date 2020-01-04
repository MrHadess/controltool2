package com.mh.controltool2.scan;

import java.util.*;

public class InvokeMsg {

    private String className;
    private String methodName;
    private Class<?>[] methodParameterNames;//参数类型数组
    private ParameterNamesMatch[] methodParameterTypes;//参数类型对应的处理器
    private String[] methodRequestParamTypes;//请求获取参数对应的参数
//    private int responseWriteState;//数据返回状态
    private CheckResponseWrite.ReturnStateToDo responseWriteState;//数据返回状态

    //函数参数 正则配对分组参数
    private HashMap<Integer,Integer> parameterMatchGroups;


    //AbsolutelyMatch
    public InvokeMsg(String className, String methodName, Class<?>[] methodParameterNames, ParameterNamesMatch[] methodParameterTypes, String[] methodRequestParamTypes, CheckResponseWrite.ReturnStateToDo responseWriteState) {
        this.className = className;
        this.methodName = methodName;
        this.methodParameterNames = methodParameterNames;
        this.methodParameterTypes = methodParameterTypes;
        this.methodRequestParamTypes = methodRequestParamTypes;
        this.responseWriteState = responseWriteState;
    }


    //FuzzyMatch
    public InvokeMsg(
            String className, String methodName, Class<?>[] methodParameterNames, ParameterNamesMatch[] methodParameterTypes, String[] methodRequestParamTypes, CheckResponseWrite.ReturnStateToDo responseWriteState, HashMap<Integer, Integer> parameterMatchGroups) {

        this.className = className;
        this.methodName = methodName;
        this.methodParameterNames = methodParameterNames;
        this.methodParameterTypes = methodParameterTypes;
        this.methodRequestParamTypes = methodRequestParamTypes;
        this.responseWriteState = responseWriteState;
        this.parameterMatchGroups = parameterMatchGroups;
    }

    public HashMap<Integer, Integer> getParameterMatchGroups() {
        return parameterMatchGroups;
    }

    public void setParameterMatchGroups(HashMap<Integer, Integer> parameterMatchGroups) {
        this.parameterMatchGroups = parameterMatchGroups;
    }

    public ParameterNamesMatch[] getMethodParameterTypes() {
        return methodParameterTypes;
    }

    public void setMethodParameterTypes(ParameterNamesMatch[] methodParameterTypes) {
        this.methodParameterTypes = methodParameterTypes;
    }

    public String[] getMethodRequestParamTypes() {
        return methodRequestParamTypes;
    }

    public void setMethodRequestParamTypes(String[] methodRequestParamTypes) {
        this.methodRequestParamTypes = methodRequestParamTypes;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getMethodParameterNames() {
        return methodParameterNames;
    }

    public void setMethodParameterNames(Class<?>[] methodParameterNames) {
        this.methodParameterNames = methodParameterNames;
    }

    public CheckResponseWrite.ReturnStateToDo getResponseWriteState() {
        return responseWriteState;
    }

    public void setResponseWriteState(CheckResponseWrite.ReturnStateToDo responseWriteState) {
        this.responseWriteState = responseWriteState;
    }

    private String printParameterNames(){
        StringBuilder sb = new StringBuilder();
        for (Class<?> item: methodParameterNames){
            sb.append(item.getName());
            sb.append(",");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "\n\nClassName:::" + className + "\nMethodName:::" + methodName + "\nParameterNames:::" + printParameterNames() + "\nHandlerSate:::" + responseWriteState;
    }

    public enum ParameterNamesMatch{

        Unknown(0),
        Request(1),
        Response(2),
        PathVariable(3),
        RequestParam(4);

        int parameterType;
        ParameterNamesMatch(int parameterType){
            this.parameterType = parameterType;
        }

        public static ParameterNamesMatch getValueType(int i){

            for (ParameterNamesMatch item:ParameterNamesMatch.values()){
                if (item.parameterType == i) return item;
            }

            return ParameterNamesMatch.Unknown;
        }


    }
}
