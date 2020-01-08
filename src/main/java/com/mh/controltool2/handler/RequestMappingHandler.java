package com.mh.controltool2.handler;

import com.google.gson.Gson;
import com.mh.controltool2.ApplicationContext;
import com.mh.controltool2.context.FullApplicationContext;
import com.mh.controltool2.exceptions.invoke.ParamDataIsEmptyException;
import com.mh.controltool2.exceptions.invoke.UnsupportedSerializeObjectException;
import com.mh.controltool2.handler.pojo.RequestMatchInfo;
import com.mh.controltool2.method.MethodInvokeInfo;
import com.mh.controltool2.method.URLInvokeTree;
import com.mh.controltool2.method.type.*;
import com.mh.controltool2.serialize.BaseDataTypeChange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestMappingHandler {

    private ApplicationContext applicationContext;
    private HashMap<String,URLInvokeTree> urlAbsolutelyMap;
    private HashMap<Pattern,URLInvokeTree> urlFuzzyMap;

    private Gson json = new Gson();

    public RequestMappingHandler(
            ApplicationContext applicationContext,
            HashMap<String, URLInvokeTree> urlAbsolutelyMap,
            HashMap<Pattern, URLInvokeTree> urlFuzzyMap
    ) {
        this.applicationContext = applicationContext;
        this.urlAbsolutelyMap = urlAbsolutelyMap;
        this.urlFuzzyMap = urlFuzzyMap;
    }

    protected RequestMatchInfo requestMatchMethodInvokeInfo() {

        HttpServletRequest request = RequestContextHolder.getHttpServletRequest();
        String reqPathInfo = request.getPathInfo();
        String reqMethod = request.getMethod();

        URLInvokeTree urlInvokeTree = null;
        urlInvokeTree = urlAbsolutelyMap.get(reqPathInfo);
        if (urlInvokeTree != null) {
            return RequestMatchInfo.CreateRequestMatchAbsolutely(
                    reqPathInfo,
                    urlInvokeTree.getMatchInvokeObject(reqMethod)
            );
        }

        for (Map.Entry<Pattern, URLInvokeTree> item : urlFuzzyMap.entrySet()) {
            Matcher matcher = item.getKey().matcher(reqPathInfo);
            if (!matcher.matches()) continue;
            return RequestMatchInfo.CreateRequestMatchFuzzy(
                    reqPathInfo,
                    matcher,
                    item.getValue().getMatchInvokeObject(reqMethod)
            );
        }

        return null;
    }

    protected Object request(RequestMatchInfo requestMatchInfo) throws Exception {

        HttpServletRequest request = RequestContextHolder.getHttpServletRequest();

        MethodInvokeInfo methodInvokeInfo = requestMatchInfo.getMethodInvokeInfo();
        InvokeObjectInfo[] invokeObjectInfoGroup = requestMatchInfo.getMethodInvokeInfo().getInvokeObjectInfoGroup();
//        String[] methodParam = new String[invokeObjectInfoGroup.length];
        Object[] methodParamObject = new Object[invokeObjectInfoGroup.length];

        for (int i = 0; i < invokeObjectInfoGroup.length; i++) {
            // math invoke method
//            methodParam[i] = invokeObjectInfoGroup[i].getArgClass().getName();

            switch (invokeObjectInfoGroup[i].getHandlerType()) {
                case RequestHeader:
                    methodParamObject[i] = paramDataToRequestHeader((InvokeRequestHeader)invokeObjectInfoGroup[i],request);
                    break;
                case RequestBody:
                    methodParamObject[i] = paramDataToRequestBody((InvokeRequestBody)invokeObjectInfoGroup[i],request);
                    break;
                case RequestParam:
                    methodParamObject[i] = paramDataToRequestParam((InvokeRequestParam)invokeObjectInfoGroup[i],request);
                    break;
                case PathVariable:
                    methodParamObject[i] = paramDataToPathVariable((InvokePathVariable)invokeObjectInfoGroup[i],requestMatchInfo);
                    break;
                case SupportDefaultValue:
                    methodParamObject[i] = paramDataToPathVariable((InvokeDefaultValue)invokeObjectInfoGroup[i]);
                    break;
                case Unmatched:
                default:
                    methodParamObject[i] = null;
                    break;
            }
        }


        return methodInvokeInfo.getMethodName().invoke(
                applicationContext.tryGetBean(methodInvokeInfo.getClassname()),
                methodParamObject
        );
    }

    private Object paramDataToRequestHeader(InvokeRequestHeader invokeRequestHeader,HttpServletRequest request) throws UnsupportedSerializeObjectException,ParamDataIsEmptyException,NumberFormatException {
        String data = request.getHeader(invokeRequestHeader.getParamKey());
        return BaseDataTypeChange.stringToBaseData(invokeRequestHeader.getArgClass().getName(),data);
    }

    private Object paramDataToRequestParam(InvokeRequestParam invokeRequestParam, HttpServletRequest request) throws UnsupportedSerializeObjectException,ParamDataIsEmptyException,NumberFormatException {
        String data = request.getParameter(invokeRequestParam.getParamKey());
        return BaseDataTypeChange.stringToBaseData(invokeRequestParam.getArgClass().getName(),data);
    }

    private Object paramDataToRequestBody(InvokeRequestBody invokeRequestBody, HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(request.getInputStream(),"UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String temporaryLineContent;
        while ((temporaryLineContent = bufferedReader.readLine()) != null){
            sb.append(temporaryLineContent);
        }

        return json.fromJson(sb.toString(),invokeRequestBody.getParameterizedType());
    }

    private Object paramDataToPathVariable(InvokePathVariable invokePathVariable, RequestMatchInfo requestMatchInfo) throws UnsupportedSerializeObjectException,ParamDataIsEmptyException,NumberFormatException {
        String data = requestMatchInfo.getMatcher().group(invokePathVariable.getMatchLocalIndex() + 1);
        return BaseDataTypeChange.stringToBaseData(invokePathVariable.getArgClass().getName(),data);
    }

    private Object paramDataToPathVariable(InvokeDefaultValue invokeDefaultValue) {
        Class<?> tClass = invokeDefaultValue.getArgClass();
        if (HttpServletRequest.class.isAssignableFrom(tClass)) {
            return tClass.cast(RequestContextHolder.getHttpServletRequest());
        }

        if (HttpServletResponse.class.isAssignableFrom(tClass)) {
            return tClass.cast(RequestContextHolder.getHttpServletResponse());
        }

        return null;
    }



}
