package com.mh.controltool2.scan.fuzzymatch;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* 用于解析URL模板信息
*
* */
public class FuzzyURLMatchToInfo {

    private static Pattern checkItemMatch = Pattern.compile("\\{.+?}");

    private static final String MatchCode = "(.+?)";

    public static Pattern urlCoverTopPattern(String url) {

        //拼接为表达式的URL
        StringBuffer sb = new StringBuffer();
        Matcher matcher = checkItemMatch.matcher(url);
        while (matcher.find()){
            matcher.appendReplacement(sb,MatchCode);
        }
        matcher.appendTail(sb);

        return Pattern.compile(sb.toString());
    }


}
