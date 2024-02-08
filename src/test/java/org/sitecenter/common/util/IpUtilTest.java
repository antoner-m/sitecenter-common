package org.sitecenter.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IpUtilTest {

    @Test
    void ipToLong() {
//        Range start 2.0.0.0 = 33554432
//        Range end 8.255.255.255 = 150994943
//        Testing IP a 4.5.6.7 = 67438087
//        Testing IP b 60.70.80.90 = 1011241050
        long start1 = IpUtil.parseIPv4ToLong("2.0.0.0");
        assertEquals(33554432, start1);
        long end1 = IpUtil.parseIPv4ToLong("8.255.255.255");
        assertEquals(150994943, end1);

        long start2 = IpUtil.parseIPv4ToLong("4.5.6.7");
        assertEquals(67438087, start2);
        long end2 = IpUtil.parseIPv4ToLong("60.70.80.90");
        assertEquals(1011241050, end2);
    }
}