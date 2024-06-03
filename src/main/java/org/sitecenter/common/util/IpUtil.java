package org.sitecenter.common.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IpUtil {

    public static Long parseIPv4ToLong(String ipAddress) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            return null;
        }
        byte[] ipv4Bytes = inetAddress.getAddress();

        long result = 0;
        for (byte b : ipv4Bytes) {
            result = (result << 8) | (b & 0xFF);
        }

        return result;
    }

    public static Long ipV4ToLong(String strIP) {
        long[] ip = new long[4];
        String[] ipSec = strIP.split("\\.");
        if (ipSec.length != 4)
            return null;
        for (int k = 0; k < 4; k++) {
            ip[k] = Long.valueOf(ipSec[k]);
        }

        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static String longToIpv4(long iplong) {
        StringBuilder result = new StringBuilder(16);
        result.append(iplong >> 24 & 0xFF).append(".");
        result.append(iplong >> 16 & 0xFF).append(".");
        result.append(iplong >> 8 & 0xFF).append(".");
        result.append(iplong & 0xFF);
        return result.toString();
    }


    public static List<String> range2cidrlist(String startIp, String endIp) {
        long start = ipV4ToLong(startIp);
        long end = ipV4ToLong(endIp);
        return range2cidrlist(start, end);
    }

    public static List<String> range2cidrlist(long start, long end) {
        ArrayList<String> pairs = new ArrayList<String>();
        while (end >= start) {
            byte maxsize = 32;
            while (maxsize > 0) {
                long mask = CIDR2MASK[maxsize - 1];
                long maskedBase = start & mask;

                if (maskedBase != start) {
                    break;
                }

                maxsize--;
            }
            double x = Math.log(end - start + 1) / Math.log(2);
            byte maxdiff = (byte) (32 - Math.floor(x));
            if (maxsize < maxdiff) {
                maxsize = maxdiff;
            }
            String ip = longToIpv4(start);
            pairs.add(ip + "/" + maxsize);
            start += Math.pow(2, (32 - maxsize));
        }
        return pairs;
    }

    public static String range2cidrString(String startIp, String endIp) {
        long start = ipV4ToLong(startIp);
        long end = ipV4ToLong(endIp);
        return range2cidrString(start, end);
    }

    public static String range2cidrString(long start, long end) {
        List<String> result = range2cidrlist(start, end);
        if (result == null || result.isEmpty())
            return null;
        return String.join(";", result);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static final int[] CIDR2MASK = new int[]{0x00000000, 0x80000000,
            0xC0000000, 0xE0000000, 0xF0000000, 0xF8000000, 0xFC000000,
            0xFE000000, 0xFF000000, 0xFF800000, 0xFFC00000, 0xFFE00000,
            0xFFF00000, 0xFFF80000, 0xFFFC0000, 0xFFFE0000, 0xFFFF0000,
            0xFFFF8000, 0xFFFFC000, 0xFFFFE000, 0xFFFFF000, 0xFFFFF800,
            0xFFFFFC00, 0xFFFFFE00, 0xFFFFFF00, 0xFFFFFF80, 0xFFFFFFC0,
            0xFFFFFFE0, 0xFFFFFFF0, 0xFFFFFFF8, 0xFFFFFFFC, 0xFFFFFFFE,
            0xFFFFFFFF};


}
