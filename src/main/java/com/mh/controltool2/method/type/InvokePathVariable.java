package com.mh.controltool2.method.type;

/*
* get url match text to param value
* need:
* match url local
* */
public class InvokePathVariable extends InvokeObjectInfo {

    private Integer matchLocalIndex;

    @Override
    public TypeEnum getHandlerType() {
        return TypeEnum.PathVariable;
    }

    public Integer getMatchLocalIndex() {
        return matchLocalIndex;
    }

    public void setMatchLocalIndex(Integer matchLocalIndex) {
        this.matchLocalIndex = matchLocalIndex;
    }
}
