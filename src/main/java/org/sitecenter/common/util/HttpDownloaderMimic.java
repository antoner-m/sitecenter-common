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

import static org.sitecenter.common.util.HttpDownloader.sanitizeMap;

/**
 * A utility class for downloading content over HTTP, designed to mimic browser-like behavior
 * with default headers and customizable options for connection and read timeouts.
 */
public class HttpDownloaderMimic {
    // Default headers mimicking a real browser
    private static final String DEFAULT_USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final String DEFAULT_ACCEPT =
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8";
    private static final String DEFAULT_ACCEPT_LANGUAGE = "en-US,en;q=0.9";
    private static final String DEFAULT_ACCEPT_ENCODING = "identity";
    private static final String DEFAULT_CONNECTION = "keep-alive";
    private static final String DEFAULT_UPGRADE_INSECURE_REQUESTS = "1";

    public static HttpDownloadResponse download(String url) throws IOException {
        return download(url, 10, 30);
    }

    public static HttpDownloadResponse download(String url, int connectTimeoutSeconds, int readTimeoutSeconds) throws IOException {
        return download(url, connectTimeoutSeconds, readTimeoutSeconds, null);
    }

    /**
     * Download with custom headers support.
     * Default browser-like headers are applied unless overridden by customHeaders.
     *
     * @param url                   The URL to download
     * @param connectTimeoutSeconds Connection timeout
     * @param readTimeoutSeconds    Read timeout
     * @param customHeaders         Optional custom headers (can be null). These override default values.
     */
    public static HttpDownloadResponse download(String url, int connectTimeoutSeconds,
                                                int readTimeoutSeconds, Map<String, String> customHeaders) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(connectTimeoutSeconds * 1000);
            connection.setReadTimeout(readTimeoutSeconds * 1000);

            // Allow redirects
            connection.setInstanceFollowRedirects(true);

            // Set default headers only if not provided in customHeaders
            setHeaderIfAbsent(connection, customHeaders, "User-Agent", DEFAULT_USER_AGENT);
            setHeaderIfAbsent(connection, customHeaders, "Accept", DEFAULT_ACCEPT);
            setHeaderIfAbsent(connection, customHeaders, "Accept-Language", DEFAULT_ACCEPT_LANGUAGE);
            setHeaderIfAbsent(connection, customHeaders, "Accept-Encoding", DEFAULT_ACCEPT_ENCODING);
            setHeaderIfAbsent(connection, customHeaders, "Connection", DEFAULT_CONNECTION);
            setHeaderIfAbsent(connection, customHeaders, "Upgrade-Insecure-Requests", DEFAULT_UPGRADE_INSECURE_REQUESTS);

            // Apply any additional custom headers
            if (customHeaders != null) {
                for (Map.Entry<String, String> header : customHeaders.entrySet()) {
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            connection.connect();

            int statusCode = connection.getResponseCode();
            Map<String, List<String>> headers = connection.getHeaderFields();

            InputStream inputStream;
            if (statusCode >= 200 && statusCode < 300) {
                inputStream = connection.getInputStream();
            } else {
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
                return new HttpDownloadResponse(baStream.toByteArray(), sanitizeMap(headers), statusCode);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Sets a header on the connection only if it's not present in customHeaders.
     * Uses case-insensitive comparison for header names.
     */
    private static void setHeaderIfAbsent(HttpURLConnection connection,
                                          Map<String, String> customHeaders, String headerName, String defaultValue) {
        if (!containsHeaderIgnoreCase(customHeaders, headerName)) {
            connection.setRequestProperty(headerName, defaultValue);
        }
    }

    /**
     * Checks if customHeaders contains a header (case-insensitive).
     */
    private static boolean containsHeaderIgnoreCase(Map<String, String> customHeaders, String headerName) {
        if (customHeaders == null || customHeaders.isEmpty()) {
            return false;
        }
        for (String key : customHeaders.keySet()) {
            if (key.equalsIgnoreCase(headerName)) {
                return true;
            }
        }
        return false;
    }
}