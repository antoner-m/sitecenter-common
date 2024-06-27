package org.sitecenter.common.util;

public class StringUtils {
    /** Remove textbetween start signal and end signal including signal itself.*/
    public static String removeBetween(String str, String start, String end) {
        if (str == null)
            return null;
        int idx = str.indexOf(start);

        if (idx >= 0) {
            int endIdx = str.indexOf(end, idx);
            String result = str.substring(0, idx) + str.substring(endIdx + end.length());
            return result;
        }
        return str;
    }
}
