package org.sitecenter.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegexpResponseTest {

    @Test
    void isRegexpFound() {

        String regexp = "(\\d{4})-(\\d{2})-(\\d{2})";
        String content = "Today's date is 2024-10-21.";

        RegexpResponse response = new RegexpResponse(regexp, content);
        assertTrue(response.isRegexpFound());
        assertEquals("2024", response.getMatchGroupValues().get(0));

        assertEquals("10", response.getMatchGroupValues().get(1));
        assertEquals("21", response.getMatchGroupValues().get(2));

        System.out.println("Regexp Found: " + response.isRegexpFound());
        System.out.println("Match Group Values: " + response.getMatchGroupValues());
    }

    @Test
    void simpleRegexpFound() {

        String regexp = "oday";
        String content = "Today's date is 2024-10-21.";

        RegexpResponse response = new RegexpResponse(regexp, content);
        assertTrue(response.isRegexpFound());
        assertEquals(0, response.getMatchGroupValues().size());
    }


    @Test
    void simpleRegexpNotFound() {

        String regexp = "Monday";
        String content = "Today's date is 2024-10-21.";

        RegexpResponse response = new RegexpResponse(regexp, content);
        assertFalse(response.isRegexpFound());
        assertEquals(0, response.getMatchGroupValues().size());

    }
}