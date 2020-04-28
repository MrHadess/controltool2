package com.mh.controltool2.scan.fuzzymatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* 用于解析URL模板信息
*
* */
public class FuzzyURLMatchToInfo {

    private static Pattern checkItemMatch = Pattern.compile("\\{.+?}");
    private static final String MatchCode = "(.[^/]+?)";

    public static Pattern urlCoverToPattern(String url) {

        //拼接为表达式的URL
        StringBuffer sb = new StringBuffer();
        Matcher matcher = checkItemMatch.matcher(url);
        while (matcher.find()){
            matcher.appendReplacement(sb,MatchCode);
        }
        matcher.appendTail(sb);

        return Pattern.compile(sb.toString());
    }

    private static final Pattern CHECK_SUPPORT_MATCH_1 = Pattern.compile("(.[^*]*?/\\*|/\\*)");
    private static final Pattern CHECK_SUPPORT_MATCH_2 = Pattern.compile("(.[^*]*?/\\*\\*|/\\*\\*)");

    private static final String MATCH_CODE1 = "/*";
    private static final String TO_MATCH_CODE1 = "/.[^/]+?";

    private static final String MATCH_CODE2 = "/**";
    private static final String TO_MATCH_CODE2 = "/.+?";

    public static Pattern urlCoverToInterceptorPattern(String url) {
//        format to pattern
        Matcher match1 = CHECK_SUPPORT_MATCH_1.matcher(url);
        if (match1.matches()) {
            String regex = url.replace(MATCH_CODE1,TO_MATCH_CODE1);
            return Pattern.compile(regex);
        }

        Matcher match2 = CHECK_SUPPORT_MATCH_2.matcher(url);
        if (match2.matches()) {
            String regex = url.replace(MATCH_CODE2,TO_MATCH_CODE2);
            return Pattern.compile(regex);
        }

        return null;
    }


}
