package org.sitecenter.common.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class I18nLoaderTest {

    @Test
    void parseSingleLineJavaScriptWithMultipleLocales() throws Exception {
        String js = "export default { en: {'k1':'v1','k2':'v2'}, ru: {'k1':'r1'}, de: {'k':'d'} };";

        Map<String, Map<String, String>> parsed = parseJavaScriptTranslations(js);

        assertEquals(3, parsed.size());
        assertEquals(2, parsed.get("en").size());
        assertEquals(1, parsed.get("ru").size());
        assertEquals(1, parsed.get("de").size());
        assertEquals("v1", parsed.get("en").get("k1"));
        assertEquals("r1", parsed.get("ru").get("k1"));
        assertEquals("d", parsed.get("de").get("k"));
    }

    @Test
    void parseThreeLocalesWithDifferentKeyCountsAndMixedFormatting() throws Exception {
        String js = "export default {\n" +
                "  fr: {'only.one':'v1'},\n" +
                "  en: {\n" +
                "    'first':'v2',\n" +
                "    'second':'v3'\n" +
                "  },\n" +
                "  ru:{'one':'v4',\n" +
                "      'two':'v5', 'three':'v6'}\n" +
                "};";

        Map<String, Map<String, String>> parsed = parseJavaScriptTranslations(js);

        assertEquals(3, parsed.size());
        assertEquals(1, parsed.get("fr").size());
        assertEquals(2, parsed.get("en").size());
        assertEquals(3, parsed.get("ru").size());
        assertEquals("v1", parsed.get("fr").get("only.one"));
        assertEquals("v2", parsed.get("en").get("first"));
        assertEquals("v3", parsed.get("en").get("second"));
        assertEquals("v6", parsed.get("ru").get("three"));
    }

    @Test
    void doesNotCreateFakeLocalesFromPlaceholderValues() throws Exception {
        String js = "export default {\n" +
                "  en: {\n" +
                "    'a': 'Cannot find site-ip part by id: {{id}}',\n" +
                "    'b': 'Port check timed out: {{message}}',\n" +
                "    'c': 'No specific checks for this cms:{{cms}}',\n" +
                "    'd': 'Country information not found for ip: {{ip}}',\n" +
                "    'e': 'Unprocessed yet: {{message}}'\n" +
                "  },\n" +
                "  ru: {\n" +
                "    'a': 'Не удалось найти часть по id: {{id}}'\n" +
                "  }\n" +
                "};";

        Map<String, Map<String, String>> parsed = parseJavaScriptTranslations(js);

        assertEquals(2, parsed.size());
        assertTrue(parsed.containsKey("en"));
        assertTrue(parsed.containsKey("ru"));
        assertFalse(parsed.containsKey("id"));
        assertFalse(parsed.containsKey("ip"));
        assertFalse(parsed.containsKey("out"));
        assertFalse(parsed.containsKey("cms"));
        assertFalse(parsed.containsKey("yet"));
        assertEquals(5, parsed.get("en").size());
        assertEquals(1, parsed.get("ru").size());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String>> parseJavaScriptTranslations(String content) throws Exception {
        Method method = I18nLoader.class.getDeclaredMethod("parseJavaScriptTranslations", String.class);
        method.setAccessible(true);
        return (Map<String, Map<String, String>>) method.invoke(null, content);
    }
}
