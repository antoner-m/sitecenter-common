package org.sitecenter.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to load translations from JavaScript-like files using plain Java
 * No external dependencies required
 */
public class I18nLoader {
    
    /**
     * Load translations from a JavaScript file (like translations.js)
     * This method parses the JS export default structure manually
     * 
     * @param resourcePath Path to the JavaScript file in resources
     * @return Map of locale -> translation map
     * @throws IOException if file cannot be read or parsed
     */
    public static Map<String, Map<String, String>> loadFromJavaScriptFile(String resourcePath) throws IOException {
        String content = readResourceFile(resourcePath);
        return parseJavaScriptTranslations(content);
    }
    
    /**
     * Load translations from a simple JSON-like file
     * 
     * @param resourcePath Path to the JSON file in resources
     * @return Map of locale -> translation map
     * @throws IOException if file cannot be read or parsed
     */
    public static Map<String, Map<String, String>> loadFromJsonFile(String resourcePath) throws IOException {
        String content = readResourceFile(resourcePath);
        return parseJsonTranslations(content);
    }
    
    /**
     * Initialize I18n with translations from a file
     * 
     * @param resourcePath Path to the translation file
     * @param isJavaScript true if it's a JS file, false if it's JSON
     */
    public static void initializeI18nFromFile(String resourcePath, boolean isJavaScript) {
        try {
            Map<String, Map<String, String>> allTranslations;
            
            if (isJavaScript) {
                allTranslations = loadFromJavaScriptFile(resourcePath);
            } else {
                allTranslations = loadFromJsonFile(resourcePath);
            }
            
            // Load all translations into I18n
            for (Map.Entry<String, Map<String, String>> entry : allTranslations.entrySet()) {
                String locale = entry.getKey();
                Map<String, String> translations = entry.getValue();
                I18n.loadTranslations(locale, translations);
            }
            
            System.out.println("Successfully loaded translations for " + allTranslations.size() + " locales");
            
        } catch (IOException e) {
            System.err.println("Failed to load translations from " + resourcePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Parse JavaScript translations manually
     */
    private static Map<String, Map<String, String>> parseJavaScriptTranslations(String content) {
        Map<String, Map<String, String>> translations = new HashMap<>();
        
        // Remove comments and clean whitespace
        String cleaned = removeComments(content);
        
        // Find the export default block
        String objectContent = extractExportDefaultObject(cleaned);
        
        // Parse each locale block
        parseLocaleBlocks(objectContent, translations);
        
        return translations;
    }
    
    /**
     * Parse simple JSON-like translations manually
     */
    private static Map<String, Map<String, String>> parseJsonTranslations(String content) {
        Map<String, Map<String, String>> translations = new HashMap<>();
        
        // Remove comments and clean
        String cleaned = removeComments(content).trim();
        
        // Remove outer braces
        if (cleaned.startsWith("{") && cleaned.endsWith("}")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
        }
        
        parseLocaleBlocks(cleaned, translations);
        
        return translations;
    }
    
    /**
     * Remove JavaScript/JSON comments
     */
    private static String removeComments(String content) {
        // Remove single line comments
        content = content.replaceAll("//.*", "");
        // Remove multi-line comments
        content = content.replaceAll("/\\*[\\s\\S]*?\\*/", "");
        return content;
    }
    
    /**
     * Extract the object content from export default { ... }
     */
    private static String extractExportDefaultObject(String content) {
        Pattern exportPattern = Pattern.compile("export\\s+default\\s*\\{", Pattern.CASE_INSENSITIVE);
        Matcher matcher = exportPattern.matcher(content);
        
        if (!matcher.find()) {
            throw new IllegalArgumentException("Could not find 'export default {' in JavaScript file");
        }
        
        int startIndex = matcher.end() - 1; // Include the opening brace
        int braceCount = 0;
        int endIndex = -1;
        
        // Find matching closing brace
        for (int i = startIndex; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    endIndex = i;
                    break;
                }
            }
        }
        
        if (endIndex == -1) {
            throw new IllegalArgumentException("Could not find matching closing brace for export default");
        }
        
        // Return content without outer braces
        return content.substring(startIndex + 1, endIndex);
    }
    
    /**
     * Parse locale blocks from the object content
     */
    private static void parseLocaleBlocks(String content, Map<String, Map<String, String>> translations) {
        int i = 0;
        while (i < content.length()) {
            // Skip separators and whitespace between top-level locale blocks.
            while (i < content.length() && (Character.isWhitespace(content.charAt(i)) || content.charAt(i) == ',')) {
                i++;
            }
            if (i >= content.length()) {
                break;
            }

            int keyStart = i;
            while (i < content.length() && content.charAt(i) >= 'a' && content.charAt(i) <= 'z') {
                i++;
            }
            int keyLen = i - keyStart;
            if (keyLen < 2 || keyLen > 3) {
                i = keyStart + 1;
                continue;
            }

            String locale = content.substring(keyStart, i);
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i >= content.length() || content.charAt(i) != ':') {
                i = keyStart + 1;
                continue;
            }
            i++; // skip ':'
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i >= content.length() || content.charAt(i) != '{') {
                i = keyStart + 1;
                continue;
            }

            int blockStart = i;
            int blockEnd = findMatchingBrace(content, blockStart);
            if (blockEnd == -1) {
                break;
            }

            String localeContent = content.substring(blockStart + 1, blockEnd);
            Map<String, String> localeTranslations = parseTranslationEntries(localeContent);
            translations.put(locale, localeTranslations);

            i = blockEnd + 1;
        }
    }

    private static int findMatchingBrace(String content, int openBraceIndex) {
        int braceCount = 0;
        boolean inString = false;
        char quoteChar = 0;

        for (int i = openBraceIndex; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inString) {
                if (c == '\\') {
                    i++; // Skip escaped char in string
                    continue;
                }
                if (c == quoteChar) {
                    inString = false;
                }
                continue;
            }

            if (c == '"' || c == '\'') {
                inString = true;
                quoteChar = c;
                continue;
            }

            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    return i;
                }
            }
        }

        return -1;
    }
    
    /**
     * Parse individual translation entries within a locale block
     */
    private static Map<String, String> parseTranslationEntries(String content) {
        Map<String, String> entries = new HashMap<>();

        // Pattern to match: "key": "value" or 'key': 'value'
        // Updated to handle any content in values including colons, using negative lookahead
        Pattern entryPattern = Pattern.compile("([\"'])([^\"']+)\\1\\s*:\\s*([\"'])(((?!\\3).)*?)\\3", Pattern.DOTALL);
        Matcher matcher = entryPattern.matcher(content);
        
        while (matcher.find()) {
            String key = matcher.group(2);
            String value = matcher.group(4);
            
            // Unescape common escape sequences
            value = unescapeString(value);
            entries.put(key, value);
        }
        
        return entries;
    }
    
    /**
     * Unescape common string escape sequences
     */
    private static String unescapeString(String str) {
        return str.replace("\\\"", "\"")
                 .replace("\\'", "'")
                 .replace("\\\\", "\\")
                 .replace("\\n", "\n")
                 .replace("\\r", "\r")
                 .replace("\\t", "\t");
    }
    
    /**
     * Read a file from resources
     */
    private static String readResourceFile(String resourcePath) throws IOException {
        try (InputStream inputStream = I18nLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            
            return content.toString();
        }
    }
    
    /**
     * Convert JavaScript translations to clean JSON format for easier parsing
     */
    public static String convertToJson(String jsContent) {
        try {
            Map<String, Map<String, String>> translations = parseJavaScriptTranslations(jsContent);
            return mapToJsonString(translations);
        } catch (Exception e) {
            System.err.println("Failed to convert to JSON: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Convert map to JSON string manually
     */
    private static String mapToJsonString(Map<String, Map<String, String>> translations) {
        StringBuilder json = new StringBuilder("{\n");
        
        String[] locales = translations.keySet().toArray(new String[0]);
        for (int i = 0; i < locales.length; i++) {
            String locale = locales[i];
            Map<String, String> localeTranslations = translations.get(locale);
            
            json.append("  \"").append(locale).append("\": {\n");
            
            String[] keys = localeTranslations.keySet().toArray(new String[0]);
            for (int j = 0; j < keys.length; j++) {
                String key = keys[j];
                String value = localeTranslations.get(key);
                
                json.append("    \"").append(escapeJsonString(key)).append("\": \"")
                    .append(escapeJsonString(value)).append("\"");
                
                if (j < keys.length - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            
            json.append("  }");
            if (i < locales.length - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Escape string for JSON
     */
    private static String escapeJsonString(String str) {
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
}
