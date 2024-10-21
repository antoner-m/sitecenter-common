package org.sitecenter.common.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class HttpDownloadResponse implements Serializable {
        private byte[] content;
        private Map<String, List<String>> headers;
        private int statusCode;

        public HttpDownloadResponse() {
        }
        public HttpDownloadResponse(byte[] content, Map<String, List<String>> headers, int statusCode) {
            this.content = content;
            this.headers = headers;
            this.statusCode = statusCode;
        }

        public byte[] getContent() {
            return content;
        }

        public Map<String, List<String>> getHeaders() {
            return headers;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }