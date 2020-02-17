package com.mh.controltool2.serialize;

import com.mh.controltool2.exceptions.invoke.ParamDataIsEmptyException;
import com.mh.controltool2.exceptions.invoke.UnsupportedSerializeObjectException;

public class BaseDataTypeChange {

    private static final String JAVA_LANG_STRING = "java.lang.String";
    private static final String JAVA_LANG_DOUBLE = "java.lang.Double";
    private static final String JAVA_LANG_FLOAT = "java.lang.Float";
    private static final String JAVA_LANG_INTEGER = "java.lang.Integer";
    private static final String JAVA_LANG_LONG = "java.lang.Long";

    private static final String BASE_DOUBLE = "double";
    private static final String BASE_FLOAT = "float";
    private static final String BASE_INT = "int";
    private static final String BASE_LONG = "long";


    public static Object stringToBaseData(String parameterName, String data) throws UnsupportedSerializeObjectException,ParamDataIsEmptyException,NumberFormatException {

        if (data == null || data.isEmpty()) {
            switch (parameterName) {
                case BASE_DOUBLE:
                case BASE_FLOAT:
                case BASE_INT:
                case BASE_LONG:
                    throw new ParamDataIsEmptyException();
                default:
                    return null;
            }
        }

        switch (parameterName) {
            case JAVA_LANG_STRING:
                return data;
            case BASE_INT:
            case JAVA_LANG_INTEGER:
                return Integer.valueOf(data);
            case BASE_LONG:
            case JAVA_LANG_LONG:
                return Long.valueOf(data);
            case BASE_FLOAT:
            case JAVA_LANG_FLOAT:
                return Float.valueOf(data);
            case BASE_DOUBLE:
            case JAVA_LANG_DOUBLE:
                return Double.valueOf(data);
            default:
                throw new UnsupportedSerializeObjectException();
        }

    }

}
