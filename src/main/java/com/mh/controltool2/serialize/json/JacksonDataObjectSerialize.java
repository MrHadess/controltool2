package com.mh.controltool2.serialize.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mh.controltool2.exceptions.serialize.JsonHandlerException;

import java.lang.reflect.Type;

public class JacksonDataObjectSerialize implements DataObjectSerialize {


    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toJson(Object obj) throws JsonHandlerException {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonHandlerException(e);
        }
    }

    @Override
    public <T> T toObject(String data, Type typeOfT) throws JsonHandlerException {
        try {
            return objectMapper.readValue(data, new TypeReference<T>(){});
        } catch (JsonProcessingException e) {
            throw new JsonHandlerException(e);
        }
    }

    @Override
    public Object getJsonObjectSerialize() {
        return objectMapper;
    }

    @Override
    public JsonObjTargetEnum getJsonObjectTargetEnum() {
        return JsonObjTargetEnum.Jackson;
    }


}
