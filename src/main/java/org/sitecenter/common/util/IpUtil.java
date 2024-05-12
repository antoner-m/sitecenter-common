package org.sitecenter.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {

    public static Long parseIPToLong(String ipAddress) {
        if (ipAddress.contains(":"))
            return parseIPv6ToLong(ipAddress);
        else
            return parseIPv4ToLong(ipAddress);
    }
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

    public static Long parseIPv6ToLong(String ipAddress) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            return null;
        }
        byte[] ipv6Bytes = inetAddress.getAddress();

        long result = 0;
        for (byte b : ipv6Bytes) {
            result = (result << 8) | (b & 0xFF);
        }

        return result;
    }
}
