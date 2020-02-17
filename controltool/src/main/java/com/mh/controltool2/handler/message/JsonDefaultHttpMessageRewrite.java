package com.mh.controltool2.handler.message;

import com.mh.controltool2.annotation.Autowired;
import com.mh.controltool2.serialize.json.DataObjectSerialize;
import com.mh.controltool2.serialize.json.DefaultDataObjectSerialize;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonDefaultHttpMessageRewrite implements HttpMessageRewrite {

    private static final String DEFAULT_CONTENT_TYPE = "application/json;utf-8";
    private final String contentType;
    private DataObjectSerialize dataObjectSerialize;

    @Autowired
    public JsonDefaultHttpMessageRewrite(DefaultDataObjectSerialize dataObjectSerialize) {
        this.dataObjectSerialize = dataObjectSerialize;
        this.contentType = DEFAULT_CONTENT_TYPE;
    }

    public JsonDefaultHttpMessageRewrite(DataObjectSerialize dataObjectSerialize) {
        this.dataObjectSerialize = dataObjectSerialize;
        this.contentType = DEFAULT_CONTENT_TYPE;
    }

    public JsonDefaultHttpMessageRewrite(DataObjectSerialize dataObjectSerialize,String contentType) {
        this.dataObjectSerialize = dataObjectSerialize;
        this.contentType = contentType;
    }

    @Override
    public void responseRewriteMessage(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType(contentType);
        String jsonData = dataObjectSerialize.toJson(obj);
        PrintWriter respWriter = response.getWriter();
        respWriter.print(jsonData);
        respWriter.flush();
    }
    
}
