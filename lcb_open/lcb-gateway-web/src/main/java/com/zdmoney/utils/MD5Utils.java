package com.zdmoney.utils;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

public class MD5Utils {

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String encode(String origin) {
        return encodeCommon(origin, null);
    }

    public static String encode(String origin, String charsetName) {
        return encodeCommon(origin, charsetName);
    }

    private static String encodeCommon(String origin, String charsetName) {
        if (origin != null) {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.reset();
                if (StringUtils.isBlank(charsetName)) {
                    digest.update(origin.getBytes());
                } else {
                    digest.update(origin.getBytes(charsetName));
                }
                String resultString = toHex(digest.digest());
                return resultString.toLowerCase();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return origin;
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(HEX_DIGITS[(bytes[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return sb.toString();
    }

}
