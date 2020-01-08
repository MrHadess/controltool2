package com.mh.controltool2;

import java.util.HashMap;

/*
* Global value
* */
public interface ApplicationContext {

    /*
    * If get bean is uncreated object will be try create this object
    *
    * */
    <T> T tryGetBean(Class<T> tClass);

    <T> T getBean(Class<T> tClass);

    Object getBean(String name);

    void addBean(Class<?> tClass,Object obj);

    void addBean(String name,Object obj);

    void addBean(Object obj);



}
