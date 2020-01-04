package com.mh.controltool2;

import javax.servlet.http.HttpServletRequest;


public class HttpRequestType {

    public static void requestHandlerMethod(HttpServletRequest req, HandlerMethod impl){
        RequestType requestType = RequestType.getType(req.getMethod());
        switch (requestType) {
            case Get:
                impl.doGet();
                break;
            case Head:
                impl.doHead();
            case Post:
                impl.doPost();
                break;
            case Put:
                impl.doPut();
                break;
            case Delete:
                impl.doDelete();
                break;
            case Options:
                impl.doOptions();
                break;
            case Trace:
                impl.doTrace();
                break;
            case UnKnow:
            default:
                impl.unKnown();
                break;
        }
    }

    public static RequestType requestHandlerMethod(HttpServletRequest req){
        return RequestType.getType(req.getMethod());
    }

    public enum RequestType{
        Get("GET"),Head("HEAD"),Post("POST"),Put("PUT"),Delete("DELETE"),Options("OPTIONS"),Trace("TRACE"),UnKnow("");

        String s;
        RequestType(String type){
            s = type;
        }

        public static RequestType getType(String nowType){
            if (nowType == null) return null;
            for (RequestType item:values()){
                if (item.s.equals(nowType)) return item;
            }

            return UnKnow;
        }
    }

    public interface HandlerMethod {
        void doGet();

        void doHead();

        void doPost();

        void doPut();

        void doDelete();

        void doOptions();

        void doTrace();

        void unKnown();
    }
}
