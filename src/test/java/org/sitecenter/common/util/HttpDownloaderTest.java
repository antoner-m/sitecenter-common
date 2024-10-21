package org.sitecenter.common.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpDownloaderTest {

    @Test
    void testDownloadWithHeaders() throws IOException {
        final String testUrl = "https://sitecenter.org";
        final String headerKey = "Content-Type";
        final String headerValue = "text/html;charset=UTF-8";
        final int testStatusCode = 200;
        final byte[] testContent = "Test content".getBytes();

        HttpDownloadResponse response = HttpDownloader.downloadWithHeaders(testUrl);

        assertEquals(testStatusCode, response.getStatusCode());

        Map<String, List<String>> headers = response.getHeaders();
        assertTrue(headers.containsKey(headerKey));
        assertEquals(headerValue, headers.get(headerKey).get(0));

        byte[] content = response.getContent();
        String contentStr = new String(content);
        assertTrue(contentStr.contains("</html>"));
    }
}