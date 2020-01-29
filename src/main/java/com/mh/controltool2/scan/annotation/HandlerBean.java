package com.mh.controltool2.scan.annotation;

import com.mh.controltool2.LogOut;
import com.mh.controltool2.annotation.Bean;
import com.mh.controltool2.scan.PackageProcessHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HandlerBean implements PackageProcessHandler {

    private static final String TAG = "HandlerBean";

    private Map<String,Class<?>> scanBeanMap = new HashMap<>();

    @Override
    public void loadFullPackageData(List<Class<?>> classList) {
        for (Class<?> item:classList) {
            Bean beanAnnotation = item.getAnnotation(Bean.class);
            if (beanAnnotation == null) continue;

            if (item.isInterface()) {
                LogOut.i(TAG,String.format("Bean is interface -> %s",item.getName()));
                continue;
            }

            if ("".equals(beanAnnotation.name())) {
                scanBeanMap.put(item.getName(),item);
            } else {
                scanBeanMap.put(beanAnnotation.name(),item);
            }
        }

    }

    public Map<String, Class<?>> getScanBeanMap() {
        return scanBeanMap;
    }
}
