package com.mh.controltool2.serialize.json;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mh.controltool2.exceptions.serialize.JsonHandlerException;

import java.lang.reflect.Type;

public class FastjsonDataObjectSerialize implements DataObjectSerialize {

    @Override
    public String toJson(Object obj) throws JsonHandlerException {
        return JSON.toJSONString(obj);
    }

    @Override
    public <T> T toObject(String data, Type typeOfT) throws JsonHandlerException {
        return JSON.parseObject(data,typeOfT);
    }

    @Override
    public Object getJsonObjectSerialize() {
        return null;
    }

    @Override
    public JsonObjTargetEnum getJsonObjectTargetEnum() {
        return JsonObjTargetEnum.FastJson;
    }


}
