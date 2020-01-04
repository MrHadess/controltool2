package com.mh.controltool2.method;

import com.mh.controltool2.annotation.RequestMethod;
import com.mh.controltool2.exceptions.RepeatURLMethodException;

import java.util.HashMap;

public class URLInvokeTree implements MatchInvokeObject {

    private int matchMethodNum = 0;
    private MatchInvokeObject matchInvokeObject;
    private HashMap<RequestMethod,MethodInvokeInfo> requestMethodInvokeInfoMap = new HashMap<RequestMethod, MethodInvokeInfo>();

    @Override
    public MethodInvokeInfo getMatchInvokeObject(String requestMethod) {
        return matchInvokeObject.getMatchInvokeObject(requestMethod);
    }

    public void addInvokeInfo(MethodInvokeInfo methodInvokeInfo, final RequestMethod requestMethod) throws RepeatURLMethodException {
        if (requestMethodInvokeInfoMap.containsKey(requestMethod)) {
            MethodInvokeInfo oldMethodInvokeInfo = requestMethodInvokeInfoMap.get(requestMethod);
            throw new RepeatURLMethodException(
                    oldMethodInvokeInfo.getClassname(),
                    oldMethodInvokeInfo.getMethodName(),
                    methodInvokeInfo.getClassname(),
                    methodInvokeInfo.getMethodName()
            );
        }

        matchMethodNum++;
        requestMethodInvokeInfoMap.put(requestMethod,methodInvokeInfo);

        if (matchMethodNum == 1) {
            matchInvokeObject = new MatchInvokeObject() {
                @Override
                public MethodInvokeInfo getMatchInvokeObject(String trueRequestMethod) {
                    return requestMethodInvokeInfoMap.get(requestMethod);
                }
            };
        } else if (matchInvokeObject != multipleInvokeObject) {
            matchInvokeObject = multipleInvokeObject;
        }
    }

    public int getMatchMethodNum() {
        return matchMethodNum;
    }

    private MatchInvokeObject multipleInvokeObject = new MatchInvokeObject() {
        @Override
        public MethodInvokeInfo getMatchInvokeObject(String requestMethod) {
            MethodInvokeInfo matchInvokeMethod = null;

            // first match child method
            switch (requestMethod) {
                case "GET":
                    matchInvokeMethod = requestMethodInvokeInfoMap.get(RequestMethod.GET);
                    break;
                case "HEAD":
                    matchInvokeMethod = requestMethodInvokeInfoMap.get(RequestMethod.HEAD);
                    break;
                case "POST":
                    matchInvokeMethod = requestMethodInvokeInfoMap.get(RequestMethod.POST);
                    break;
                case "PUT":
                    matchInvokeMethod = requestMethodInvokeInfoMap.get(RequestMethod.PUT);
                    break;
                case "DELETE":
                    matchInvokeMethod = requestMethodInvokeInfoMap.get(RequestMethod.DELETE);
                    break;
                case "OPTIONS":
                    matchInvokeMethod = requestMethodInvokeInfoMap.get(RequestMethod.OPTIONS);
                    break;
                case "TRACE":
                    matchInvokeMethod = requestMethodInvokeInfoMap.get(RequestMethod.TRACE);
                    break;
            }

            if (matchInvokeMethod != null) {
                return matchInvokeMethod;
            }
            // last match full match
            return requestMethodInvokeInfoMap.get(RequestMethod.Full);
        }
    };

}