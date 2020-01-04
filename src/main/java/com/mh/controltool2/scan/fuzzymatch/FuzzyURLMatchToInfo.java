package com.mh.controltool2.scan.fuzzymatch;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* 用于解析URL模板信息
*
* */
public class FuzzyURLMatchToInfo {

    private Pattern checkItemMatch = Pattern.compile("\\{.+?}");

    private static final String MatchCode = "(.+?)";

    public MatchURLInfo matchURL(String fuzzyUrl){

        //用于存储当前的 URl模板的命名 用于Method的参数检查，以用作标记以进行数据注入
        ArrayList<String> matchField = new ArrayList<String>();

        //拼接为表达式的URL
        StringBuffer sb = new StringBuffer();

        Matcher matcher = checkItemMatch.matcher(fuzzyUrl);

        while (matcher.find()){

            String matchContentGroup = matcher.group();
            String matchContent = matchContentGroup.substring(1,matchContentGroup.length() - 1);
            matchField.add(matchContent);

            matcher.appendReplacement(sb,MatchCode);

        }

        matcher.appendTail(sb);

        return new MatchURLInfo(matchField,sb.toString());
    }


}
