package com.mh.controltool2.scan;

import java.util.ArrayList;
import java.util.List;

public class CenterPackageProcessHandler {

    private List<String> packageList;
    private List<PackageProcessHandler> packageProcessHandlerList = new ArrayList<>();

    public CenterPackageProcessHandler(List<String> packageList) {
        this.packageList = packageList;
    }

    public void addPackagesScan(PackageProcessHandler packageProcessHandler) {
        packageProcessHandlerList.add(packageProcessHandler);
    }

    public void startProcessHandler() {
        if (packageList == null || packageList.isEmpty()) return;

        List<Class<?>> scanPackageToClassGroup = PackageScan.scanPackages(packageList);
        for (PackageProcessHandler item:packageProcessHandlerList) {
            item.loadFullPackageData(scanPackageToClassGroup);
        }
    }

}
