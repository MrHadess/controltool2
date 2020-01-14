package com.mh.controltool2.util;

import java.util.Collection;

public class Assert {

    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, message);
        }
    }

    public static void hasText(String text, String message) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String text, String message) {
        if (text == null || text.isEmpty() || text.trim().length() <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void instanceCheckFailed(Class<?> type, Object obj, String msg) {
        String className = (obj != null ? obj.getClass().getName() : "null");
        String result = msg + " " + ("Object of class [" + className + "] must be an instance of " + type);
        throw new IllegalArgumentException(result);
    }

}
