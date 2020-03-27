package com.mh.controltool2.method;

import com.mh.controltool2.annotation.RequestMethod;
import com.mh.controltool2.exceptions.RepeatURLMethodException;

import java.util.HashMap;

public class URLInvokeTreeV2 implements MatchInvokeObject {

//    Match request method index
    private static final int REQUEST_METHOD_FULL = 0;
    private static final int REQUEST_METHOD_GET = 1;
    private static final int REQUEST_METHOD_POST = 2;
    private static final int REQUEST_METHOD_PUT = 3;
    private static final int REQUEST_METHOD_HEAD = 4;
    private static final int REQUEST_METHOD_PATCH = 5;
    private static final int REQUEST_METHOD_DELETE = 6;
    private static final int REQUEST_METHOD_OPTIONS = 7;
    private static final int REQUEST_METHOD_TRACE = 8;

    private int matchMethodNum = 0;
    private RequestMethodMatchInvokeObject matchInvokeObject;
    private MethodInvokeInfo[] methodInvokeInfoArray = new MethodInvokeInfo[9];


    @Override
    public MethodInvokeInfo getMatchInvokeObject(String requestMethod) {
        return matchInvokeObject.getMatchInvokeObject(requestMethod);
    }

    @Override
    public void addInvokeInfo(MethodInvokeInfo methodInvokeInfo, final RequestMethod requestMethod) throws RepeatURLMethodException {

        int matchPoint = programRequestMethodToPoint(requestMethod);
        if (matchPoint < 0) {
            throw new NullPointerException("Unsupported request method");
        }
        if (methodInvokeInfoArray[matchPoint] != null) {
            MethodInvokeInfo oldMethodInvokeInfo = methodInvokeInfoArray[matchPoint];
            throw new RepeatURLMethodException(
                    oldMethodInvokeInfo.getTargetClass().getName(),
                    oldMethodInvokeInfo.getTargetMethod().getName(),
                    methodInvokeInfo.getTargetClass().getName(),
                    methodInvokeInfo.getTargetMethod().getName()
            );
        }

        matchMethodNum++;
        methodInvokeInfoArray[matchPoint] = methodInvokeInfo;

        if (matchMethodNum == 1) {
            matchInvokeObject = new SingleURLMatch(requestMethod);
        } else if (matchInvokeObject != multipleInvokeObject) {
            matchInvokeObject = multipleInvokeObject;
        }
    }

    public int getMatchMethodNum() {
        return matchMethodNum;
    }

    private RequestMethodMatchInvokeObject multipleInvokeObject = new RequestMethodMatchInvokeObject() {
        @Override
        public MethodInvokeInfo getMatchInvokeObject(String requestMethod) {
            MethodInvokeInfo matchInvokeMethod = null;

            // first match child method
            int matchIndex = requestMethodToPoint(requestMethod);
            matchInvokeMethod = methodInvokeInfoArray[matchIndex];

            if (matchInvokeMethod != null) {
                return matchInvokeMethod;
            }
            // last match full match
            return methodInvokeInfoArray[REQUEST_METHOD_FULL];
        }
    };

//    class MultipleInvokeObject

    class SingleURLMatch implements RequestMethodMatchInvokeObject {

        private int requestMatchIndex;

        SingleURLMatch(RequestMethod requestMethod) {
            this.requestMatchIndex = programRequestMethodToPoint(requestMethod);
        }

        @Override
        public MethodInvokeInfo getMatchInvokeObject(String requestMethod) {
            if (requestMatchIndex == REQUEST_METHOD_FULL || requestMatchIndex == requestMethodToPoint(requestMethod)) {
                return methodInvokeInfoArray[requestMatchIndex];
            }

            return null;
        }
    }

    private static int programRequestMethodToPoint(RequestMethod requestMethod) {
        switch (requestMethod) {
            case Full:
                return REQUEST_METHOD_FULL;
            case GET:
                return REQUEST_METHOD_GET;
            case POST:
                return REQUEST_METHOD_POST;
            case PUT:
                return REQUEST_METHOD_PUT;
            case HEAD:
                return REQUEST_METHOD_HEAD;
            case PATCH:
                return REQUEST_METHOD_PATCH;
            case DELETE:
                return REQUEST_METHOD_DELETE;
            case OPTIONS:
                return REQUEST_METHOD_OPTIONS;
            case TRACE:
                return REQUEST_METHOD_TRACE;
        }
        return -1;
    }

    private static int requestMethodToPoint(String requestMethod) {
        switch (requestMethod) {
            case "GET":
                return REQUEST_METHOD_GET;
            case "POST":
                return REQUEST_METHOD_POST;
            case "PUT":
                return REQUEST_METHOD_PUT;
            case "HEAD":
                return REQUEST_METHOD_HEAD;
            case "PATCH":
                return REQUEST_METHOD_PATCH;
            case "DELETE":
                return REQUEST_METHOD_DELETE;
            case "OPTIONS":
                return REQUEST_METHOD_OPTIONS;
            case "TRACE":
                return REQUEST_METHOD_TRACE;
        }
        return -1;
    }

}
