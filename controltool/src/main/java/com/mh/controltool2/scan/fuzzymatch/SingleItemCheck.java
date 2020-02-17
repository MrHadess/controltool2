package com.mh.controltool2.scan.fuzzymatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
* 用于检查单个Url模板的状态，针对特殊描述的URl尝试进行解析，以适配URL预想设定
* A. aaa{bbb}ccc
* B. aaa{bbb}
* C. {aaa}bbb
*
* 应返回
* */
public class SingleItemCheck {

//    private Pattern checkMatchGroup = Pattern.compile(".+?[{].+?[}].+?");//检查模型 XXX{XXX}XXX
//    private Pattern checkMatchGroup2 = Pattern.compile(".+?[{].+?[}].+?");//检查模型 XXX{XXX}XXX
//    private Pattern checkMatchGroup3 = Pattern.compile(".+?[{].+?[}].+?");//检查模型 XXX{XXX}XXX

    //如果匹配结果为True视为此值为存在动态变量取出相关变量，False视为常量
    private Pattern checkMatchGroupFirst = Pattern.compile("\\{(.+?)}");//检查模型 {XXX}
    private Pattern checkMatchGroup = Pattern.compile("(.+?)[{](.+?)[}](.+?)");//检查模型 XXX{XXX}XXX
    private Pattern checkMatchGroup2 = Pattern.compile("(.+?)[{](.+?)[}]");//检查模型 XXX{XXX}
    private Pattern checkMatchGroup3 = Pattern.compile("[{](.+?)[}](.+?)");//检查模型 XXX{XXX}XXX

    //正则表达式匹配符
    private static final String MatchCode = "(.+?)";


    public MatchItemInfo checkItem(String item){

        Matcher matcher;

        matcher = checkMatchGroupFirst.matcher(item);
        if (matcher.matches()){
            return new MatchItemInfo(MatchCode,matcher.group(1));
        }

        //尝试匹配模型 aaa{bbb}ccc
        matcher = checkMatchGroup.matcher(item);
        if (matcher.matches()){
            String itemToPattern = matcher.group(1) + MatchCode + matcher.group(3);
            return new MatchItemInfo(itemToPattern,matcher.group(2));
        }

        //尝试匹配模型 aaa{bbb}
        matcher = checkMatchGroup2.matcher(item);
        if (matcher.matches()){
            String itemToPattern = matcher.group(1) + MatchCode;
            return new MatchItemInfo(itemToPattern,matcher.group(2));
        }

        //尝试匹配模型 {bbb}ccc
        matcher = checkMatchGroup3.matcher(item);
        if (matcher.matches()){
            String itemToPattern = MatchCode + matcher.group(2);
            return new MatchItemInfo(itemToPattern,matcher.group(1));
        }


        return null;
    }

}
