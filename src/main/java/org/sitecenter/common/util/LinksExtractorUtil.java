package org.sitecenter.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinksExtractorUtil {

    public static String extractURL(String str) {
        List<String> urls = extractURLList(str);
        if (!urls.isEmpty())
            return urls.get(0);
        return null;
    }

    // Function to extract all the URLs from the string
    public static List<String> extractURLList(String str) {
        // Creating an empty ArrayList to store URLs
        ArrayList<String> urlList = new ArrayList<>();

        // Regular Expression to extract URL from the string
        String regexStr = "\\b((?:https?|ftp|file):"
                + "\\/\\/[a-zA-Z0-9+&@#\\/%?=~_|!:,.;]*"
                + "[a-zA-Z0-9+&@#\\/%=~_|])";

        // Compile the Regular Expression pattern
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE);

        // Create a Matcher that matches the pattern with the input string
        Matcher matcher = pattern.matcher(str);

        // Find and add all matching URLs to the ArrayList
        while (matcher.find()) {
            // Add the matched URL to the ArrayList
            urlList.add(matcher.group());
        }
        return urlList;
    }
}
