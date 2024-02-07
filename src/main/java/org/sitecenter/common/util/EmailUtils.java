package org.sitecenter.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {
    public static String extractEmail(String input) {
        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }
}
