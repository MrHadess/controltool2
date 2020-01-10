package com.mh.controltool2.exceptions;


public class RepeatURLMethodException extends RuntimeException {

    private String oldMethodClass;
    private String oldMethodArgClassArray;
    private String newMethodClass;
    private String newMethodArgClassArray;

    public RepeatURLMethodException(String oldMethodClass, String oldMethodArgClassArray, String newMethodClass, String newMethodArgClassArray) {
        super(toSimpleString(oldMethodClass,oldMethodArgClassArray,newMethodClass,newMethodArgClassArray));
        this.oldMethodClass = oldMethodClass;
        this.oldMethodArgClassArray = oldMethodArgClassArray;
        this.newMethodClass = newMethodClass;
        this.newMethodArgClassArray = newMethodArgClassArray;
    }

    public String toSimpleString() {
        return toSimpleString(oldMethodClass,oldMethodArgClassArray,newMethodClass,newMethodArgClassArray);
    }

    private static String toSimpleString(String oldMethodClass, String oldMethodArgClassArray, String newMethodClass, String newMethodArgClassArray) {
        return String.format("OLD=> '%s(%s)'.will be reject NEW=> '%s(%s)'",oldMethodClass,oldMethodArgClassArray,newMethodClass,newMethodArgClassArray);
    }

    public String getOldMethodClass() {
        return oldMethodClass;
    }

    public String getOldMethodArgClassArray() {
        return oldMethodArgClassArray;
    }

    public String getNewMethodClass() {
        return newMethodClass;
    }

    public String getNewMethodArgClassArray() {
        return newMethodArgClassArray;
    }

    @Override
    public String toString() {
        return "RepeatURLMethodException{" +
                "oldMethodClass='" + oldMethodClass + '\'' +
                ", oldMethodArgClassArray='" + oldMethodArgClassArray + '\'' +
                ", newMethodClass='" + newMethodClass + '\'' +
                ", newMethodArgClassArray='" + newMethodArgClassArray + '\'' +
                '}';
    }
}
