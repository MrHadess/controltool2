package com.mh.controltool2;


import java.util.Date;

public class LogOut {

    @Deprecated
    public void logError(Object o,Class cl){
        System.out.println("Error  Time:" + new Date().toString() + " ClassName:" + cl.getName() + ":\n" + o);
    }

    @Deprecated
    public void logWarn(Object o, Class cl) {
        System.out.println("Warn  Time:" + new Date().toString() + " ClassName:" + cl.getName() + ":\n" + o);
    }

    public static void e(String errInfo) {
        System.out.println("ControlTool=>Error:" + errInfo);
    }

    public static void e(String tag, Object errObject) {
        System.out.println("ControlTool=>Error " + tag + ":" + String.valueOf(errObject));
    }

    public static void i(String info) {
        System.out.println("ControlTool:" + info);
    }

    public static void i(String tag, Object info) {
        System.out.println("ControlTool=>" + tag + ":" + String.valueOf(info));
    }

}
