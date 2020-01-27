package com.mh.controltool2.exceptions;

public class BeanFactoryException extends RuntimeException {

    private ExpType type;

    public BeanFactoryException(String message) {
        super(message);
        type = ExpType.Eles;
    }

    public BeanFactoryException(ExpType type) {
        super(type.message);
        this.type = type;
    }

    public BeanFactoryException(ExpType type,String message) {
        super(type.message + " " + message);
        this.type = type;
    }

    public ExpType getType() {
        return type;
    }

    public enum ExpType {
        IsInterface("Class is interface"),
        UnsupportedDataType("Unsupported data type"),
        ReplaceBean("Has replace bean"),
        Eles("Else exception");

        String message;
        ExpType(String msg) {
            this.message = msg;
        }
    }

    @Override
    public String toString() {
        return "BeanFactoryException{" +
                "type=" + type +
                '}';
    }
}
