package com.mh.controltool2.context;

import com.mh.controltool2.ApplicationContext;
import com.mh.controltool2.annotation.Autowired;
import com.mh.controltool2.annotation.Bean;
import com.mh.controltool2.exceptions.BeanFactoryException;
import com.mh.controltool2.exceptions.invoke.BeanInstantiationException;
import com.mh.controltool2.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/*
* Global value
* */
public class FullApplicationContext implements ApplicationContext {

    private static final String PACKAGE_NAME_LANG = "java.lang";

    private final Map<String,Object> objectMap = new ConcurrentHashMap<>();
    private final Set<String> creatingClassSet = new ConcurrentSkipListSet<>();

    @Override
    public <T> T tryGetBean(Class<T> tClass) throws BeanFactoryException,BeanInstantiationException {
        Object obj = objectMap.get(tClass.getName());
        if (obj != null) {
            return tClass.cast(obj);
        }
        // try create obj
        return createObj(tClass,tClass);
    }

    @Override
    public <T> T tryGetBean(Class<T> tClass, Class<? extends T> tClassImpl) throws BeanFactoryException,BeanInstantiationException {
        Object obj = objectMap.get(tClass.getName());
        if (obj != null) {
            return tClass.cast(obj);
        }
        // try create obj
        return createObj(tClass,tClassImpl);
    }

