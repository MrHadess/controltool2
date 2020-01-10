package com.mh.controltool2.exceptions;


public class RepeatURLMethodException extends RuntimeException {

    private String oldClass;
    private String oldMethod;
    private String newClass;
    private String newMethod;

    public RepeatURLMethodException(String oldClass, String oldMethod, String newClass, String newMethod) {
        super(toSimpleString(oldClass, oldMethod, newClass, newMethod));
        this.oldClass = oldClass;
        this.oldMethod = oldMethod;
        this.newClass = newClass;
        this.newMethod = newMethod;
    }

    public String toSimpleString() {
        return toSimpleString(oldClass, oldMethod, newClass, newMethod);
    }

    private static String toSimpleString(String oldMethodClass, String oldMethodArgClassArray, String newMethodClass, String newMethodArgClassArray) {
        return String.format("OLD=> '%s(%s)'.will be reject NEW=> '%s(%s)'",oldMethodClass,oldMethodArgClassArray,newMethodClass,newMethodArgClassArray);
    }

    public String getOldClass() {
        return oldClass;
    }

    public String getOldMethod() {
        return oldMethod;
    }

    public String getNewClass() {
        return newClass;
    }

    public String getNewMethod() {
        return newMethod;
    }

    @Override
    public String toString() {
        return "RepeatURLMethodException{" +
                "oldClass='" + oldClass + '\'' +
                ", oldMethod='" + oldMethod + '\'' +
                ", newClass='" + newClass + '\'' +
                ", newMethod='" + newMethod + '\'' +
                '}';
    }
}
