package org.sitecenter.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I18nTest {

    @BeforeEach
    void setUp() {
        I18n.loadTranslations(Map.of());
    }

    @Test
    void addTranslationsCreatesLocaleWhenMissing() {
        I18n.addTranslations("en", Map.of("hello", "Hello", "bye", "Bye"));

        assertEquals("Hello", I18n.tt("hello", "fallback", "en"));
        assertEquals("Bye", I18n.tt("bye", "fallback", "en"));
    }

    @Test
    void addTranslationsPreservesExistingKeysAndOverridesDuplicates() {
        I18n.addTranslations("en", Map.of("hello", "Hello", "bye", "Bye"));

        I18n.addTranslations("en", Map.of("bye", "Goodbye", "thanks", "Thanks"));

        assertEquals("Hello", I18n.tt("hello", "fallback", "en"));
        assertEquals("Goodbye", I18n.tt("bye", "fallback", "en"));
        assertEquals("Thanks", I18n.tt("thanks", "fallback", "en"));
    }

    @Test
    void addTranslationsDoesNotAffectOtherLocales() {
        I18n.addTranslations("en", Map.of("hello", "Hello"));
        I18n.addTranslations("ru", Map.of("hello", "Privet"));

        assertEquals("Hello", I18n.tt("hello", "fallback", "en"));
        assertEquals("Privet", I18n.tt("hello", "fallback", "ru"));
    }

    @Test
    void loadTranslationsStillReplacesLocale() {
        I18n.addTranslations("en", Map.of("hello", "Hello", "bye", "Bye"));

        I18n.loadTranslations("en", Map.of("hello", "Replaced"));

        assertEquals("Replaced", I18n.tt("hello", "fallback", "en"));
        assertEquals("fallback", I18n.tt("bye", "fallback", "en"));
    }
}
