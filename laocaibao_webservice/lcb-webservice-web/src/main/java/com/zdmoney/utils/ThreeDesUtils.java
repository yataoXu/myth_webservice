package com.zdmoney.utils;

import com.zdmoney.secure.utils.HexByteUtil;
import lombok.extern.slf4j.Slf4j;


import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * Author: silence.cheng
 * Date: 2018/1/4 10:22
 */
@Slf4j
public class ThreeDesUtils {
    private static String ENCRYPT_KEY = "YoAv98Qq9CVJiqRoPUyG4Mv3UmcgYrWo";
    private static String ARITHMETIC = "DESEDE";
    private static String CHARSET_NAME = "UTF-8";
    private static String IV = "00000000";

    private ThreeDesUtils() {
    }

    public static String encryptMode(String sdata) {
        try {
            byte[] key = ENCRYPT_KEY.getBytes(CHARSET_NAME);
            byte[] data = sdata.getBytes(CHARSET_NAME);
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ARITHMETIC);
            Key desKey = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance(ARITHMETIC + "/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes(CHARSET_NAME));
            cipher.init(1, desKey, iv);
            byte[] bOut = cipher.doFinal(data);
            return HexByteUtil.bytesToHexString(bOut).toUpperCase();
        } catch (Exception var9) {
            var9.printStackTrace();
            log.error("加密异常,原因[]", var9.getMessage(), var9);
            return null;
        }
    }

    public static String decryptMode(String sdata) {
        try {
            byte[] key = ENCRYPT_KEY.getBytes(CHARSET_NAME);
            byte[] data = HexByteUtil.hexStringToBytes(sdata);
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ARITHMETIC);
            Key desKey = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance(ARITHMETIC + "/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes(CHARSET_NAME));
            cipher.init(2, desKey, iv);
            byte[] bOut = cipher.doFinal(data);
            return new String(bOut, CHARSET_NAME);
        } catch (Exception var9) {
            var9.printStackTrace();
            log.error("解密异常,原因[]", var9.getMessage(), var9);
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(encryptMode("18321725279"));
    }
}
