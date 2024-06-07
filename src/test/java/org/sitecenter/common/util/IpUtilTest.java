package org.sitecenter.common.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IpUtilTest {
    @Test
    void ipPrivateRange() {
        assertEquals(2130706432, IpUtil.ipV4ToLong("127.0.0.0"));
        assertEquals(2147483647, IpUtil.ipV4ToLong("127.255.255.255"));

        assertEquals(2851995648L, IpUtil.ipV4ToLong("169.254.0.0"));
        assertEquals(2852061183L, IpUtil.ipV4ToLong("169.254.255.255"));

        assertEquals(3758096384L, IpUtil.ipV4ToLong("224.0.0.0"));
        assertEquals(4026531839L, IpUtil.ipV4ToLong("239.255.255.255"));

        assertEquals(3221225472L, IpUtil.ipV4ToLong("192.0.0.0"));
        assertEquals(3221225727L, IpUtil.ipV4ToLong("192.0.0.255"));


        assert(IpUtil.ipv4IsPrivateRange("192.168.1.1"));
        assert(IpUtil.ipv4IsPrivateRange("172.31.255.255"));

        assert(IpUtil.ipv4IsPrivateRange("192.0.0.0"));
        assert(IpUtil.ipv4IsPrivateRange("192.0.0.255"));
        assertFalse(IpUtil.ipv4IsPrivateRange("192.1.0.0"));

        assert(IpUtil.ipv4IsPrivateRange("192.168.0.0"));
        assert(IpUtil.ipv4IsPrivateRange("192.168.255.255"));
        assert(IpUtil.ipv4IsPrivateRange("172.16.0.0"));
        assert(IpUtil.ipv4IsPrivateRange("172.31.255.255"));
        assert(IpUtil.ipv4IsPrivateRange("10.0.0.0"));
        assert(IpUtil.ipv4IsPrivateRange("10.255.255.255"));
        assert(IpUtil.ipv4IsPrivateRange("169.254.1.1"));
        assert(IpUtil.ipv4IsPrivateRange("169.254.255.255"));


        assert(IpUtil.ipv4IsPrivateRange("127.0.0.1"));

        assertFalse(IpUtil.ipv4IsPrivateRange("1.1.1.1"));
        assertFalse(IpUtil.ipv4IsPrivateRange("8.8.8.8"));
        assertFalse(IpUtil.ipv4IsPrivateRange("253.8.8.8"));
    }
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

    @Test
    void cidrFromRange() {
        List<String> cidrSrb = IpUtil.range2cidrlist("178.148.0.0", "178.148.255.255");
        System.out.println(String.join(";", cidrSrb));
        assertEquals(1, cidrSrb.size());
        assertEquals("178.148.0.0/16", cidrSrb.get(0));

        List<String> cidrSrb2 = IpUtil.range2cidrlist("178.148.0.6", "178.148.255.255");
        System.out.println(String.join(";", cidrSrb2));
        assertEquals(14, cidrSrb2.size());
        assertEquals("178.148.0.6/31", cidrSrb2.get(0));

        List<String> cidrTotal = IpUtil.range2cidrlist("0.0.0.0", "255.255.255.255");
        System.out.println(String.join(";", cidrTotal));
        assertEquals(1, cidrTotal.size());
        assertEquals("0.0.0.0/0", cidrTotal.get(0));

        List<String> cidr7 = IpUtil.range2cidrlist("0.0.0.0", "1.255.255.255");
        System.out.println(String.join(";", cidr7));
        assertEquals(1, cidr7.size());
        assertEquals("0.0.0.0/7", cidr7.get(0));

        List<String> cidr192 = IpUtil.range2cidrlist("192.168.0.0", "192.168.255.255");
        System.out.println(String.join(";", cidr192));
        assertEquals(1, cidr192.size());
        assertEquals("192.168.0.0/16", cidr192.get(0));


        List<String> cidr1922 = IpUtil.range2cidrlist("192.168.0.0", "192.168.0.255");
        System.out.println(String.join(";", cidr1922));
        assertEquals(1, cidr1922.size());
        assertEquals("192.168.0.0/24", cidr1922.get(0));
    }
}