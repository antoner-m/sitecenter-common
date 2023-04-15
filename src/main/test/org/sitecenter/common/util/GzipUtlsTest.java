package org.sitecenter.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GzipUtlsTest {

    @org.junit.jupiter.api.Test
    void compressDecompress() {
        byte source [] = "This is a sample test string for gzipping".getBytes(StandardCharsets.UTF_8);
        byte[] compressed = GzipUtls.gzipCompress(source);
        byte[] uncompressed = GzipUtls.gzipUncompress(compressed);
        assertEquals(source.length, uncompressed.length);
        assert(Arrays.equals(source, uncompressed));
    }
}