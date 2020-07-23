package com.zdmoney.utils;

import org.apache.commons.lang3.StringUtils;


/**
 * 脱敏工具类
 */
public class EncryptUtils {

    private static final int SIZE = 6;
    private static final String SYMBOL = "*";

    /**
     * 手机号脱敏
     *
     * @param mobile
     * @return
     */
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isBlank(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 身份证号脱敏
     *
     * @param idNum
     * @return
     */
    public static String idNumEncrypt(String idNum) {
        if (StringUtils.isBlank(idNum) || (idNum.length() < 8)) {
            return idNum;
        }
        return idNum.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", SYMBOL);
    }


    /**
     * 姓名脱敏
     *
     * @param value
     * @return
     */
    public static String nameEncrypt(String value) {
        if (StringUtils.isBlank(value) || value.length() < 2) {
            return value;
        }
        int len = value.length();
        int pamaone = len / 2;
        int pamatwo = pamaone - 1;
        int pamathree = len % 2;
        StringBuilder stringBuilder = new StringBuilder();
        if (len == 2) {
            if (pamathree == 1) {
                return SYMBOL;
            }
            stringBuilder.append(value.charAt(0));
            stringBuilder.append(SYMBOL);
        } else {
            if (pamatwo <= 0) {
                stringBuilder.append(value.substring(0, 1));
                stringBuilder.append(SYMBOL);
                stringBuilder.append(value.substring(len - 1, len));

            } else if (pamatwo >= SIZE / 2 && SIZE + 1 != len) {
                int pamafive = (len - SIZE) / 2;
                stringBuilder.append(value.substring(0, pamafive));
                for (int i = 0; i < SIZE; i++) {
                    stringBuilder.append(SYMBOL);
                }
                if ((pamathree == 0 && SIZE / 2 == 0) || (pamathree != 0 && SIZE % 2 != 0)) {
                    stringBuilder.append(value.substring(len - pamafive, len));
                } else {
                    stringBuilder.append(value.substring(len - (pamafive + 1), len));
                }
            } else {
                int pamafour = len - 2;
                stringBuilder.append(value.substring(0, 1));
                for (int i = 0; i < pamafour; i++) {
                    stringBuilder.append(SYMBOL);
                }
                stringBuilder.append(value.substring(len - 1, len));
            }
        }
        return stringBuilder.toString();
    }
}