    @Override
    public Object tryGetBean(String name, Class<?> tClassImpl) throws BeanFactoryException, BeanInstantiationException {
        Object obj = objectMap.get(name);
        if (obj != null) {
            return tClassImpl.cast(obj);
        }
        // try create obj
        return absoluteCreateObj(name,tClassImpl);
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
    public void addBean(Class<?> tClass, Object obj) throws BeanFactoryException {
        Assert.notNull(tClass,"Target class must not be null");
        Assert.notNull(obj,"Object must not be null");
        synchronized (this.objectMap) {
            if (objectMap.containsKey(tClass.getName())) {
                throw new BeanFactoryException(BeanFactoryException.ExpType.ReplaceBean);
            }
            objectMap.put(tClass.getName(),obj);
        }
    }

    @Override
    public void addBean(String name, Object obj) throws BeanFactoryException {
        Assert.notNull(name,"Bean name must not be null");
        Assert.notNull(obj,"Object must not be null");
        synchronized (this.objectMap) {
            if (objectMap.containsKey(name)) {
                throw new BeanFactoryException(BeanFactoryException.ExpType.ReplaceBean);
            }
            objectMap.put(name,obj);
        }
    }

    @Override
    public void addBean(Object obj) throws BeanFactoryException {
        String beanName= obj.getClass().getName();
        if (PACKAGE_NAME_LANG.startsWith(beanName)) {
            throw new BeanFactoryException(BeanFactoryException.ExpType.UnsupportedDataType,beanName);
        }
        addBean(beanName,obj);
    }

    private synchronized <T> T createObjOld(Class<T> tClass, Class<? extends T> tClassImpl) throws BeanInstantiationException {
        // just support public constructors
        Constructor[] constructors = tClassImpl.getConstructors();
        for (Constructor item:constructors) {
            if (item.getParameterCount() <= 0) {
                return createEmptyConstructorObj(tClass,tClassImpl);
            }
        }
        throw new BeanInstantiationException(tClass.getName());
    }

    private synchronized <T> T createObj(Class<T> tClass, Class<? extends T> tClassImpl) throws BeanFactoryException,BeanInstantiationException {
        if (!tClass.isAssignableFrom(tClassImpl)) {
            throw new BeanFactoryException(
                    BeanFactoryException.ExpType.NotAssignableFromClass,
                    String.format("Target class:'%s',class impl name:'%s'",tClass.getName(),tClassImpl.getName())
            );
        }
        return tClass.cast(createObj(tClass.getName(),tClassImpl));
    }

    private synchronized Object createObj(String beanName, Class<?> tClassImpl) throws BeanFactoryException,BeanInstantiationException {
        try {
            return absoluteCreateObj(beanName,tClassImpl);
        } finally {
            creatingClassSet.remove(tClassImpl.getName());
        }
    }

    private synchronized Object absoluteCreateObj(String beanName, Class<?> tClassImpl) throws BeanFactoryException,BeanInstantiationException {
        // First check public constructors has empty or has 'Autowired' annotation
        if (tClassImpl.isInterface()) {
            throw new BeanFactoryException(BeanFactoryException.ExpType.IsInterface,tClassImpl.getName());
        }
        if (PACKAGE_NAME_LANG.startsWith(tClassImpl.getName())) {
            throw new BeanFactoryException(BeanFactoryException.ExpType.UnsupportedDataType,tClassImpl.getName());
        }
        if (creatingClassSet.contains(tClassImpl.getName())) {
            throw new BeanFactoryException(BeanFactoryException.ExpType.CurrentlyInCreation,tClassImpl.getName());
        }

        creatingClassSet.add(tClassImpl.getName());

        boolean hasEmptyConstructors = false;
        Constructor[] constructors = tClassImpl.getConstructors();
        List<Constructor> supportAutowiredConstructorList = new ArrayList<>();
        for (Constructor item:constructors) {
            if (item.getParameterCount() <= 0) {
                hasEmptyConstructors = true;
                continue;
            }
            if (item.getAnnotation(Autowired.class) != null) {
                supportAutowiredConstructorList.add(item);
            }
        }

        // Auto chose first class constructors init object
        if (supportAutowiredConstructorList.isEmpty()) {
            // Go to create empty constructor
            return createEmptyConstructorObj(beanName,tClassImpl);
        }
        // Next try create has 'autowired' annotation constructors (Is random mode)
        Object[] paramTypeObject;
        for (Constructor item:supportAutowiredConstructorList) {
            // If 'autowired' annotation has bean name ,first use it to bean name
            try {
                paramTypeObject = loadParamObjects(item);
            } catch (BeanFactoryException e) {
                switch (e.getType()) {
                    case IsInterface:
                    case UnsupportedDataType:
                        continue;
                        // If unsupported type use next init object
                    default:
                        throw e;
                }
            }

            try {
                synchronized (this.objectMap) {
                    Object obj = item.newInstance(paramTypeObject);;
                    objectMap.put(beanName,obj);
                    return obj;
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new BeanInstantiationException(e);
            }

        }

        // If nothing init object,then try use empty constructor
        if (hasEmptyConstructors) {
            return createEmptyConstructorObj(beanName,tClassImpl);
        }

        throw new BeanInstantiationException(beanName);
    }

    private Object[] loadParamObjects(Constructor constructor) throws BeanFactoryException {
        TypeVariable[] itemTypeParameters = constructor.getTypeParameters();
        Class<?>[] itemParameterTypes = constructor.getParameterTypes();
        Object[] paramTypeObject = new Object[itemParameterTypes.length];
        for (int index = 0;index < itemParameterTypes.length;index++) {
//            Autowired autowiredAnnotation = itemTypeParameters[index].getAnnotation(Autowired.class);
//            if (autowiredAnnotation != null) {
//                paramTypeObject[index] = tryGetBean(itemParameterTypes[index]);
//            } else {
//                // input null
//                paramTypeObject[index] = null;
//            }

            Autowired autowiredAnnotation = itemTypeParameters[index].getAnnotation(Autowired.class);
            if (autowiredAnnotation != null) {
                paramTypeObject[index] = tryGetBean(itemParameterTypes[index]);
                continue;
            }

            Bean beanAnnotation = itemTypeParameters[index].getAnnotation(Bean.class);
            if (beanAnnotation != null && "".equals(beanAnnotation.name())) {
                // input null
                paramTypeObject[index] = tryGetBean(beanAnnotation.name(),itemParameterTypes[index]);
                continue;
            }

            paramTypeObject[index] = null;
        }
        return paramTypeObject;
    }

    private synchronized <T> T createEmptyConstructorObj(Class<T> tClass, Class<? extends T> tClassImpl) throws BeanFactoryException,BeanInstantiationException {
        try {
            synchronized (this.objectMap) {
                T obj = tClassImpl.newInstance();
                objectMap.put(tClass.getName(),obj);
                return obj;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }
    }

    private synchronized <T> T createEmptyConstructorObj(String beanName, Class<? extends T> tClassImpl) throws BeanFactoryException,BeanInstantiationException {
        try {
            synchronized (this.objectMap) {
                T obj = tClassImpl.newInstance();
                objectMap.put(beanName,obj);
                return obj;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }
    }

}
