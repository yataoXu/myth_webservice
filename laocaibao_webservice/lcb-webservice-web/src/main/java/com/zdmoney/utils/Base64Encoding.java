package com.zdmoney.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by 00232384 on 2017/7/15.
 */
public class Base64Encoding {
    private static final BASE64Decoder decoder = new BASE64Decoder();
    private static final BASE64Encoder encoder = new BASE64Encoder();
    private static final org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64 ();

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key) throws Exception {
        if (key == null || key.length() < 1) {
            return "";
        }
        //return new String(encoder.encode(key.getBytes()));
        return new String(base64.encodeBase64URLSafe((new String(encoder.encode(key.getBytes()))).getBytes()));
    }


    public void changeCodeGBK(){

    }
    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) throws Exception {
        if (key == null || key.length() < 1) {
            return "";
        }
        return new String(decoder.decodeBuffer(new String(base64.decodeBase64(key.getBytes()),"UTF-8")));
        //return new String(base64.decodeBase64(key.getBytes()));
    }

    /**
     * BASE64解密
     *
     * @return
     * @throws Exception
     */
    public static byte[] decodeToByteValue(String data) throws Exception {
        if (data == null || data.length() < 1) {
            return null;
        }
        byte[] bDate = decoder.decodeBuffer(new String(base64.decodeBase64(data.getBytes()),"UTF-8"));
        return bDate;
    }

    /**
     * BASE64加密
     *
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] data) throws Exception {
        if (data == null) {
            return "";
        }
        return new String(base64.encodeBase64URLSafe((new String(encoder.encode(data).getBytes(),"UTF-8")).getBytes()));
    }


    /**
     * BASE64解密
     *
     * @param
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(byte[] data) throws Exception {
        if (data == null) {
            return "";
        }
        return new String(decoder.decodeBuffer(new String(base64.decodeBase64(data))));
    }

    
}
