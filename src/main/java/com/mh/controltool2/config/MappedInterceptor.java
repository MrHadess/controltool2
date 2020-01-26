package com.mh.controltool2.config;

import com.mh.controltool2.LogOut;
import com.mh.controltool2.scan.fuzzymatch.FuzzyURLMatchToInfo;
import com.mh.controltool2.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MappedInterceptor {

    private static final String TAG = "MappedInterceptor";

    private final HandlerInterceptor interceptor;

    private final Pattern[] includePatterns;
    private final Pattern[] excludePatterns;

    public MappedInterceptor(HandlerInterceptor interceptor,List<String> includePatterns,List<String> excludePatterns) {
        this.interceptor = interceptor;
        this.includePatterns = urlToPattern(includePatterns);
        this.excludePatterns = urlToPattern(excludePatterns);
    }

    /*
    * try match url
    * match includePatterns and unmatched excludePatterns
    * */
    public boolean match(String url) {
        for (Pattern includePatternItem:includePatterns) {
            Matcher matcher = includePatternItem.matcher(url);
            if (!matcher.matches()) continue;
            // check exclude pattern
            for (Pattern excludePatternItem:excludePatterns) {
                if (excludePatternItem.matcher(url).matches()) return false;
            }
            return true;
        }
        return false;
    }

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }

    private Pattern[] urlToPattern(List<String> list) {
        List<Pattern> patternList = new ArrayList<>();
        for (String url : list) {
            if (url == null) continue;
            Pattern pattern = FuzzyURLMatchToInfo.urlCoverToInterceptorPattern(url);
            if (pattern == null) {
                LogOut.i(TAG, String.format("URL is unsupported : %s", url));
                continue;
            }
            patternList.add(pattern);
        }
        return patternList.toArray(new Pattern[0]);
    }

    @Override
    public String toString() {
        return "MappedInterceptor{" +
                "interceptor=" + interceptor +
                ", includePatterns=" + Arrays.toString(includePatterns) +
                ", excludePatterns=" + Arrays.toString(excludePatterns) +
                '}';
    }
}
