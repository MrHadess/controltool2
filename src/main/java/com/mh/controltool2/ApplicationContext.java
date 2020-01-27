package com.mh.controltool2;

import com.mh.controltool2.exceptions.BeanFactoryException;
import com.mh.controltool2.exceptions.invoke.BeanInstantiationException;

/*
* Global value
* */
public interface ApplicationContext {

    /*
    * If get bean is uncreated object will be try create this object
    *
    * */
    <T> T tryGetBean(Class<T> tClass) throws BeanFactoryException,BeanInstantiationException;

    <T> T tryGetBean(Class<T> tClass,Class<? extends T> tClassImpl) throws BeanFactoryException,BeanInstantiationException;

    Object tryGetBean(String name,Class<?> tClassImpl) throws BeanFactoryException,BeanInstantiationException;

    <T> T getBean(Class<T> tClass);

    Object getBean(String name);

    /*
    * 'addBean' reject same bean register
    * */

    void addBean(Class<?> tClass,Object obj) throws BeanFactoryException;

    void addBean(String name,Object obj) throws BeanFactoryException;

    void addBean(Object obj) throws BeanFactoryException;



}
