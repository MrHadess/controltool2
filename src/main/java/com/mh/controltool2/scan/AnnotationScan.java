package com.mh.controltool2.scan;

import com.mh.controltool2.ControlAnnotation;
import com.mh.controltool2.exceptions.scan.ParameterNameNotStringException;
import com.mh.controltool2.LogOut;
import com.mh.controltool2.scan.fuzzymatch.FuzzyURLMatchToInfo;
import com.mh.controltool2.scan.fuzzymatch.MatchURLInfo;
import com.mh.controltool2.scan.fuzzymatch.SingleItemCheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnotationScan {

    //通过Map Set 方式在添加单一元素前后比较是否有数值增长，有--》证明没有重复，没有--》有重复抛出当前重复项Key，以及类相关信息，用作重复排除，以及当前Map中对应的Value相关信息


    private CheckResponseWrite checkResponseWrite = new CheckResponseWrite();
    private CheckParameterType checkParameterType = new CheckParameterType();
    private SingleItemCheck fuzzySingleItemCheck = new SingleItemCheck();
    private FuzzyURLMatchToInfo fuzzyURLMatchToInfo = new FuzzyURLMatchToInfo();

    public HashMap<String,InvokeMsg> nowScan(List<Class<?>> classList){

        if (classList == null || classList.size() <= 0) return null;

        HashMap<String,InvokeMsg> map = new HashMap<String, InvokeMsg>();

        HashSet<String> urlSet = new HashSet<String>();//用于检查重复URL

        for (Class<?> item:classList){


            ControlAnnotation.RestController restControllerModulClass = item.getAnnotation(ControlAnnotation.RestController.class);
            //当此类不存在控制器时 不加入URL匹配Map中
            if (restControllerModulClass == null) continue;
            LogOut.i("-----------------------------------------------------------");
            LogOut.i("This has RestController:::" + item);

            String className = item.getName();

            //控制器根Mapping前缀匹配
            String urlFirstMatch = "";
            ControlAnnotation.RequestMapping requestMappingClass = item.getAnnotation(ControlAnnotation.RequestMapping.class);
            if (requestMappingClass != null) {
                String value = requestMappingClass.value().trim();
                LogOut.i("This has RequestMapping:::" + item + "  value=" + requestMappingClass.value());
                if (!value.isEmpty()){
                    urlFirstMatch = value;
                }
            }



            Method[] methods = item.getDeclaredMethods();
            if (methods == null || methods.length <= 0) continue;

            for (Method method : methods) {


                LogOut.i("++++++++++++++" );
                LogOut.i("Now method name:" + method.getName() );


                ControlAnnotation.RequestMapping requestMapping = method.getAnnotation(ControlAnnotation.RequestMapping.class);
                if (requestMapping == null) {
                    LogOut.i("Does not RequestMapping");
                    continue;
                }
//                LogOut.i
//("This has RequestMapping:::" + item + "  value=" + requestMapping.value());

                String requestMappingValue = requestMapping.value().trim();
                if (requestMappingValue.isEmpty()) {
                    LogOut.i("Does not RequestMapping Value");
                    continue;
                }


                //获取当前函数的各个的参数类名,目前由于没对返回参数进行response进行数据写入，so只能依靠参数注入，将操作权限转移到函数中，当函数无对应方法时当作没有任何操作
                Class<?>[] methodNames = method.getParameterTypes();
                if (methodNames.length  <= 0) {
                    LogOut.i("Method parameter type is zero");
                    continue;
                }


                String[] methodNamesString = new String [methodNames.length];
                for (int i = 0;i < methodNames.length;i++){
                    methodNamesString[i] = methodNames[i].getName();
                }

                String matchURl = urlFirstMatch + requestMappingValue;

                //对URl进行分析,分析是否采取模糊匹配(URL模板)进行匹配
//                fuzzyMatch(matchURl,className,method);

                //检查set表是否有增长，无这表示有重复的URL，需要打印LogOut.i
                int oldSetSize = urlSet.size();
                if (urlSet.contains(matchURl)){

                    InvokeMsg first = map.get(matchURl);
                    if (first == null) break;

                    LogOut.e("\nError:URL conflict to \'" + matchURl + "\'" +
                        "\nFirst==>" + first.getClassName() + "." + first.getMethodName() +
                        "\nLost==>" + className + "." + method.getName()
                    );
                    break;
                }else {
                    urlSet.add(matchURl);
                }

                CheckResponseWrite.ReturnStateToDo writeSate = checkResponseWrite.responseHandlerSate(method.getReturnType().getName());

                //Key组装后的URL
//                map.put(matchURl,new InvokeMsg(className,method.getName(),methodNames,writeSate));

                LogOut.i("put matchURL:" + matchURl + "  method.getName:" + method.getName() );



            }
        }


        return map;
    }

    private Pattern urlWarnPattern = Pattern.compile("(/+|.*?)(/{2,})(/+|.*?)");//检查是否存在多个"/"斜杠以作出提示（需要附加相关响应控制器的位置信息）
    private Pattern checkVarMatch = Pattern.compile(".+?[{].+?[}].*?");//简单检查是否存在"XX/{ZZZZZ}/XXXX/{ZZZZZZ}/XXXXX"格式的字符串(存在时则认为存在相关动态变量，将进行相关流程处理)
//    private Pattern checkVarMatch = Pattern.compile(".+?[{].+?[}].+?");//简单检查是否存在"XX/{ZZZZZ}/XXXX/{ZZZZZZ}/XXXXX"格式的字符串(存在时则认为存在相关动态变量，将进行相关流程处理)
    private Pattern checkVarMatch2 = Pattern.compile(".+?/[{].+?[}]");//简单检查是否存在"XX/{ZZZZZ}/XXXX/{ZZZZZZ}/XXXXX"格式的字符串(存在时则认为存在相关动态变量，将进行相关流程处理)


    private HashMap<String,InvokeMsg> absolutelyMap;
    private HashMap<String,InvokeMsg> fuzzyMap;

    private HashSet<String> absolutelyUrlSet;
    private HashSet<String> fuzzyUrlSet;

    public LocalParameterData scanAnnotation(List<Class<?>> classList){

        absolutelyMap = new HashMap<String, InvokeMsg>();
        fuzzyMap = new HashMap<String, InvokeMsg>();

        absolutelyUrlSet = new HashSet<String>();
        fuzzyUrlSet = new HashSet<String>();

        if (classList == null || classList.size() <= 0) return null;

        for (Class<?> item:classList) {


            ControlAnnotation.RestController restControllerModulClass = item.getAnnotation(ControlAnnotation.RestController.class);
            //当此类不存在控制器时 不加入URL匹配Map中
            if (restControllerModulClass == null) continue;
            LogOut.i("-----------------------------------------------------------");
            LogOut.i("This has RestController:::" + item);

            String className = item.getName();

            //控制器根Mapping前缀匹配
            String urlFirstMatch = "";
            ControlAnnotation.RequestMapping requestMappingClass = item.getAnnotation(ControlAnnotation.RequestMapping.class);
            if (requestMappingClass != null) {
                String value = requestMappingClass.value().trim();
                LogOut.i("This has RequestMapping:::" + item + "  value=" + requestMappingClass.value());
                if (!value.isEmpty()) {
                    urlFirstMatch = value;
                }
            }


            //Method check
            methodCheck(item,urlFirstMatch,className);



        }

        return new LocalParameterData(absolutelyMap,fuzzyMap);

    }

    private void methodCheck(Class<?> aClass,String urlFirstMatch,String className){

        Method[] methods = aClass.getDeclaredMethods();
        if (methods == null || methods.length <= 0) return;

        for (Method method : methods) {


            LogOut.i("++++++++++++++" );
            LogOut.i("Now method name:" + method.getName() );


            ControlAnnotation.RequestMapping requestMapping = method.getAnnotation(ControlAnnotation.RequestMapping.class);
            if (requestMapping == null) {
                LogOut.i("Does not RequestMapping");
                continue;
            }
//                LogOut.i
//("This has RequestMapping:::" + item + "  value=" + requestMapping.value());

            String requestMappingValue = requestMapping.value().trim();
            if (requestMappingValue.isEmpty()) {
                LogOut.i("Does not RequestMapping Value");
                continue;
            }


            Class<?>[] methodParameterTypeNames = method.getParameterTypes();

//            String[] methodNamesString = new String [methodParameterTypeNames.length];
//            for (int i = 0;i < methodParameterTypeNames.length;i++){
//                methodNamesString[i] = methodParameterTypeNames[i].getName();
//            }

            String matchURl = urlFirstMatch + requestMappingValue;

            //URL格式检查，URL拼接后"/"是否存在连续重叠的问题
            Matcher urlWarnMatch = urlWarnPattern.matcher(matchURl);
            if (urlWarnMatch.matches()) {
                LogOut.i("Warn:URL warn\'" + matchURl + "\' Class:" + className + "." + method.getName());
            }

            //对URl进行分析,分析是否采取模糊匹配(URL模板)进行匹配
            Matcher urlMatchModul = checkVarMatch.matcher(matchURl);
            Matcher urlMatchModul2 = checkVarMatch2.matcher(matchURl);


            if (urlMatchModul.matches() || urlMatchModul2.matches()) {
                //fuzzy match
                LogOut.i("Has FuzzyMatch");
                fuzzyMatch(matchURl, method, aClass, methodParameterTypeNames);
            } else {
                //absolutely match
                LogOut.i("Has AbsolutelyMatch");
                absolutelyMatch(matchURl, method, aClass, methodParameterTypeNames);
            }


        }

    }

    private void fuzzyMatch(String url , Method method , Class<?> aClass , Class<?>[] methodParameterTypeNames ){

        //函数参数 正则配对分组参数
        HashMap<Integer,Integer> parameterMatchGroups = new HashMap<Integer, Integer>();

        ArrayList<String> partList = new ArrayList<String>();//URL模板 路径块

        Matcher urlWarnMatch = urlWarnPattern.matcher(url);
        if (urlWarnMatch.matches()) {
            LogOut.i("Warn:URL warn\'" + url + "\' Class:" + aClass.getName() + "." + method.getName());
            return;
        }

        //用于存储当前的 URl模板的命名 用于Method的参数检查，以用作标记以进行数据注入
        ArrayList<String> matchField = new ArrayList<String>();
        //匹配URL
        String matchCode;

        MatchURLInfo matchURLInfo = fuzzyURLMatchToInfo.matchURL(url);
        matchCode = matchURLInfo.getUrlMatchCode();
        matchField = matchURLInfo.getMatchField();

//        Old function 解析匹配旧方法
//        //用于存储当前的 URl模板的命名 用于Method的参数检查，以用作标记以进行数据注入
//        ArrayList<String> matchField = new ArrayList<String>();
//
//        //用于拼接当前的表达式的字符操作
//        StringBuilder matchCodeSB = new StringBuilder();
//        String matchCode;
//
//        //确认第一个是否存在“/”以补充此字符
//        if(url.startsWith("/")) matchCodeSB.append("/");
//
//        String[] simpleSp = url.split("/");
//        for (String item : simpleSp) {
//
//            if (item.isEmpty()) continue;
//
//            MatchItemInfo matchItemInfo = fuzzySingleItemCheck.checkItem(item);
//            if (matchItemInfo == null) {
//                matchCodeSB.append(item);
//            } else {
//                matchField.add(matchItemInfo.getItemKey());
//                matchCodeSB.append(matchItemInfo.getItemToPattern());
//            }
//
//
//            //会导致拼接的表达式 最后多出一个“/”字符
//            matchCodeSB.append("/");
//        }
//
//        //确认最后一个是否存在“/”以确认是否移除此字符
//        if (url.endsWith("/")) {
//            matchCode = matchCodeSB.toString();
//        }else {
//            matchCode = matchCodeSB.substring(0,matchCodeSB.length()-1);
//        }


        //检查多重URL匹配
        if(fuzzyUrlSet.contains(matchCode)){
            InvokeMsg first = fuzzyMap.get(matchCode);

            LogOut.e("\nError:URL conflict--> \'" + url + "\' regular--> \'" + matchCode + "\'" +
                    "\nFirst==>" + first.getClassName() + "." + first.getMethodName() +
                    "\nLost==>" + aClass.getName() + "." + method.getName()
            );
            return;
        }
        fuzzyUrlSet.add(matchCode);

        Annotation[][] ParameterAnnotations = method.getParameterAnnotations();

//        for (Annotation[] aParameter : ParameterAnnotations) {
//
//            for (int i = 0; i < aParameter.length; i++) {
//
//                Annotation nowAnnotation = aParameter[i];
//                boolean aaa = nowAnnotation.annotationType().equals(ControlAnnotation.PathVariable.class);
//                if (aaa) {
//                    LogOut.i
//("Has PathVariable annotation:" + ((ControlAnnotation.PathVariable) nowAnnotation).value());
//                }
//            }
//
//        }

        //获得当前方法对的所有注解的参数
        for (int parameterLocal = 0 ;parameterLocal < ParameterAnnotations.length;parameterLocal++) {
            //parameterLocal 为方法中参数的位置


            //将当前一个参数的所有注解逐一解析
            for (Annotation aParameter:ParameterAnnotations[parameterLocal]) {

                boolean isPathVariable = aParameter.annotationType().equals(ControlAnnotation.PathVariable.class);
                if (!isPathVariable) continue;

                LogOut.i("Has PathVariable annotation:" + ((ControlAnnotation.PathVariable) aParameter).value());

                String pvAnnotation = ((ControlAnnotation.PathVariable) aParameter).value();

                //尝试找出匹配的参数
                for (int matchFieldLocal = 0;matchFieldLocal < matchField.size();matchFieldLocal++){
                    if (!matchField.get(matchFieldLocal).equals(pvAnnotation)) continue;
                    parameterMatchGroups.put(parameterLocal, matchFieldLocal);
                }
            }

        }

        InvokeMsg.ParameterNamesMatch[] methodParameterTypes = null;
        try {
            methodParameterTypes = checkParameterType.getParameterTypes(methodParameterTypeNames, ParameterAnnotations);
        }catch (ParameterNameNotStringException e){
            throw new ParameterNameNotStringException(method);
        }
        String[] methodRequestParamTypes = checkParameterType.getRequestParamTypes(methodParameterTypeNames,ParameterAnnotations);
        CheckResponseWrite.ReturnStateToDo writeSate = checkResponseWrite.responseHandlerSate(method.getReturnType().getName());
        //Key组装后的URL
        fuzzyMap.put(matchCode,
                new InvokeMsg(
                        aClass.getName() ,
                        method.getName(),
                        methodParameterTypeNames,
                        methodParameterTypes,
                        methodRequestParamTypes,
                        writeSate,parameterMatchGroups
                )
        );

        LogOut.i("put matchURL:" + url + "  method.getName:" + method.getName() );

    }

    private void absolutelyMatch(String url,Method method,Class<?> aClass,Class<?>[] methodParameterTypeNames){
        //检查set表是否有增长，无这表示有重复的URL，需要打印LogOut.i
        int oldSetSize = absolutelyUrlSet.size();
        if (absolutelyUrlSet.contains(url)){
//        absolutelyUrlSet.add(url);
//        if (absolutelyUrlSet.size() <= oldSetSize && oldSetSize != 0){
//                    LogOut.i
//("oldSetSize:"+oldSetSize+" ,absolutelyUrlSet:"+absolutelyUrlSet.size()+" ,matchURl:"+matchURl);
            InvokeMsg first = absolutelyMap.get(url);
            if (first == null) return;

            LogOut.e("\nError:URL conflict to \'" + url + "\'" +
                    "\nFirst==>" + first.getClassName() + "." + first.getMethodName() +
                    "\nLost==>" + aClass.getName() + "." + method.getName()
            );
            return;
        }
        absolutelyUrlSet.add(url);

        InvokeMsg.ParameterNamesMatch[] methodParameterTypes = null;
        try {
            methodParameterTypes = checkParameterType.getParameterTypes(methodParameterTypeNames, method.getParameterAnnotations());
        }catch (ParameterNameNotStringException e){
            throw new ParameterNameNotStringException(method);
        }
        String[] methodRequestParamTypes = checkParameterType.getRequestParamTypes(methodParameterTypeNames,method.getParameterAnnotations());
        CheckResponseWrite.ReturnStateToDo writeSate = checkResponseWrite.responseHandlerSate(method.getReturnType().getName());

        //Key组装后的URL
        absolutelyMap.put(url,
                new InvokeMsg(
                        aClass.getName() ,
                        method.getName(),
                        methodParameterTypeNames,
                        methodParameterTypes,
                        methodRequestParamTypes,
                        writeSate
                )
        );

        LogOut.i("put matchURL:" + url + "  method.getName:" + method.getName());
    }

    class UrlPart {

        public String part;
        public String parameter;
        public boolean hasParameter;

        public UrlPart(String part, String parameter, boolean hasParameter) {
            this.part = part;
            this.parameter = parameter;
            this.hasParameter = hasParameter;
        }
    }



}
