package com.zdmoney.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by IntelliJ IDEA.
 * Date: 2008-6-18 11:48:56
 */
public class Des2 {
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    private static Log log = LogFactory.getLog(Des2.class);

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception 异常
     */
    public static String encode(String key, String data) throws Exception {
        return encode(key, data.getBytes("GBK"));
    }

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception 异常
     */
    public static String encode(String key, byte[] data) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes());

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        //key的长度不能够小于8位字节
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        AlgorithmParameterSpec paramSpec = new IvParameterSpec("12345678".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

        byte[] bytes = cipher.doFinal(data);
        return Base64.encode(bytes);
    }

    public static String encodeSafe(String key, String data) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes());

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        //key的长度不能够小于8位字节
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        AlgorithmParameterSpec paramSpec = new IvParameterSpec("12345678".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

        byte[] bytes = cipher.doFinal(data.getBytes());
        return Base64Encoding.encryptBASE64(bytes);
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static byte[] decode(String key, byte[] data) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        //key的长度不能够小于8位字节
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        AlgorithmParameterSpec paramSpec = new IvParameterSpec("12345678".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
        return cipher.doFinal(data);
    }

    /**
     * 获取编码后的值
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    public static String decodeSafe(String key, String data) {
        byte[] datas;
        String value = null;
        try {
            datas = decode(key,Base64Encoding.decodeToByteValue(data));
            value = new String(datas,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("解密失败");
            value = "";
        }
        return value;
    }

    /**
     * 获取编码后的值
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    public static String decodeValue(String key, String data) {
        byte[] datas;
        String value = null;
        try {
            if (System.getProperty("os.name") != null && (System.getProperty("os.name").equalsIgnoreCase("sunos") || System.getProperty("os.name").equalsIgnoreCase("linux"))) {
                log.debug("os.name(true)=" + System.getProperty("os.name"));
                datas = decode(key, Base64.decode(data));
                log.debug("ddd=" + new String(datas));
            } else {
                log.debug("os.name(false)=" + System.getProperty("os.name"));
                datas = decode(key, Base64.decode(data));
                log.debug("ddd=" + new String(datas, "GBK"));
            }
            value = new String(datas, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("解密失败");
            value = "";
        }
        return value;
    }

    public static void main(String[] args) throws Exception{
        String encodeValue = Des2.encodeSafe("QWEDCXZAQWEDCXZA","838385:1D5209C0-4A70-4857-8071-342DC0E074AC");
        System.out.println(encodeValue);
        String decodeValue = Des2.decodeSafe("QWEDCXZAQWEDCXZA",encodeValue);
        System.out.println(decodeValue);



       ;
    }
}
