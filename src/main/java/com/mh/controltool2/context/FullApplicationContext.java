package com.mh.controltool2.context;

import com.mh.controltool2.ApplicationContext;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/*
* Global value
* */
public class FullApplicationContext implements ApplicationContext {

    private static final String PACKAGE_NAME_LANG = "java.lang";

    private HashMap<String,Object> objectMap = new HashMap<>();

    @Override
    public <T> T tryGetBean(Class<T> tClass) throws InstantiationException, IllegalAccessException {
        Object obj = objectMap.get(tClass.getName());
        if (obj != null) {
            return tClass.cast(obj);
        }
        // try create obj
        return createObj(tClass,tClass);
    }

    @Override
    public <T> T tryGetBean(Class<T> tClass, Class<? extends T> tClassImpl) throws InstantiationException, IllegalAccessException {
        Object obj = objectMap.get(tClass.getName());
        if (obj != null) {
            return tClass.cast(obj);
        }
        // try create obj
        return createObj(tClass,tClassImpl);
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
        objectMap.put(tClass.getName(),obj);
    }

    @Override
    public void addBean(String name, Object obj) {
        objectMap.put(name,obj);
    }

    @Override
    public void addBean(Object obj) {
        String beanName= obj.getClass().getName();
        if (PACKAGE_NAME_LANG.startsWith(beanName)) {
            return;
        }
        objectMap.put(beanName,obj);
    }

    private synchronized <T> T createObj(Class<T> tClass, Class<? extends T> tClassImpl) throws IllegalAccessException, InstantiationException {
        // just support public constructors
        Constructor[] constructors = tClassImpl.getConstructors();
        for (Constructor item:constructors) {
            if (item.getParameterCount() <= 0) {
                T obj = tClassImpl.newInstance();
                objectMap.put(tClass.getName(),obj);
                return obj;
            }
        }
        throw new InstantiationException(tClass.getName());
    }
}
