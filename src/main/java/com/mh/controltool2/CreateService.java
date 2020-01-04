package com.mh.controltool2;

import com.mh.controltool2.scan.CenterPackageProcessHandler;
import com.mh.controltool2.scan.PackageScan;
import com.mh.controltool2.scan.annotation.HandlerConfig;
import com.mh.controltool2.scan.annotation.HandlerControl;

import javax.servlet.ServletConfig;
import java.util.ArrayList;
import java.util.List;

public class CreateService {

    private CenterPackageProcessHandler centerPackageProcessHandler;

    private HandlerControl handlerControl = new HandlerControl();
    private HandlerConfig handlerConfig = new HandlerConfig();


    /*
    * first support annotation
    * first not support file config
    *
    * default load file 'control_tool.properties'
    *
    * */
    @Deprecated
    public CreateService() {

    }

    private  CreateService(ServletConfig servletConfig) {
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

        centerPackageProcessHandler.startProcessHandler();








    }

}
