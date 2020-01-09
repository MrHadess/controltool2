package com.mh.controltool2.serialize.json;

import com.mh.controltool2.exceptions.serialize.JsonHandlerException;

import java.lang.reflect.Type;

public interface DataObjectSerialize {

    String toJson(Object obj) throws JsonHandlerException;

    <T> T toObject(String data,Type typeOfT) throws JsonHandlerException;

    Object getJsonObjectSerialize();

    JsonObjTargetEnum getJsonObjectTargetEnum();

}
