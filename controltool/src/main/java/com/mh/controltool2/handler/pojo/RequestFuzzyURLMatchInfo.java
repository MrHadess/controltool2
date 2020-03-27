package com.mh.controltool2.handler.pojo;

import com.mh.controltool2.method.MatchInvokeObject;

import java.util.regex.Pattern;

public class RequestFuzzyURLMatchInfo {

    private Pattern pattern;
    private MatchInvokeObject matchInvokeObject;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public MatchInvokeObject getMatchInvokeObject() {
        return matchInvokeObject;
    }

    public void setMatchInvokeObject(MatchInvokeObject matchInvokeObject) {
        this.matchInvokeObject = matchInvokeObject;
    }

    @Override
    public String toString() {
        return "FuzzyURLMatchInfo{" +
                "pattern=" + pattern +
                ", matchInvokeObject=" + matchInvokeObject +
                '}';
    }
}
