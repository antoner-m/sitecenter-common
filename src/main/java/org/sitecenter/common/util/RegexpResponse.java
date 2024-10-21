package org.sitecenter.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpResponse {
    private boolean regexpFound;
    private List<String> matchGroupValues;

    public RegexpResponse(String regexp, String content) {
        this.matchGroupValues = new ArrayList<>();
        if (regexp != null && !regexp.isEmpty()) {
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(content);
            
            if (matcher.find()) {
                this.regexpFound = true;
                
                // Extract all groups from the match
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    this.matchGroupValues.add(matcher.group(i));
                }
            } else {
                this.regexpFound = false;
            }
        } else {
            this.regexpFound = false;
        }
    }

    public boolean isRegexpFound() {
        return regexpFound;
    }

    public List<String> getMatchGroupValues() {
        return matchGroupValues;
    }
}
