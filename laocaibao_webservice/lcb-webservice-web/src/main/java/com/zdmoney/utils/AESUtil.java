package com.zdmoney.utils;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";

    private static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";

    /**
     * AES加密明文字符串
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String content, byte[] password) throws Exception{
        Key key = new SecretKeySpec(password, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(content.getBytes("GBK"));
    }
    /**
     * 解密AES加密过的字符串
     * @param content
     * @param password
     * @return
     */
    public static byte[] decrypt(byte[] content, byte[] password) {
        try {
            Key key = new SecretKeySpec(password, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 随机生成秘钥
     */
    public static void getKey(){
        try {
            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            kg.init(128);
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String s = byteToHexString(b);
            System.out.println(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex=Integer.toHexString(bytes[i]);
            if(strHex.length() > 3){
                sb.append(strHex.substring(6));
            } else {
                if(strHex.length() < 2){
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return  sb.toString();
    }


    public static void main(String[] args) throws  Exception{
        String key = "1234567890123456";
        System.out.println(byteToHexString(key.getBytes()));
        System.out.println(key.getBytes().length);
        String content = "<MsgText>some-message</MsgText>";
        byte[] encrypt = AESUtil.encrypt(content, key.getBytes());
        System.out.println("加密后的内容：" + Base64.encode(encrypt));

        System.out.println("加密后的内容：" + Base64.encode(encrypt));
//        System.out.println("解密后的内容：" + new String(AESUtil.decrypt(Base64.decode(str), key)));

        String str1 = "P0003000|k6djjK8kW/wSowp7j0fGCC5dviPB0LLD+SjTuxR0h/gqwg9mvOf9PTYnkIJqJJjegLTKxsPZNgsRmbyB9nG9H0MNLr4iYBxrJvb400KDPl+QsNS3pBpUQoiCFNweWxAtrNARShnpPtg8DOPV1AkhkKs+rFhiCrQrOjEsLgopbfU=|KyYleWCjc2CrIb5aCzif8/h/mMfzWlRaGeQ7z/G9B6fJ2hthkTfm8ioKTsoc2kmQavZCV4kVoPtL986D30mMqlKAfK3oEb2VFuwypVDfeiy5QyMsnlmfOtreDfFAk9S373pZavy/OsES9SRUe4tIpzXdT+ND+turEQrcL+Gup6s=|awDZDjK222YcCAB0Hqpk2sFL3W9y8b/6cD1n/VMVYO5RwaLpgJen1Zi6nOqLi/JJk8z5RENmj0Vl+81VBCebmY3001csV2q+j7LX6UAKoUkF6JLPVweZm34XNC3YnOMu8W+kcH6gD8SHd4WYUo4Q81Q0m7AdrMFCvi5bxTt7Q5LHLI3SNnM73E3qweg1xhEwtR/MMSb34Gyjwa6SSgepoB1RFADUD/KLeAUi7bWIkpjPFhN2vQWa7XAPWOmanNZN6+e6wcUWuTO2Z+MinnTFQ7QNsNK4B0Yusnx4huNk0CaJcdahNvd1bsvrnTV2CcpwxP0Pi5O4KwtSkhC8rKsirCatxVHY3yZOxwsFQB9DhTCjW2d8P3oxxtdE7OlGS7D/4fSAsTQh4w/AMzwrLIkHHKvTzeNSLg929/EazCX3TLpE7GkDclnMRwIuzoLxL6mZpfaqDQ1m1nYQKchkv+WSzIb+aBpDfk5JtbtH/RQnxue7tyZwSglJYvzB27Fk5BEGuafH8CH+6zHr8P/YjGxkd5vCQQoezKBZY948QkX4yJ2WFh1P0ei5lXNdsin78mic0cmfrnpI8Y+HON98UJTw00nS8FCGNbL7UOVkXm8N0vjZ3tQE4oPkl4IUcNCz1QligCCyYIPwTDb71Vu1siRvEfQrffuk0E8/8sQAXxiSujKBfRMC+aAEeP9xv4uCTqI0nY8Kz6jM7KBwYePtFl3z+3Kj4uN6ViDKJjN04yM3m4rSwe3v0tZfNAV3bV6ygqkt+S0u0fGNEwJMUdojAN+43sc3XXgNq4nryJsAzkN1082N34erfwUNPVMe888EQxebewjkUkfwD/tpKjq+rN2eQvVyo15006JJ4osTZjF/O1BfocpsfVH+531HVAoDf1p/gZUpNjh8SgcH8Fljwb/mNy+4TSzgEWWiSOLNDczZmKgPbOc/Ov8sa0bct+cOl8r+GvIMfxgyIyAJB8uqeCo4p/cUyQAqFerfQBDh+DtjdDDg+PaMwnoxLRClyFKT3FLycHrYNlj0nGYLrr1dCH6nYSsTLjpKlCKUdfhsasdQt9y1mOjLI1rDNOf3o31Cx03Pgw7ngsFXo8fuN/O7Kz0Zl7TmUSizhpWkGBnnhBbH5++N67Utaprzoe/NF6qPBa/d5Q48qwGPlNtMXj3opkHyTNjbaz57Cp1WbaJdoRTXGrY8mnDiNjsSqgc65MazIOuh4iO5g4a5FLyPXVfgSXEron5murGIva7aK5WamIQqg+Q3rIqvlrWD+Y8q/e2br9O8ziElQHECn1c/5FsIcxmB3yMrwwGzaRHjNdlGwHVCNf1Kwuq+FpwvK9nh6tfHTblopO0O+wvak0OEG9FIshVAkpa1wU3yhinCvNFSaUEbm+7hk1Mxld3STYyGiCWWZvi1I0lc13KxYhei6CreMrQuxiydGFg29pm1IkGPBfXg0kk=\n";
        String[] secretStr1 = str1.split("\\|");
        for(String data : secretStr1){
            System.out.println(data);
        }
    }
}
