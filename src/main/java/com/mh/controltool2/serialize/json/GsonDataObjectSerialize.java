package com.mh.controltool2.serialize.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mh.controltool2.exceptions.serialize.JsonHandlerException;

import java.lang.reflect.Type;

public class GsonDataObjectSerialize implements DataObjectSerialize {
    
    private Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd hh:mm:ss")
        .create();

    @Override
    public String toJson(Object obj) throws JsonHandlerException {
        return gson.toJson(obj);
    }

    @Override
    public <T> T toObject(String data, Type typeOfT) throws JsonHandlerException {
        try {
            return gson.fromJson(data,typeOfT);
        } catch (JsonSyntaxException e) {
            throw new JsonHandlerException(e);
        }
    }

    @Override
    public Object getJsonObjectSerialize() {
        return gson;
    }

    @Override
    public JsonObjTargetEnum getJsonObjectTargetEnum() {
        return JsonObjTargetEnum.Gson;
    }


}
