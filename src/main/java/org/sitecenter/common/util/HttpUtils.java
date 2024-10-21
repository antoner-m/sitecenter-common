package org.sitecenter.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpUtils {

    public static byte[] download(String url) throws IOException {
        URL urlObj = new URL(url);
        try (BufferedInputStream in = new BufferedInputStream(urlObj.openStream());
             ByteArrayOutputStream baStream = new ByteArrayOutputStream()) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                baStream.write(dataBuffer, 0, bytesRead);
            }
            return baStream.toByteArray();
        } catch (IOException e) {
            throw e;
        }
    }

    public static String extractFileName(String url) {
        try {
            Path fileName = Paths.get(new URI(url).getPath()).getFileName();
            if (fileName == null) return null;
            return fileName.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
