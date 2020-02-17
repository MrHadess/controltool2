package com.mh.controltool2.scan.annotation;

import com.mh.controltool2.annotation.Bean;
import com.mh.controltool2.scan.PackageProcessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HandlerBean implements PackageProcessHandler {

    private static Logger logger = LoggerFactory.getLogger("HandlerBean");

    private Map<String,Class<?>> scanBeanMap = new HashMap<>();

    @Override
    public void loadFullPackageData(List<Class<?>> classList) {
        for (Class<?> item:classList) {
            Bean beanAnnotation = item.getAnnotation(Bean.class);
            if (beanAnnotation == null) continue;

            if (item.isInterface()) {
                logger.info(String.format("Bean is interface -> %s",item.getName()));
                continue;
            }

            if ("".equals(beanAnnotation.value())) {
                scanBeanMap.put(item.getName(),item);
            } else {
                scanBeanMap.put(beanAnnotation.value(),item);
            }
        }

    }

    public Map<String, Class<?>> getScanBeanMap() {
        return scanBeanMap;
    }
}
