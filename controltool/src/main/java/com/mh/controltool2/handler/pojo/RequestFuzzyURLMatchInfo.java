package com.mh.controltool2.handler.pojo;

import com.mh.controltool2.method.URLInvokeTree;

import java.util.regex.Pattern;

public class RequestFuzzyURLMatchInfo {

    private Pattern pattern;
    private URLInvokeTree urlInvokeTree;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public URLInvokeTree getUrlInvokeTree() {
        return urlInvokeTree;
    }

    public void setUrlInvokeTree(URLInvokeTree urlInvokeTree) {
        this.urlInvokeTree = urlInvokeTree;
    }

    @Override
    public String toString() {
        return "FuzzyURLMatchInfo{" +
                "pattern=" + pattern +
                ", urlInvokeTree=" + urlInvokeTree +
                '}';
    }
}
