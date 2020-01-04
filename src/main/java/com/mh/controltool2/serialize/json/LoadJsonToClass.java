package com.mh.controltool2.serialize.json;

import com.mh.controltool2.exceptions.invoke.JsonParseException;
import com.mh.controltool2.exceptions.invoke.NotJsonPackageException;
import com.mh.controltool2.LogOut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LoadJsonToClass {

    private static final String TAG = "ObjectToJson";

    private JsonObjTargetEnum useJsonObjTarget;

    private Object toClass;
    private Method toMethod;

    public LoadJsonToClass() throws NotJsonPackageException {

        //以下Catch为不可预知的错误进行捕获 一般此错误不会被触发 So 将不向上抛异常
        try {
            loadObjectAndMethod();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        //检查是否有示例化的对象 null表示尝试加载失败
        if (this.toClass == null) throw new NotJsonPackageException();


    }

    @SuppressWarnings("WeakerAccess")
    public String toJson(Object o) throws JsonParseException {
        if (this.toClass == null) return "";
        try {
            return (String) toMethod.invoke(toClass,o);
        } catch (InvocationTargetException e) {
             throw new JsonParseException(e);
        } catch (IllegalAccessException e) {
            throw new JsonParseException(e);
        }
    }

    public JsonObjTargetEnum getUseJsonObjTarget() {
        return useJsonObjTarget;
    }

    //尝试加载Json序列化方案
    private void loadObjectAndMethod() throws IllegalAccessException, NoSuchMethodException, InstantiationException {

        //一旦有一个Object序列化包被正常实例化时将立即中断操作 以下为加载顺序
        if (gsonJson()) {
            useJsonObjTarget = JsonObjTargetEnum.Gson;
            return;
        }
        if (jacksonJson()) {
            useJsonObjTarget = JsonObjTargetEnum.Jackson;
            return;
        }
        if (fastJson()) {
            useJsonObjTarget = JsonObjTargetEnum.FastJson;
            return;
        }

    }

    private boolean gsonJson() throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        Class<?> gson = null;
        try {
            gson = Class.forName("com.google.gson.Gson");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        Object gsonToObject = gson.newInstance();
        Method gsonTojsonMethod = gson.getMethod("toJson",Object.class);

        this.toClass = gsonToObject;
        this.toMethod = gsonTojsonMethod;
        LogOut.i(TAG,"Use Gson serialize");
        return true;
    }

    private boolean jacksonJson() throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        Class<?> jackson = null;
        try {
            jackson = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        Object jacksonToObject = jackson.newInstance();
        Method jacksonToJsonMethod = jackson.getMethod("writeValueAsString",Object.class);

        this.toClass = jacksonToObject;
        this.toMethod = jacksonToJsonMethod;
        LogOut.i(TAG,"Use Jackson serialize");
        return true;
    }

    private boolean fastJson() throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        Class<?> fastJson = null;
        try {
            fastJson = Class.forName("com.alibaba.fastjson.JSON");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        Object fastJsonToObject = fastJson.newInstance();
        Method fastJsonTojsonMethod = fastJson.getMethod("toJSONString",Object.class);

        this.toClass = fastJsonToObject;
        this.toMethod = fastJsonTojsonMethod;
        LogOut.i(TAG,"Use Fastjson serialize");
        return true;
    }


}
