package com.mh.controltool2.scan.annotation;

import com.mh.controltool2.LogOut;
import com.mh.controltool2.annotation.Configuration;
import com.mh.controltool2.annotation.InterceptorRegistration;
import com.mh.controltool2.annotation.InterceptorRegistry;
import com.mh.controltool2.config.annotation.Configurer;
import com.mh.controltool2.config.annotation.Configurer2;
import com.mh.controltool2.scan.PackageProcessHandler;
import com.mh.controltool2.servlet.HandlerInterceptor;

import java.lang.reflect.Constructor;
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
            if (!item.isAssignableFrom(Configurer2.class)) {
                LogOut.e("Unmatch");
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

            try {
                Configurer configurer = (Configurer) item.newInstance();

                createConfig((Class<Configurer2>) item);



                return;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }


    }


    private void createConfig(Class<Configurer2> tClass) throws IllegalAccessException, InstantiationException {
        Configurer2 configurer = tClass.newInstance();

        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
        configurer.addInterceptors(interceptorRegistry);

        List<InterceptorRegistration> interceptorRegistrationList = interceptorRegistry.getInterceptors();
        for (InterceptorRegistration item:interceptorRegistrationList) {

        }


    }


}
