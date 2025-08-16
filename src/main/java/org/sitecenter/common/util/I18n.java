package org.sitecenter.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Java equivalent of the JavaScript i18n translation functions
 */
public class I18n {
    
    // Cache for compiled regex patterns for better performance
    private static final Map<String, Pattern> regexCache = new ConcurrentHashMap<>();
    
    // Translations map - load this from files/database
    private static final Map<String, Map<String, String>> translations = new HashMap<>();
    
    /**
     * Internal translation function - equivalent to the translate() function in JS
     * @param locale The locale to use
     * @param key The translation key
     * @param defaultValue Default value if translation not found
     * @param vars Variables to substitute in the translation
     * @return Translated and interpolated string
     */
    private static String translate(String locale, String key, String defaultValue, Map<String, Object> vars) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("No key provided to translate()");
        }
        
        if (locale == null || locale.isEmpty()) {
            System.out.println("i18n: no locale found for " + locale + "." + key);
            return defaultValue != null ? defaultValue : key;
        }
        
        // Get translations for the locale
        Map<String, String> localeTranslations = translations.get(locale);
        String text = null;
        
        if (localeTranslations != null) {
            text = localeTranslations.get(key);
        }
        
        // Fallback to English if translation not found
        if (text == null) {
            if (!locale.equals("en")) {
                System.out.println("i18n: no translation found for " + locale + " key=" + key);
                Map<String, String> enTranslations = translations.get("en");
                if (enTranslations != null) {
                    text = enTranslations.get(key);
                }
            }
            if (text == null) {
                text = defaultValue != null ? defaultValue : key;
            }
        }
        
        // Replace variables if provided
        if (vars != null && !vars.isEmpty()) {
            for (Map.Entry<String, Object> entry : vars.entrySet()) {
                String varKey = entry.getKey();
                String varValue = String.valueOf(entry.getValue());
                
                // Create regex pattern for {{variableName}}
                String patternKey = "{{" + varKey + "}}";
                Pattern pattern = regexCache.computeIfAbsent(patternKey, 
                    k -> Pattern.compile("\\{\\{" + Pattern.quote(varKey) + "\\}\\}", Pattern.LITERAL));
                
                text = text.replace("{{" + varKey + "}}", varValue);
            }
        }
        
        return text;
    }
    
    /**
     * Equivalent to the 't' derived store function in JS
     * Translates key to specified locale with reactive behavior simulation
     * @param key Translation key
     * @param defaultValue Default value if translation not found
     * @param vars Variables for interpolation
     * @param locale Locale to use for translation
     * @return Translated string
     */
    public static String t(String key, String defaultValue, Map<String, Object> vars, String locale) {
        return translate(locale, key, defaultValue, vars);
    }
    
    /**
     * Overloaded version without variables
     */
    public static String t(String key, String defaultValue, String locale) {
        return t(key, defaultValue, null, locale);
    }
    
    /**
     * Overloaded version with only key and locale (uses key as default)
     */
    public static String t(String key, String locale) {
        return t(key, key, null, locale);
    }
    
    /**
     * Equivalent to the 'tt' function in JS
     * Translates key to specified locale - static translation
     * @param key Translation key
     * @param defaultValue Default value if translation not found
     * @param vars Variables for interpolation
     * @param locale Locale to use for translation
     * @return Translated string
     */
    public static String tt(String key, String defaultValue, Map<String, Object> vars, String locale) {
        return translate(locale, key, defaultValue != null ? defaultValue : key, vars);
    }
    
    /**
     * Overloaded version without variables
     */
    public static String tt(String key, String defaultValue, String locale) {
        return tt(key, defaultValue, null, locale);
    }
    
    /**
     * Overloaded version with only key and locale
     */
    public static String tt(String key, String locale) {
        return tt(key, null, null, locale);
    }
    
    /**
     * Equivalent to the 'tv' function in JS
     * Translates value to localized text based on prefixes
     * - t:key -> translates the key
     * - d:dateString -> formats the date
     * @param value The value to process
     * @param locale Locale to use for translation
     * @return Processed/translated value
     */
    public static String tv(String value, String locale) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        
        if (value.startsWith("t:")) {
            // Translation prefix
            String translationKey = value.substring(2); // Remove 't:'
            return tt(translationKey, translationKey, locale);
        } else if (value.startsWith("d:")) {
            // Date prefix
            String dateValue = value.substring(2); // Remove 'd:'
            try {
                // Parse ISO date string and format it
                LocalDateTime dateTime = LocalDateTime.parse(dateValue.replace("Z", ""), 
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (Exception e) {
                System.err.println("i18n: Error parsing date value: " + dateValue);
                return dateValue; // Return original if parsing fails
            }
        }
        
        return value; // Return raw value if no prefix
    }
    
    /**
     * Convenience method to create a variables map
     * @param keyValuePairs Alternating key-value pairs
     * @return Map of variables
     */
    public static Map<String, Object> vars(Object... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Key-value pairs must be even number of arguments");
        }
        
        Map<String, Object> vars = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            vars.put(String.valueOf(keyValuePairs[i]), keyValuePairs[i + 1]);
        }
        return vars;
    }
    
    /**
     * Add translations programmatically
     * @param locale Locale code
     * @param key Translation key
     * @param value Translation value
     */
    public static void addTranslation(String locale, String key, String value) {
        translations.computeIfAbsent(locale, k -> new HashMap<>()).put(key, value);
    }
    
    /**
     * Load translations from a map
     * @param locale Locale code
     * @param translationMap Map of key-value translations
     */
    public static void loadTranslations(String locale, Map<String, String> translationMap) {
        translations.put(locale, new HashMap<>(translationMap));
    }
}