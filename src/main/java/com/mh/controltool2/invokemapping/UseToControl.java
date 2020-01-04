package com.mh.controltool2.invokemapping;

import com.mh.controltool2.exceptions.invoke.NotJsonPackageException;
import com.mh.controltool2.serialize.json.LoadJsonToClass;
import com.mh.controltool2.scan.CheckResponseWrite;
import com.mh.controltool2.scan.DataTypeChange;
import com.mh.controltool2.scan.InvokeMsg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UseToControl {

    private DataTypeChange dataTypeChange = new DataTypeChange();

    private LoadJsonToClass loadJsonToClass;

    //缓存实例 较少因一个类包含多个Mapping时，进行多次实例化操作。以实现减少实例化，使用相同变量以及减少内存开销
    private HashMap<String,InitData> initDataMap = new HashMap<String, InitData>();

    private HashMap<String,InvokeMsg> absolutelyMap;
    private HashMap<Pattern,InvokeMsg> fuzzyMap;

    //初始化加载数据
    public UseToControl(HashMap<String,InvokeMsg> absolutelyMap, HashMap<String,InvokeMsg> fuzzyMap) {

        this.absolutelyMap = absolutelyMap;

        HashMap<Pattern,InvokeMsg> fuzzyMapChange = new HashMap<Pattern, InvokeMsg>();
        for (Map.Entry<String,InvokeMsg> item:fuzzyMap.entrySet()){
            Pattern urlPattern = Pattern.compile(item.getKey());
            fuzzyMapChange.put(urlPattern,item.getValue());
        }

        this.fuzzyMap = fuzzyMapChange;

    }

    public void run(String s, HttpServletRequest request, HttpServletResponse response){
//    public void run(HashMap<String,InvokeMsg> absolutelyMap, HashMap<String,InvokeMsg> fuzzyMap, String s, HttpServletRequest request, HttpServletResponse response){

        InvokeMsg invokeMsg = absolutelyMap.get(s);

        if (invokeMsg != null) {
            invokeMethodAbsolutely(invokeMsg,request,response);
            return;
        }

        //尝试模糊匹配
        if (fuzzyUrlMatch(fuzzyMap,s,request,response)) return;

        //最后无法无法查找相应的控制器是触发 404
        try {
            response.getWriter().print("404 The origin server did not find a current representation for the target resource (ControlTool)");
            response.setStatus(404);
//                response.sendError(404);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


//    @SuppressWarnings("all")
    private void invokeMethodAbsolutely(InvokeMsg invokeMsg, HttpServletRequest request, HttpServletResponse response){
        try {


            InitData initData = initDataMap.get(invokeMsg.getClassName());
            if (initData == null){
                Class nowForNameclass = Class.forName(invokeMsg.getClassName());

                initData = new InitData(
                        nowForNameclass,
                        nowForNameclass.newInstance()
                );
                initDataMap.put(invokeMsg.getClassName(),initData);
            }



            Object object = initData.getClassNewInstance();


            Method method = initData.getClassForName().getDeclaredMethod(invokeMsg.getMethodName(),invokeMsg.getMethodParameterNames());//能返回当前函数为“公共、保护、默认（包）访问和私有方法”能被返回

            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            Object[] methodNamesObject = new Object [invokeMsg.getMethodParameterNames().length];
            InvokeMsg.ParameterNamesMatch[] methodParameterTypes = invokeMsg.getMethodParameterTypes();//取得函数中每个参数的对应类型
            for (int i = 0; i < invokeMsg.getMethodParameterNames().length; i++){
//                String parameterName = invokeMsg.getMethodParameterNames()[i].getName();
//                if (parameterName.equals("javax.servlet.http.HttpServletRequest")){
////                    log("injection HttpServletRequest");
//                    methodNamesObject[i] = request;
//                }else if (parameterName.equals("javax.servlet.http.HttpServletResponse")){
////                    log("injection HttpServletResponse");
//                    methodNamesObject[i] = response;
//                }else {
//                    methodNamesObject[i] = null;
//                }

                //获取相关的枚举
                switch (methodParameterTypes[i]){
                    case Request:
                        methodNamesObject[i] = request;
                        break;
                    case Response:
                        methodNamesObject[i] = response;
                        break;
                    case RequestParam:
                        methodNamesObject[i] = request.getParameter(invokeMsg.getMethodRequestParamTypes()[i]);
                        break;
                    case PathVariable:
                    case Unknown:
                        methodNamesObject[i] = null;
                        break;
                }

            }


            Object methodRun = null;
            try {
                //捕获当前堆栈所有异常
                methodRun = method.invoke(object, methodNamesObject);

            } catch (InvocationTargetException e) {
                //打印当前控制器的异常堆栈，隐藏本身的异常的堆栈
                e.getTargetException().printStackTrace();
                //返回堆栈异常内容
                response.setStatus(500);
                respWrite(CheckResponseWrite.ReturnStateToDo.WriteNow, getTrace(e), response);
                return;
            } catch (Exception e) {
                //打印当前控制器的异常堆栈，隐藏本身的异常的堆栈
                e.printStackTrace();
                //返回堆栈异常内容
                response.setStatus(500);
                respWrite(CheckResponseWrite.ReturnStateToDo.WriteNow, getTrace(e), response);
                return;
            }

            if (methodRun == null) return;

            respWrite(invokeMsg.getResponseWriteState(),methodRun,response);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void invokeMethodFuzzy(InvokeMsg invokeMsg,HttpServletRequest request, HttpServletResponse response,Matcher matcherToUrl){
        try {


            InitData initData = initDataMap.get(invokeMsg.getClassName());
            if (initData == null){
                Class nowForNameclass = Class.forName(invokeMsg.getClassName());

                initData = new InitData(
                        nowForNameclass,
                        nowForNameclass.newInstance()
                );
                initDataMap.put(invokeMsg.getClassName(),initData);
            }



            Object object = initData.getClassNewInstance();


            Method method = initData.getClassForName().getDeclaredMethod(invokeMsg.getMethodName(),invokeMsg.getMethodParameterNames());//能返回当前函数为“公共、保护、默认（包）访问和私有方法”能被返回

            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            Object[] methodNamesObject = new Object [invokeMsg.getMethodParameterNames().length];
            HashMap<Integer,Integer> parameterMatchGroupMap = invokeMsg.getParameterMatchGroups();
            InvokeMsg.ParameterNamesMatch[] methodParameterTypes = invokeMsg.getMethodParameterTypes();//取得函数中每个参数的对应类型
            for (int i = 0; i < invokeMsg.getMethodParameterNames().length; i++){
//                String parameterName = invokeMsg.getMethodParameterNames()[i].getName();
//                if (parameterName.equals("javax.servlet.http.HttpServletRequest")){
////                    log("injection HttpServletRequest");
//                    methodNamesObject[i] = request;
//                }else if (parameterName.equals("javax.servlet.http.HttpServletResponse")){
////                    log("injection HttpServletResponse");
//                    methodNamesObject[i] = response;
//                }else {
////                    methodNamesObject[i] = null;
//                    Integer parameterGroup = parameterMatchGroupMap.get(i);
//                    if (parameterGroup == null){
//                        methodNamesObject[i] = null;
//                        continue;
//                    }
//                    //根据数据类型值
//                    if (parameterName.equals("java.lang.String")){
//                        methodNamesObject[i] = matcherToUrl.group(parameterGroup + 1);
//                    }else {
//                        methodNamesObject[i] = dataTypeChange.stringToTypeData(parameterName,matcherToUrl.group(parameterGroup + 1));
//                    }
//
//                }

                //获取相关的枚举
                switch (methodParameterTypes[i]){
                    case Request:
                        methodNamesObject[i] = request;
                        break;
                    case Response:
                        methodNamesObject[i] = response;
                        break;
                    case PathVariable:
                        Integer parameterGroup = parameterMatchGroupMap.get(i);
                        if (parameterGroup == null){
                            methodNamesObject[i] = null;
                            continue;
                        }
                        //根据数据类型值
                        String parameterName = invokeMsg.getMethodParameterNames()[i].getName();
                        if (parameterName.equals("java.lang.String")){
                            methodNamesObject[i] = matcherToUrl.group(parameterGroup + 1);
                        }else {
                            methodNamesObject[i] = dataTypeChange.stringToTypeData(parameterName,matcherToUrl.group(parameterGroup + 1));
                        }
                        break;
                    case RequestParam:
                        methodNamesObject[i] = request.getParameter(invokeMsg.getMethodRequestParamTypes()[i]);
                        break;
                    case Unknown:
                        methodNamesObject[i] = null;
                        break;
                }
            }


            Object methodRun = null;
            try {
                //捕获当前堆栈所有异常
                methodRun = method.invoke(object, methodNamesObject);

            } catch (InvocationTargetException e) {
                //打印当前控制器的异常堆栈，隐藏本身的异常的堆栈
                e.getTargetException().printStackTrace();
                //返回堆栈异常内容
                response.setStatus(500);
                respWrite(CheckResponseWrite.ReturnStateToDo.WriteNow, getTrace(e), response);
                return;
            } catch (Exception e) {
                //打印当前控制器的异常堆栈，隐藏本身的异常的堆栈
                e.printStackTrace();
                //返回堆栈异常内容
                response.setStatus(500);
                respWrite(CheckResponseWrite.ReturnStateToDo.WriteNow, getTrace(e), response);
                return;
            }

            if (methodRun == null) return;

            respWrite(invokeMsg.getResponseWriteState(), methodRun, response);



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean fuzzyUrlMatch(HashMap<Pattern,InvokeMsg> fuzzyMap, String url, HttpServletRequest request, HttpServletResponse response){

        if (url == null) return false;

        for (Map.Entry<Pattern,InvokeMsg> item:fuzzyMap.entrySet()){

//            Pattern urlPattern = Pattern.compile(item.getKey());
//            Matcher matcher = urlPattern.matcher(url);

            Matcher matcher = item.getKey().matcher(url);

            if (!matcher.matches()) continue;

            invokeMethodFuzzy(item.getValue(),request,response,matcher);

            return true;
        }

        return false;
    }

    private void respWrite(CheckResponseWrite.ReturnStateToDo returnStateToDo,Object methodReturn,HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        switch (returnStateToDo){
            case SerializableObjectToWrite:
                if (loadJsonToClass == null){
                    try {
                        loadJsonToClass = new LoadJsonToClass();
                    }catch (NotJsonPackageException e){
                        printWriter.print(e.getMessage());
                        e.printStackTrace();
                    }
                }
                printWriter.print(loadJsonToClass.toJson(methodReturn));
                break;
            case WriteNow:
                printWriter.print(methodReturn);
                break;
        }
        printWriter.flush();
        printWriter.close();


    }


    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    private void log(Object o){
        System.out.println(o);
    }

    private boolean checkHasNoStructuralParam(Class<?> c) {
        for (Constructor<?> item:c.getConstructors()) {
            if (item.getParameterTypes().length <= 0) return true;
        }
        return false;
    }

}
