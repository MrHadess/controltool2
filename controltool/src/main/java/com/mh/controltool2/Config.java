package com.mh.controltool2;

import com.mh.controltool2.scan.CenterPackageProcessHandler;
import com.mh.controltool2.scan.annotation.HandlerBean;
import com.mh.controltool2.scan.annotation.HandlerConfig;
import com.mh.controltool2.scan.annotation.HandlerControl;

import javax.servlet.ServletConfig;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private static final String DEFAULT_BEAN_PROPERTIES_FILE_NAME = "controltool.properties";

    private CenterPackageProcessHandler centerPackageProcessHandler;
    private String beanPropertiesFileName;

    private HandlerBean handlerBean = new HandlerBean();
    private HandlerControl handlerControl = new HandlerControl();
    private HandlerConfig handlerConfig = new HandlerConfig();


    /*
    * first support annotation
    * first not support file config
    *
    * default load file 'control_tool.properties'
    *
    * */

    private Config(ServletConfig servletConfig) {
        beanPropertiesFileName = servletConfig.getInitParameter("BeanProperties");
        if (beanPropertiesFileName == null) {
            beanPropertiesFileName = DEFAULT_BEAN_PROPERTIES_FILE_NAME;
        }

        String needScanPackages = servletConfig.getInitParameter("ScanPkg");
        String[] packages = needScanPackages.split(";");
        List<String> scanPackages = new ArrayList<String>();
        for (String aPkg:packages) {
            if (aPkg.isEmpty()) continue;
            scanPackages.add(aPkg);
        }

        centerPackageProcessHandler = new CenterPackageProcessHandler(scanPackages);
        centerPackageProcessHandler.addPackagesScan(handlerControl);
        centerPackageProcessHandler.addPackagesScan(handlerConfig);
        centerPackageProcessHandler.addPackagesScan(handlerBean);

        centerPackageProcessHandler.startProcessHandler();







    }

    protected static Config create(ServletConfig servletConfig) {
        return new Config(servletConfig);
    }

    public String getBeanPropertiesFileName() {
        return beanPropertiesFileName;
    }

    public HandlerControl getHandlerControl() {
        return handlerControl;
    }

    public HandlerConfig getHandlerConfig() {
        return handlerConfig;
    }

    public HandlerBean getHandlerBean() {
        return handlerBean;
    }
}
