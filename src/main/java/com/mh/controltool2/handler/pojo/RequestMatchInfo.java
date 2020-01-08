package com.mh.controltool2.handler.pojo;

import com.mh.controltool2.method.MethodInvokeInfo;

import java.util.regex.Matcher;

public class RequestMatchInfo {

    private MatchType reqMatchType;
    private String matchURL;
    private MethodInvokeInfo methodInvokeInfo;
    private Matcher matcher;

    private RequestMatchInfo(MatchType reqMatchType, String matchURL, MethodInvokeInfo methodInvokeInfo, Matcher matcher) {
        this.reqMatchType = reqMatchType;
        this.matchURL = matchURL;
        this.methodInvokeInfo = methodInvokeInfo;
        this.matcher = matcher;
    }

    public static RequestMatchInfo CreateRequestMatchAbsolutely(String matchURL, MethodInvokeInfo methodInvokeInfo) {
        return new RequestMatchInfo(
                MatchType.Absolutely,
                matchURL,
                methodInvokeInfo,
                null
        );
    }

    public static RequestMatchInfo CreateRequestMatchFuzzy(String matchURL, Matcher matcher, MethodInvokeInfo methodInvokeInfo) {
        return new RequestMatchInfo(
                MatchType.Fuzzy,
                matchURL,
                methodInvokeInfo,
                matcher
        );
    }

    public MatchType getReqMatchType() {
        return reqMatchType;
    }

    public String getMatchURL() {
        return matchURL;
    }

    public MethodInvokeInfo getMethodInvokeInfo() {
        return methodInvokeInfo;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public enum MatchType {
        Absolutely,Fuzzy;
    }

    @Override
    public String toString() {
        return "RequestMatchInfo{" +
                "reqMatchType=" + reqMatchType +
                ", matchURL='" + matchURL + '\'' +
                ", methodInvokeInfo=" + methodInvokeInfo +
                ", matcher=" + matcher +
                '}';
    }
}
