package com.mh.controltool2.config;

import com.mh.controltool2.exceptions.BeanFactoryException;
import com.mh.controltool2.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanRegistry {

    private static final String PACKAGE_NAME_LANG = "java.lang";

    private final Map<String,Object> beanMap = new ConcurrentHashMap<>();

    public void registerBean(Object obj) throws BeanFactoryException {
        Assert.notNull(obj,"Target object must not be null");
        String beanName= obj.getClass().getName();
        if (PACKAGE_NAME_LANG.startsWith(beanName)) {
            throw new BeanFactoryException(BeanFactoryException.ExpType.UnsupportedDataType,beanName);
        }
        registerBean(beanName,obj);
    }

    public void registerBean(Class interfaceFormClass, Object obj) throws BeanFactoryException {
        Assert.notNull(interfaceFormClass, "Target object must not be null");
        if (beanMap.containsKey(interfaceFormClass.getName())) {
            throw new BeanFactoryException(BeanFactoryException.ExpType.ReplaceBean);
        }
        registerBean(interfaceFormClass.getName(), obj);

    }

    public void registerBean(String beanName, Object obj) throws BeanFactoryException {
        Assert.notNull(beanName,"Bean name must not be null");
        Assert.notNull(obj,"Object must not be null");
        synchronized (this.beanMap) {
            if (beanMap.containsKey(beanName)) {
                throw new BeanFactoryException(BeanFactoryException.ExpType.ReplaceBean);
            }
            beanMap.put(beanName,obj);
        }
    }


    /**
     * Return all bean
     */
    protected Map<String,Object> getBeanMap() {
        return this.beanMap;
    }



}
