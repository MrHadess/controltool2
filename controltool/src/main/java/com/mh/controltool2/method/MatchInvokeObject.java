package com.mh.controltool2.method;

import com.mh.controltool2.annotation.RequestMethod;
import com.mh.controltool2.exceptions.RepeatURLMethodException;

/*
* Use request method match invoke object
*
* */
public interface MatchInvokeObject {

    void addInvokeInfo(MethodInvokeInfo methodInvokeInfo, final RequestMethod requestMethod) throws RepeatURLMethodException;

    MethodInvokeInfo getMatchInvokeObject(String requestMethod);

}
