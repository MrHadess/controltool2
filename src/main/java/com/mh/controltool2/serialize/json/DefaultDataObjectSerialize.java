package com.mh.controltool2.serialize.json;

import com.mh.controltool2.exceptions.serialize.JsonHandlerException;
import com.mh.controltool2.exceptions.serialize.UnknownJsonLibException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class DefaultDataObjectSerialize implements DataObjectSerialize {

    private static Logger logger = LoggerFactory.getLogger("DefaultDataObjectSerialize");

    private static final String[] CHECK_GSON_PACKAGE = new String[]{"com.google.gson.Gson"};
    private static final String[] CHECK_JACKSON_PACKAGE = new String[]{"com.fasterxml.jackson.databind.ObjectMapper","com.fasterxml.jackson.core.JsonFactory"};
    private static final String[] CHECK_FASTJSON_PACKAGE = new String[]{"com.alibaba.fastjson.JSON"};

    private DataObjectSerialize dataObjectSerialize;

    public DefaultDataObjectSerialize() throws UnknownJsonLibException {

        if (hasPackages(CHECK_GSON_PACKAGE)) {
            dataObjectSerialize = new GsonDataObjectSerialize();
            logger.info("Use Gson serialize");
            return;
        }

        if (hasPackages(CHECK_JACKSON_PACKAGE)) {
            dataObjectSerialize = new JacksonDataObjectSerialize();
            logger.info("Use Jackson serialize");
            return;
        }

        if (hasPackages(CHECK_FASTJSON_PACKAGE)) {
            dataObjectSerialize = new FastjsonDataObjectSerialize();
            logger.info("Use Fastjson serialize");
            return;
        }

        throw new UnknownJsonLibException();
    }

    @Override
    public String toJson(Object obj) throws JsonHandlerException {
        return dataObjectSerialize.toJson(obj);
    }

    @Override
    public <T> T toObject(String data, Type typeOfT) throws JsonHandlerException {
        return dataObjectSerialize.toObject(data,typeOfT);
    }

    @Override
    public Object getJsonObjectSerialize() {
        return dataObjectSerialize.getJsonObjectSerialize();
    }

    @Override
    public JsonObjTargetEnum getJsonObjectTargetEnum() {
        return dataObjectSerialize.getJsonObjectTargetEnum();
    }

    private boolean hasPackages(String[] tClassNameArray) {
        try {
            for (String tClassName:tClassNameArray) {
                Class.forName(tClassName);
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
