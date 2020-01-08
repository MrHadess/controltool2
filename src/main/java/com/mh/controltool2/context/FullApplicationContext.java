package com.mh.controltool2.context;

import com.mh.controltool2.ApplicationContext;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/*
* Global value
* */
public class FullApplicationContext implements ApplicationContext {

    private HashMap<String,Object> objectMap = new HashMap<>();

    @Override
    public <T> T tryGetBean(Class<T> tClass) {
        Object obj = objectMap.get(tClass.getName());
        if (obj != null) {
            return tClass.cast(obj);
        }
        // try create obj

        return null;
    }

    @Override
    public <T> T getBean(Class<T> tClass) {
        Object obj = objectMap.get(tClass.getName());
        if (obj != null) {
            return tClass.cast(obj);
        }
        return null;
    }

    @Override
    public Object getBean(String name) {
        return objectMap.get(name);
    }

    @Override
    public void addBean(Class<?> tClass, Object obj) {

    }

    @Override
    public void addBean(String name, Object obj) {

    }

    @Override
    public void addBean(Object obj) {

    }

    private Object createObj(Class<?> tClass)  {

        // just support public constructors
        Constructor[] constructors = tClass.getConstructors();
        for (Constructor item:constructors) {
            if (item.getParameterCount() <= 0) {

            } else {

            }
        }







        return null;
    }
}
