package com.mh.controltool2;

import com.mh.controltool2.scan.annotation.HandlerConfig;
import com.mh.controltool2.scan.annotation.HandlerControl;

public class ApplicationContext {

    private HandlerControl handlerControl;
    private HandlerConfig handlerConfig;

    public HandlerControl getHandlerControl() {
        return handlerControl;
    }

    public void setHandlerControl(HandlerControl handlerControl) {
        this.handlerControl = handlerControl;
    }

    public HandlerConfig getHandlerConfig() {
        return handlerConfig;
    }

    public void setHandlerConfig(HandlerConfig handlerConfig) {
        this.handlerConfig = handlerConfig;
    }
}
