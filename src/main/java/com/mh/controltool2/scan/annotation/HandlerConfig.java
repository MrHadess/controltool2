package com.mh.controltool2.scan.annotation;

import com.mh.controltool2.LogOut;
import com.mh.controltool2.annotation.Configuration;
import com.mh.controltool2.config.LoadConfigurer;
import com.mh.controltool2.config.MappedInterceptor;
import com.mh.controltool2.config.annotation.Configurer2;
import com.mh.controltool2.handler.messagerewrite.HttpMessageRewrite;
import com.mh.controltool2.scan.PackageProcessHandler;
import com.mh.controltool2.serialize.json.DataObjectSerialize;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerConfig implements PackageProcessHandler {

    private static final String TAG = "HandlerConfig";

    private List<MappedInterceptor> mappedInterceptorList = new ArrayList<>();
    private Map<String,Object> beanMap = new HashMap<>();
    private DataObjectSerialize dataObjectSerialize;
    private HttpMessageRewrite httpMessageRewrite;

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
                LogOut.e(TAG,"Class unmatched 'Configurer2' => " + item.getName());
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
                LogOut.e(TAG,"Class has no nullify constructor ==>" + item.getName());
            }

            LogOut.i(TAG,String.format("Use config:%s",item.getName()));

            try {
                Configurer2 configurer = (Configurer2) item.newInstance();

                LoadConfigurer loadConfigurer = new LoadConfigurer(configurer);
                mappedInterceptorList = loadConfigurer.getMappedInterceptorList();
                beanMap = loadConfigurer.getBeanMap();
                dataObjectSerialize = loadConfigurer.getDataObjectSerialize();
                httpMessageRewrite = loadConfigurer.getHttpMessageRewrite();

                return;
            } catch (InstantiationException e) {
                e.getCause().printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


    }

    public List<MappedInterceptor> getMappedInterceptorList() {
        return mappedInterceptorList;
    }

    public Map<String, Object> getBeanMap() {
        return beanMap;
    }

    public DataObjectSerialize getDataObjectSerialize() {
        return dataObjectSerialize;
    }

    public HttpMessageRewrite getHttpMessageRewrite() {
        return httpMessageRewrite;
    }

    @Override
    public String toString() {
        return "HandlerConfig{" +
                "mappedInterceptorList=" + mappedInterceptorList +
                ", beanMap=" + beanMap +
                ", dataObjectSerialize=" + dataObjectSerialize +
                ", httpMessageRewrite=" + httpMessageRewrite +
                '}';
    }
}
