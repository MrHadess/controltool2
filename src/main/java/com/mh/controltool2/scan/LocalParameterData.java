package com.mh.controltool2.scan;

import java.util.HashMap;

public class LocalParameterData {

    private HashMap<String,InvokeMsg> absolutelyMap;
    private HashMap<String,InvokeMsg> fuzzyMap;

    public LocalParameterData(HashMap<String, InvokeMsg> absolutelyMap, HashMap<String, InvokeMsg> fuzzyMap) {
        this.absolutelyMap = absolutelyMap;
        this.fuzzyMap = fuzzyMap;
    }

    public HashMap<String, InvokeMsg> getAbsolutelyMap() {
        return absolutelyMap;
    }

    public HashMap<String, InvokeMsg> getFuzzyMap() {
        return fuzzyMap;
    }
}
