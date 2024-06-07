package org.sitecenter.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinksExtractorUtilTest {

    @Test
    void extractURL() {
        String result = LinksExtractorUtil.extractURL("\"http://www.barracudanetworks.com/reputation/?pr=1&ip=178.148.34.103\"");
        assert("http://www.barracudanetworks.com/reputation/?pr=1&ip=178.148.34.103".equalsIgnoreCase(result));
    }
}