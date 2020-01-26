package com.mh.controltool2.handler;

public interface RequestProcessListener {

    /*
    * has requesting object
    * */
    default void requestStart(){};

    /*
     * has requesting object
     * */
    default void requestAfter(){};

}
