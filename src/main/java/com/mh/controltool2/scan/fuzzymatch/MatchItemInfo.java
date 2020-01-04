package com.mh.controltool2.scan.fuzzymatch;

public class MatchItemInfo {

    private String itemToPattern;
    private String itemKey;

    public MatchItemInfo(String itemToPattern, String itemKey) {
        this.itemToPattern = itemToPattern;
        this.itemKey = itemKey;
    }

    public String getItemToPattern() {
        return itemToPattern;
    }

    public String getItemKey() {
        return itemKey;
    }
}
