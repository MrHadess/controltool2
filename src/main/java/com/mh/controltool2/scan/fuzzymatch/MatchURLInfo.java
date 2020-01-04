package com.mh.controltool2.scan.fuzzymatch;

import java.util.ArrayList;

public class MatchURLInfo {

    private ArrayList<String> matchField;
    private String urlMatchCode;

    public MatchURLInfo(ArrayList<String> matchField, String urlMatchCode) {
        this.matchField = matchField;
        this.urlMatchCode = urlMatchCode;
    }

    public ArrayList<String> getMatchField() {
        return matchField;
    }

    public String getUrlMatchCode() {
        return urlMatchCode;
    }
}
