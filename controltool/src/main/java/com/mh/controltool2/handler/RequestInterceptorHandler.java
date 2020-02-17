package com.mh.controltool2.handler;

import com.mh.controltool2.config.MappedInterceptor;
import com.mh.controltool2.exceptions.invoke.HandlerThrowException;
import com.mh.controltool2.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

public class RequestInterceptorHandler {

    private final MappedInterceptor[] mappedInterceptorArray;
    private ThreadLocal<Stack<HandlerInterceptor>> handlerInterceptorStackThreadLocal = new ThreadLocal<>();

    public RequestInterceptorHandler(List<MappedInterceptor> mappedInterceptors) {
        this.mappedInterceptorArray = mappedInterceptors.toArray(new MappedInterceptor[0]);
    }

    public boolean request(Method handlerMethod) throws HandlerThrowException {
        HttpServletRequest request = RequestContextHolder.getHttpServletRequest();
        String reqPathInfo = request.getPathInfo();

        // interceptor stack
        Stack<HandlerInterceptor> handlerInterceptorStack = new Stack<>();

        boolean handlerAccept;
        for (MappedInterceptor item:mappedInterceptorArray) {
            if (!item.match(reqPathInfo)) continue;

            HandlerInterceptor handlerInterceptor = item.getInterceptor();

            try {
                handlerAccept = handlerInterceptor.preHandle(
                        request,
                        RequestContextHolder.getHttpServletResponse(),
                        handlerMethod
                );
                if (!handlerAccept) return false;
            } catch (Exception e) {
                throw new HandlerThrowException("Interceptor handler throw exception",e);
            }

            handlerInterceptorStack.push(handlerInterceptor);
        }

        handlerInterceptorStackThreadLocal.set(handlerInterceptorStack);
        return true;
    }

    public void requestHandlerAfter(Method handlerMethod,Exception handlerException) throws HandlerThrowException {
        HttpServletRequest request = RequestContextHolder.getHttpServletRequest();
        HttpServletResponse response = RequestContextHolder.getHttpServletResponse();
        // get interceptor object stack
        Stack<HandlerInterceptor> handlerInterceptorStack = handlerInterceptorStackThreadLocal.get();
        while (!handlerInterceptorStack.empty()) {
            HandlerInterceptor handlerInterceptor = handlerInterceptorStack.pop();

            try {
                handlerInterceptor.afterCompletion(
                        request,
                        response,
                        handlerMethod,
                        handlerException
                );
            } catch (Exception e) {
                throw new HandlerThrowException("Interceptor throw exception",e);
            }
        }
    }

    public void cleanInterceptorStack() {
        handlerInterceptorStackThreadLocal.remove();
    }

}
