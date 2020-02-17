package com.mh.controltool2.scan;

import com.mh.controltool2.exceptions.invoke.ParamDataIsEmptyException;

public class DataTypeChange {


    public Object stringToTypeData(String parameterName, String data) throws NumberFormatException {

        TypeEnum typeEnumByValue = TypeEnum.getByValue(parameterName);
        if (typeEnumByValue == null) return null;

        if (data == null || data.isEmpty()) {
            switch (typeEnumByValue) {
                case baseInt:
                case baseLong:
                case baseFloat:
                case baseDouble:
                    throw new ParamDataIsEmptyException();
                default:
                    return null;
            }
        }


        switch (typeEnumByValue) {
            case typeString:
                return data;
            case baseInt:
            case typeInt:
                return Integer.valueOf(data);
            case baseLong:
            case typeLong:
                return Long.valueOf(data);
            case baseFloat:
            case typeFloat:
                return Float.valueOf(data);
            case baseDouble:
            case typeDouble:
                return Double.valueOf(data);
            default:
                return null;
        }


    }


    private enum TypeEnum {

        typeString("java.lang.String"),
        typeDouble("java.lang.Double"),
        typeFloat("java.lang.Float"),
        typeInt("java.lang.Integer"),
        typeLong("java.lang.Long"),

        baseDouble("double"),
        baseFloat("float"),
        baseInt("int"),
        baseLong("long");


        String data;

        TypeEnum(String s) {
            data = s;
        }

        private static TypeEnum getByValue(String s) {
            for (TypeEnum item : values()) {
                if (item.data.equals(s)) return item;
            }
            return null;
        }

    }
}
