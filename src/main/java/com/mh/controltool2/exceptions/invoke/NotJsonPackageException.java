package com.mh.controltool2.exceptions.invoke;

public class NotJsonPackageException extends RuntimeException {

    public NotJsonPackageException() {
        super ("Can't not find package to \"Gson\" \"Jackson\" or \"Fastjson\",so can't not change object to json string." +
                "\nIf you want to use auto serialize ,please import to one of them");
    }

}
