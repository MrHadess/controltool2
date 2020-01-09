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
                    oldMethodInvokeInfo.getClassname().getName(),
                    oldMethodInvokeInfo.getMethodName().getName(),
                    methodInvokeInfo.getClassname().getName(),
                    methodInvokeInfo.getMethodName().getName()
            );
        }

        matchMethodNum++;
        requestMethodInvokeInfoMap.put(requestMethod,methodInvokeInfo);

        if (matchMethodNum == 1) {
            matchInvokeObject = new MatchInvokeObject() {
                @Override
                public MethodInvokeInfo getMatchInvokeObject(String trueRequestMethod) {
                    if (requestMethod == RequestMethod.Full) return methodInvokeInfo;
                    RequestMethod clientReqMethod = null;
                    switch (trueRequestMethod) {
                        case "GET":
                            clientReqMethod = RequestMethod.GET;
                            break;
                        case "HEAD":
                            clientReqMethod = RequestMethod.HEAD;
                            break;
                        case "POST":
                            clientReqMethod = RequestMethod.POST;
                            break;
                        case "PUT":
                            clientReqMethod = RequestMethod.PUT;
                            break;
                        case "DELETE":
                            clientReqMethod = RequestMethod.DELETE;
                            break;
                        case "OPTIONS":
                            clientReqMethod = RequestMethod.OPTIONS;
                            break;
                        case "TRACE":
                            clientReqMethod = RequestMethod.TRACE;
                            break;
                    }
                    return  requestMethod == clientReqMethod ? methodInvokeInfo : null;
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
