package org.sitecenter.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpDownloader {

    public static HttpDownloadResponse downloadWithHeaders(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.setRequestMethod("GET");
            connection.connect();

            int statusCode = connection.getResponseCode();
            Map<String, List<String>> headers = connection.getHeaderFields();

            try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
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
