package org.sitecenter.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailUtilsTest {

    @Test
    void extractEmail() {
        String inputString = "Hello, my email is john.doe@example.com. Please contact me!";
        String extractedEmail = EmailUtils.extractEmail(inputString);
        assertEquals("john.doe@example.com", extractedEmail);

        inputString = "% Abuse contact for '178.248.234.119 - 178.248.234.119' is 'abuse@qrator.net'";
        extractedEmail = EmailUtils.extractEmail(inputString);
        assertEquals("abuse@qrator.net", extractedEmail);

        inputString = "% Abuse contact for '178.248.234.119 - 178.248.234.119'is'abuse@qrator.net'dsadsadsa";
        extractedEmail = EmailUtils.extractEmail(inputString);
        assertEquals("abuse@qrator.net", extractedEmail);
    }
}