package org.sitecenter.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpResponse {
    private boolean regexpFound;
    private List<List<String>> matchGroupValues;

    public RegexpResponse(String regexp, String content) {
        this.matchGroupValues = new ArrayList<>();
        this.regexpFound = false; // Initialize to false by default

        if (regexp != null && !regexp.isEmpty()) {
            Pattern pattern = Pattern.compile(regexp);

            // Split the content into individual lines
            String[] lines = content.split("\\R"); // \\R matches any line break sequence

            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);

                // Search each line for matches
                if (matcher.find()) {
                    List<String> matchRow = new ArrayList<>();
                    this.regexpFound = true;

                    // Extract all groups from the match
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        matchRow.add(matcher.group(i));
                    }
                    this.matchGroupValues.add(matchRow);
                }
            }
        }
    }

    public boolean isRegexpFound() {
        return regexpFound;
    }

    public List<String> getMatchGroupValues() {
        if (matchGroupValues.isEmpty()) {
            return new ArrayList<>();
        }
        return matchGroupValues.get(0);
    }

    public List<List<String>> getMatchGroupAllValues() {
        return matchGroupValues;
    }
}
