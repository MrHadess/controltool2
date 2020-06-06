package com.mh.controltool2.handler;

import com.mh.controltool2.config.MappedInterceptor;
import com.mh.controltool2.exceptions.invoke.HandlerThrowException;
import com.mh.controltool2.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class RequestInterceptorHandler {

    private final MappedInterceptor[] mappedInterceptorArray;
    private ThreadLocal<LinkedList<HandlerInterceptor>> handlerInterceptorLinkListThreadLocal = new ThreadLocal<>();

    public RequestInterceptorHandler(List<MappedInterceptor> mappedInterceptors) {
        this.mappedInterceptorArray = mappedInterceptors.toArray(new MappedInterceptor[0]);
    }

    public boolean request(Method handlerMethod) throws HandlerThrowException {
        HttpServletRequest request = RequestContextHolder.getHttpServletRequest();
        String reqPathInfo = request.getPathInfo();

        // interceptor stack
        LinkedList<HandlerInterceptor> handlerInterceptorLinkedList = new LinkedList<>();

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

            handlerInterceptorLinkedList.add(handlerInterceptor);
        }

        handlerInterceptorLinkListThreadLocal.set(handlerInterceptorLinkedList);
        return true;
    }

    public void requestHandlerAfter(Method handlerMethod,Exception handlerException) throws HandlerThrowException {
        HttpServletRequest request = RequestContextHolder.getHttpServletRequest();
        HttpServletResponse response = RequestContextHolder.getHttpServletResponse();
        // get interceptor object stack
        LinkedList<HandlerInterceptor> handlerInterceptorLinkedList = handlerInterceptorLinkListThreadLocal.get();
        HandlerInterceptor handlerInterceptor;
        while ((handlerInterceptor = handlerInterceptorLinkedList.pollLast()) != null) {
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
        handlerInterceptorLinkListThreadLocal.remove();
    }

}
