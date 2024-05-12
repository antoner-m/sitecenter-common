package org.sitecenter.common.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

    public static String longToIpv4(long iplong) {
        StringBuilder result = new StringBuilder(16);
        result.append(iplong>>24 & 0xFF).append(".");
        result.append(iplong>>16 & 0xFF).append(".");
        result.append(iplong>>8 & 0xFF).append(".");
        result.append(iplong & 0xFF);
        return result.toString();
    }
}
