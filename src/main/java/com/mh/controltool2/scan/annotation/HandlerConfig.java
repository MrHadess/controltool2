package com.mh.controltool2.scan.annotation;

import com.mh.controltool2.LogOut;
import com.mh.controltool2.annotation.Configuration;
import com.mh.controltool2.scan.PackageProcessHandler;
import com.mh.controltool2.servlet.HandlerInterceptor;

import java.util.List;

public class HandlerConfig implements PackageProcessHandler {
    @Override
    public void loadFullPackageData(List<Class<?>> classList) {

        for (Class<?> item:classList) {
            Configuration annotationConfiguration = item.getAnnotation(Configuration.class);
            if (annotationConfiguration == null) continue;

            // Create config Configurer
            if (!item.isAssignableFrom(HandlerInterceptor.class)) {
                LogOut.e("Unmatch");
                continue;
            }

//            ControlAnnotation.RestController restControllerModulClass = item.getAnnotation(ControlAnnotation.RestController.class);
//            //当此类不存在控制器时 不加入URL匹配Map中
//            if (restControllerModulClass == null) continue;
//            LogOut.i("-----------------------------------------------------------");
//            LogOut.i("This has RestController:::" + item);
//
//            String className = item.getName();
//
//            //控制器根Mapping前缀匹配
//            String urlFirstMatch = "";
//            ControlAnnotation.RequestMapping requestMappingClass = item.getAnnotation(ControlAnnotation.RequestMapping.class);
//            if (requestMappingClass != null) {
//                String value = requestMappingClass.value().trim();
//                LogOut.i("This has RequestMapping:::" + item + "  value=" + requestMappingClass.value());
//                if (!value.isEmpty()) {
//                    urlFirstMatch = value;
//                }
//            }
//
//            //Method check
//            methodCheck(item,urlFirstMatch,className);
        }


    }
}
