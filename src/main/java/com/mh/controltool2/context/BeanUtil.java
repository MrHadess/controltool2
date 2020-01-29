package com.mh.controltool2.context;

import com.mh.controltool2.ApplicationContext;
import com.mh.controltool2.LogOut;
import com.mh.controltool2.exceptions.BeanFactoryException;

import java.util.Map;

public class BeanUtil {

    private static final String TAG = "BeanUtil";

    public static void tryBeanMapToContext(ApplicationContext applicationContext, Map<String,Object> beanMap) {
        for (Map.Entry<String,Object> item:beanMap.entrySet()) {
            try {
                applicationContext.addBean(item.getKey(),item.getValue());
            } catch (BeanFactoryException e) {
                if (e.getType() == BeanFactoryException.ExpType.ReplaceBean) {
                    LogOut.i(TAG, String.format("Replace bean,unable add bean to context (BeanName:'%s')",item.getKey()));
                } else {
                    throw e;
                }
            }
        }

    }

    public static void tryLoadBeansToContext(ApplicationContext applicationContext, Map<String,Class<?>> beanMap) {
        for (Map.Entry<String,Class<?>> item:beanMap.entrySet()) {
            try {
                applicationContext.tryGetBean(item.getKey(),item.getValue());
            } catch (BeanFactoryException e) {
                if (e.getType() == BeanFactoryException.ExpType.ReplaceBean) {
                    LogOut.i(TAG, String.format("Replace bean,unable add bean to context (BeanName:'%s')",item.getKey()));
                } else {
                    throw e;
                }
            }
        }

    }

}
