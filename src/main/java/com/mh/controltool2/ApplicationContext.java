package com.mh.controltool2;

import com.mh.controltool2.exceptions.invoke.BeanInstantiationException;

/*
* Global value
* */
public interface ApplicationContext {

    /*
    * If get bean is uncreated object will be try create this object
    *
    * */
    <T> T tryGetBean(Class<T> tClass) throws BeanInstantiationException;

    <T> T tryGetBean(Class<T> tClass,Class<? extends T> tClassImpl) throws BeanInstantiationException;

    <T> T getBean(Class<T> tClass);

    Object getBean(String name);

    void addBean(Class<?> tClass,Object obj);

    void addBean(String name,Object obj);

    void addBean(Object obj);



}
