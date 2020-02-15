package com.mh.controltool2.scan.annotation;

import com.mh.controltool2.annotation.RequestMapping;
import com.mh.controltool2.annotation.RestController;
import com.mh.controltool2.exceptions.RepeatURLMethodException;
import com.mh.controltool2.method.MethodInvokeInfo;
import com.mh.controltool2.method.URLInvokeTree;
import com.mh.controltool2.method.type.InvokeObjectInfo;
import com.mh.controltool2.scan.PackageProcessHandler;
import com.mh.controltool2.scan.annotation.handler.control.CheckMatchInvokeInfo;
import com.mh.controltool2.scan.fuzzymatch.FuzzyURLMatchToInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerControl implements PackageProcessHandler {

    private static Logger logger = LoggerFactory.getLogger("HandlerControl");

    private Pattern urlWarnPattern = Pattern.compile("(/+|.*?)(/{2,})(/+|.*?)");//检查是否存在多个"/"斜杠以作出提示（需要附加相关响应控制器的位置信息）
    private Pattern checkVarMatch = Pattern.compile(".+?[{].+?[}].*?");//简单检查是否存在"XX/{ZZZZZ}/XXXX/{ZZZZZZ}/XXXXX"格式的字符串(存在时则认为存在相关动态变量，将进行相关流程处理)
//    private Pattern checkVarMatch = Pattern.compile(".+?[{].+?[}].+?");//简单检查是否存在"XX/{ZZZZZ}/XXXX/{ZZZZZZ}/XXXXX"格式的字符串(存在时则认为存在相关动态变量，将进行相关流程处理)
    private Pattern checkVarMatch2 = Pattern.compile(".+?/[{].+?[}]");//简单检查是否存在"XX/{ZZZZZ}/XXXX/{ZZZZZZ}"格式的字符串(存在时则认为存在相关动态变量，将进行相关流程处理)

    private CheckMatchInvokeInfo checkMatchInvokeInfo = new CheckMatchInvokeInfo();

    private HashMap<String,URLInvokeTree> urlAbsolutelyMap;
    private HashMap<Pattern,URLInvokeTree> urlFuzzyMap;

    @Override
    public void loadFullPackageData(List<Class<?>> classList) {

        urlAbsolutelyMap = new HashMap<>();
        urlFuzzyMap = new HashMap<>();
        
        for (Class<?> item:classList) {
            RestController restControllerModuleClass = item.getAnnotation(RestController.class);
            //当此类不存在控制器时 不加入URL匹配Map中
            if (restControllerModuleClass == null) continue;

            String className = item.getName();
            //控制器根Mapping前缀匹配
            String urlFirstMatch = "";
            RequestMapping requestMappingClass = item.getAnnotation(RequestMapping.class);
            if (requestMappingClass != null) {
                String value = requestMappingClass.value().trim();
                if (!value.isEmpty()) {
                    urlFirstMatch = value;
                }
            }

            //Method check
            methodCheck(item,urlFirstMatch,className);
        }
    }

    private void methodCheck(Class<?> matchClass,String urlFirstMatch,String className){

        Method[] methods = matchClass.getDeclaredMethods();
        if (methods == null || methods.length <= 0) return;

        for (Method method : methods) {

            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                printErrLog(matchClass,method,"Does not RequestMapping");
                continue;
            }


            String requestMappingValue = requestMapping.value().trim();
            if (requestMappingValue.isEmpty()) {
                printErrLog(matchClass,method,"Does not RequestMapping Value");
                continue;
            }

            String matchURl = urlFirstMatch + requestMappingValue;

            //URL格式检查，URL拼接后"/"是否存在连续重叠的问题
            Matcher urlWarnMatch = urlWarnPattern.matcher(matchURl);
            if (urlWarnMatch.matches()) {
                printErrLog(matchClass,method,"Warn:URL warn\'" + matchURl + "\' Class:" + className + "." + method.getName());
                continue;
            }

            //对URl进行分析,分析是否采取模糊匹配(URL模板)进行匹配
            Matcher urlMatchModul = checkVarMatch.matcher(matchURl);
            Matcher urlMatchModul2 = checkVarMatch2.matcher(matchURl);

            boolean isFuzzyURL = urlMatchModul.matches() || urlMatchModul2.matches();

            MethodInvokeInfo methodInvokeInfo = new MethodInvokeInfo();
            methodInvokeInfo.setTargetClass(matchClass);
            methodInvokeInfo.setTargetMethod(method);

            InvokeObjectInfo[] invokeObjectInfoGroup = null;
            invokeObjectInfoGroup = checkMatchInvokeInfo.checkToInvokeObjectGroup(matchURl,method);

            methodInvokeInfo.setInvokeObjectInfoGroup(invokeObjectInfoGroup);

            try {
                if (isFuzzyURL) {
                    //fuzzy match
                    updateFuzzyMatch(matchURl,methodInvokeInfo,requestMapping);
                } else {
                    //absolutely match
                    updateAbsolutelyMatch(matchURl,methodInvokeInfo,requestMapping);
                }
            } catch (RepeatURLMethodException e) {
                String message = String.format("Has replace url {first:'%s.%s',lost:'%s.%s'}",e.getOldClass(),e.getOldMethod(),e.getNewClass(),e.getNewMethod());
                printErrLog(matchClass,method,message);
            }

            logger.info(String.format("%s.%s--Handler mapping to url:'%s'",matchClass.getName(),method.getName(),matchURl));

        }

    }

    private void updateFuzzyMatch(String matchURl, MethodInvokeInfo methodInvokeInfo, RequestMapping requestMapping) throws RepeatURLMethodException {

        Pattern urlPattern = FuzzyURLMatchToInfo.urlCoverToPattern(matchURl);
        URLInvokeTree urlInvokeTree = urlFuzzyMap.get(urlPattern);
        if (urlInvokeTree != null) {
            urlInvokeTree.addInvokeInfo(methodInvokeInfo,requestMapping.method());
        } else {
            urlInvokeTree = new URLInvokeTree();
            urlInvokeTree.addInvokeInfo(methodInvokeInfo,requestMapping.method());
            urlFuzzyMap.put(urlPattern,urlInvokeTree);
        }

    }

    private void updateAbsolutelyMatch(String matchURl, MethodInvokeInfo methodInvokeInfo, RequestMapping requestMapping) throws RepeatURLMethodException {

        URLInvokeTree urlInvokeTree = urlAbsolutelyMap.get(matchURl);
        if (urlInvokeTree != null) {
            urlInvokeTree.addInvokeInfo(methodInvokeInfo,requestMapping.method());
        } else {
            urlInvokeTree = new URLInvokeTree();
            urlInvokeTree.addInvokeInfo(methodInvokeInfo,requestMapping.method());
            urlAbsolutelyMap.put(matchURl,urlInvokeTree);
        }

    }

    private void printErrLog(Class<?> tClass, Method method, String message) {
        logger.error(String.format("%s.%s  %s",tClass.getName(),method.getName(),message));
    }

    public HashMap<String, URLInvokeTree> getUrlAbsolutelyMap() {
        return urlAbsolutelyMap;
    }

    public HashMap<Pattern, URLInvokeTree> getUrlFuzzyMap() {
        return urlFuzzyMap;
    }
}
