package com.mh.controltool2.scan.annotation;

import com.mh.controltool2.annotation.Configuration;
import com.mh.controltool2.config.LoadConfigurer;
import com.mh.controltool2.config.annotation.Configurer2;
import com.mh.controltool2.scan.PackageProcessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.List;


public class HandlerConfig extends LoadConfigurer implements PackageProcessHandler {

    private static Logger logger = LoggerFactory.getLogger("HandlerConfig");

    @Override
    public void loadFullPackageData(List<Class<?>> classList) {

        for (Class<?> item:classList) {
            if (!item.isAnnotationPresent(Configuration.class)) {
                continue;
            }

            Configuration annotationConfiguration = item.getAnnotation(Configuration.class);
            if (annotationConfiguration == null) continue;

            // Create config Configurer
            if (!Configurer2.class.isAssignableFrom(item)) {
                logger.warn("Class unmatched 'Configurer2' => " + item.getName());
                continue;
            }

            boolean hasNullConstructor = false;
            for (Constructor<?> constructorItem:item.getConstructors()) {
                if (constructorItem.getParameterCount() <= 0) {
                    hasNullConstructor = true;
                    break;
                }
            }
            if (!hasNullConstructor) {
                logger.error("Class has no nullify constructor ==>" + item.getName());
            }

            logger.info(String.format("Use config:%s",item.getName()));

            try {
                Configurer2 configurer = (Configurer2) item.newInstance();
                load(configurer);
                return;
            } catch (InstantiationException e) {
                logger.error("Unable empty constructor",e.getCause());
            } catch (IllegalAccessException e) {
                logger.error("Method access permission",e);
            }
        }


    }

    @Override
    public String toString() {
        return "HandlerConfig{" +
                "mappedInterceptorList=" + mappedInterceptorList +
                ", beanMap=" + beanMap +
                ", dataObjectSerialize=" + dataObjectSerialize +
                ", httpMessageRewrite=" + httpMessageRewrite +
                ", exceptionHandler=" + exceptionHandler +
                '}';
    }
}
