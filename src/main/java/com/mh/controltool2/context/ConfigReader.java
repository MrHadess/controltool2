package com.mh.controltool2.context;

public interface ConfigReader {

    String readValue(String key);

    <T> T readValue(String key,Class<T> tagValueType);

}
