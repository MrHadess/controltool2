package com.mh.controltool2.scan.annotation;

import com.mh.controltool2.ControlAnnotation;
import com.mh.controltool2.LogOut;
import com.mh.controltool2.annotation.PathVariable;
import com.mh.controltool2.annotation.RequestMapping;
import com.mh.controltool2.annotation.RestController;
import com.mh.controltool2.exceptions.scan.ParameterNameNotStringException;
import com.mh.controltool2.method.MethodInvokeInfo;
import com.mh.controltool2.method.URLInvokeTree;
import com.mh.controltool2.method.type.InvokeObjectInfo;
import com.mh.controltool2.scan.*;
import com.mh.controltool2.scan.fuzzymatch.FuzzyURLMatchToInfo;
import com.mh.controltool2.scan.fuzzymatch.MatchURLInfo;
import com.mh.controltool2.scan.fuzzymatch.SingleItemCheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerControl implements PackageProcessHandler {

    //通过Map Set 方式在添加单一元素前后比较是否有数值增长，有--》证明没有重复，没有--》有重复抛出当前重复项Key，以及类相关信息，用作重复排除，以及当前Map中对应的Value相关信息


    private CheckResponseWrite checkResponseWrite = new CheckResponseWrite();
    private CheckParameterType checkParameterType = new CheckParameterType();
    private SingleItemCheck fuzzySingleItemCheck = new SingleItemCheck();
    private FuzzyURLMatchToInfo fuzzyURLMatchToInfo = new FuzzyURLMatchToInfo();


    private Pattern urlWarnPattern = Pattern.compile("(/+|.*?)(/{2,})(/+|.*?)");//检查是否存在多个"/"斜杠以作出提示（需要附加相关响应控制器的位置信息）
    private Pattern checkVarMatch = Pattern.compile(".+?[{].+?[}].*?");//简单检查是否存在"XX/{ZZZZZ}/XXXX/{ZZZZZZ}/XXXXX"格式的字符串(存在时则认为存在相关动态变量，将进行相关流程处理)
//    private Pattern checkVarMatch = Pattern.compile(".+?[{].+?[}].+?");//简单检查是否存在"XX/{ZZZZZ}/XXXX/{ZZZZZZ}/XXXXX"格式的字符串(存在时则认为存在相关动态变量，将进行相关流程处理)
    private Pattern checkVarMatch2 = Pattern.compile(".+?/[{].+?[}]");//简单检查是否存在"XX/{ZZZZZ}/XXXX/{ZZZZZZ}"格式的字符串(存在时则认为存在相关动态变量，将进行相关流程处理)


    private HashMap<String,InvokeMsg> absolutelyMap;
    private HashMap<String,InvokeMsg> fuzzyMap;

    private HashMap<String,URLInvokeTree> urlAbsolutelyMap;
    private HashMap<String,URLInvokeTree> urlFuzzyMap;

//    private HashSet<String> absolutelyUrlSet;
    private HashSet<String> fuzzyUrlSet;

    @Override
    public void loadFullPackageData(List<Class<?>> classList) {

        absolutelyMap = new HashMap<>();
        fuzzyMap = new HashMap<>();

        urlAbsolutelyMap = new HashMap<>();
        urlFuzzyMap = new HashMap<>();

        fuzzyUrlSet = new HashSet<>();
        
        for (Class<?> item:classList) {
            RestController restControllerModuleClass = item.getAnnotation(RestController.class);
            //当此类不存在控制器时 不加入URL匹配Map中
            if (restControllerModuleClass == null) continue;
            LogOut.i("-----------------------------------------------------------");
            LogOut.i("This has RestController:::" + item);

            String className = item.getName();

            //控制器根Mapping前缀匹配
            String urlFirstMatch = "";
            RequestMapping requestMappingClass = item.getAnnotation(RequestMapping.class);
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
    }

    public LocalParameterData getLocalParameterData() {
        return new LocalParameterData(absolutelyMap,fuzzyMap);
    }

    private void methodCheck(Class<?> matchClass,String urlFirstMatch,String className){

        Method[] methods = matchClass.getDeclaredMethods();
        if (methods == null || methods.length <= 0) return;

        for (Method method : methods) {


            LogOut.i("++++++++++++++" );
            LogOut.i("Now method name:" + method.getName() );


            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                LogOut.i("Does not RequestMapping");
                continue;
            }


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
                fuzzyMatch(matchURl, method, matchClass, methodParameterTypeNames);
            } else {
                //absolutely match
                LogOut.i("Has AbsolutelyMatch");
                absolutelyMatch(matchURl, method, matchClass, methodParameterTypeNames);
            }


        }

    }

    private void fuzzyMatch(String url , Method method , Class<?> aClass , Class<?>[] methodParameterTypeNames ){

        //函数参数 正则配对分组参数
        HashMap<Integer,Integer> parameterMatchGroups = new HashMap<>();

        ArrayList<String> partList = new ArrayList<>();//URL模板 路径块

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

        //检查多重URL匹配
        if(fuzzyUrlSet.contains(matchCode)) {
            InvokeMsg first = fuzzyMap.get(matchCode);

            LogOut.e("\nError:URL conflict--> \'" + url + "\' regular--> \'" + matchCode + "\'" +
                    "\nFirst==>" + first.getClassName() + "." + first.getMethodName() +
                    "\nLost==>" + aClass.getName() + "." + method.getName()
            );
            return;
        }
        fuzzyUrlSet.add(matchCode);

        Annotation[][] ParameterAnnotations = method.getParameterAnnotations();

        //获得当前方法对的所有注解的参数
        for (int parameterLocal = 0 ;parameterLocal < ParameterAnnotations.length;parameterLocal++) {
            //parameterLocal 为方法中参数的位置


            //将当前一个参数的所有注解逐一解析
            for (Annotation aParameter:ParameterAnnotations[parameterLocal]) {

                boolean isPathVariable = aParameter.annotationType().equals(PathVariable.class);
                if (!isPathVariable) continue;

                LogOut.i("Has PathVariable annotation:" + ((PathVariable) aParameter).value());

                String pvAnnotation = ((PathVariable) aParameter).value();

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
        } catch (ParameterNameNotStringException e) {
            throw new ParameterNameNotStringException(method);
        }
        String[] methodRequestParamTypes = checkParameterType.getRequestParamTypes(methodParameterTypeNames,ParameterAnnotations);
        CheckResponseWrite.ReturnStateToDo writeSate = checkResponseWrite.responseHandlerSate(method.getReturnType().getName());
        //Key组装后的URL
        fuzzyMap.put(matchCode,
                new InvokeMsg(
                        aClass.getName(),
                        method.getName(),
                        methodParameterTypeNames,
                        methodParameterTypes,
                        methodRequestParamTypes,
                        writeSate,
                        parameterMatchGroups
                )
        );

        LogOut.i("put matchURL:" + url + "  method.getName:" + method.getName() );

    }

    private void absolutelyMatch(String url,Method method,Class<?> aClass,Class<?>[] methodParameterTypeNames) {

        URLInvokeTree urlInvokeTree = urlAbsolutelyMap.get(url);

        MethodInvokeInfo methodInvokeInfo = new MethodInvokeInfo();
        methodInvokeInfo.setClassname(aClass.getName());
        methodInvokeInfo.setMethodName(method.getName());

//        methodInvokeInfo.setInvokeObjectInfoGroup();

        // 检查函数注解以及类型完善InvokeObjectInfo,优先检查注解参数如无注解，则交由自动匹配处理器处理
//        for(method.getParameterAnnotations()) {
//
//        }

        if (urlInvokeTree == null) {

        } else {
//            urlInvokeTree.addInvokeInfo();
        }

//        if (absolutelyUrlSet.contains(url)){
////        absolutelyUrlSet.add(url);
////        if (absolutelyUrlSet.size() <= oldSetSize && oldSetSize != 0){
////                    LogOut.i
////("oldSetSize:"+oldSetSize+" ,absolutelyUrlSet:"+absolutelyUrlSet.size()+" ,matchURl:"+matchURl);
//            InvokeMsg first = absolutelyMap.get(url);
//            if (first == null) return;
//
//            LogOut.e("\nError:URL conflict to \'" + url + "\'" +
//                    "\nFirst==>" + first.getClassName() + "." + first.getMethodName() +
//                    "\nLost==>" + aClass.getName() + "." + method.getName()
//            );
//            return;
//        }

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
                        aClass.getName(),
                        method.getName(),
                        methodParameterTypeNames,
                        methodParameterTypes,
                        methodRequestParamTypes,
                        writeSate
                )
        );

        LogOut.i("put matchURL:" + url + "  method.getName:" + method.getName());
    }


}
