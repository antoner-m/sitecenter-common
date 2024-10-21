package org.sitecenter.common.util;

import org.sitecenter.common.struct.PairList;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpDownloader {
    public static Map<String, List<String>> sanitizeMap(Map<String, List<String>> map) {
        Map<String, List<String>> sanitizedMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getKey() != null) {
                sanitizedMap.put(entry.getKey(), entry.getValue());
            } else {
                sanitizedMap.put("empty-key", entry.getValue());
            }
        }
        return sanitizedMap;
    }

    public static HttpDownloadResponse downloadWithHeaders(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.setRequestMethod("GET");
            connection.connect();

            int statusCode = connection.getResponseCode();
            Map<String, List<String>> headers = connection.getHeaderFields();

            InputStream inputStream;
            if (statusCode >= 200 && statusCode < 300) {
                // Success responses (2xx)
                inputStream = connection.getInputStream();
            } else {
                // Error responses (4xx, 5xx)
                inputStream = connection.getErrorStream();
            }

            if (inputStream == null) {
                return new HttpDownloadResponse(new byte[]{}, headers, statusCode);
            }
            try (BufferedInputStream in = new BufferedInputStream(inputStream);
                 ByteArrayOutputStream baStream = new ByteArrayOutputStream()) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    baStream.write(dataBuffer, 0, bytesRead);
                }
                return new HttpDownloadResponse(baStream.toByteArray(), headers, statusCode);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            connection.disconnect();
        }
    }
}
