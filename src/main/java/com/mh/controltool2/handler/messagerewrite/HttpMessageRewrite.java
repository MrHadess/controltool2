package com.mh.controltool2.handler.messagerewrite;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HttpMessageRewrite {

    void responseRewriteMessage(HttpServletResponse response, Object obj) throws IOException;

}
