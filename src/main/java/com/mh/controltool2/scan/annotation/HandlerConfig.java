package com.mh.controltool2.scan.annotation;

import com.mh.controltool2.LogOut;
import com.mh.controltool2.annotation.Configuration;
import com.mh.controltool2.config.annotation.Configurer;
import com.mh.controltool2.scan.PackageProcessHandler;
import com.mh.controltool2.servlet.HandlerInterceptor;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;

public class HandlerConfig implements PackageProcessHandler {

    private static final String TAG = "HandlerConfig";

    @Override
    public void loadFullPackageData(List<Class<?>> classList) {

        for (Class<?> item:classList) {
            if (!item.isAnnotationPresent(Configuration.class)) {
                continue;
            }

            Configuration annotationConfiguration = item.getAnnotation(Configuration.class);
            if (annotationConfiguration == null) continue;

            // Create config Configurer
            if (!item.isAssignableFrom(Configurer.class)) {
                LogOut.e("Unmatch");
                continue;
            }

            if (item.getConstructors().length > 0) {
                LogOut.e(TAG,"Unknown empty constructors ==>" + item.getName());
            }

            try {
                Configurer configurer = (Configurer) item.newInstance();







            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return;
        }


    }
}
