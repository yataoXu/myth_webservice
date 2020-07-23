package com.zdmoney.utils;



import com.zdmoney.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MD5Utils
{
	private final static String[] hexDigits = {
      "0", "1", "2", "3", "4", "5", "6", "7",
      "8", "9", "a", "b", "c", "d", "e", "f"};

	public static String byteArrayToHexString(byte[] b){
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++){
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b){
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin){
		String resultString = null;
		try {
			resultString=new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString=byteArrayToHexString(md.digest(resultString.getBytes("utf-8")));
		}catch (Exception ex){}
		return resultString;
	}


	public static String MD5Encrypt(Map<String, Object> map, String key) {
		String inputStr = getInputStr(map);
		String origin = inputStr + "|" + key;
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes("utf-8")));
		} catch (Exception ex) {
		}
		return resultString;
	}

	/**
	 * map 获取字符串，排除signature 字段
	 *
	 * @param map
	 * @return
	 */
	public static String getInputStr(Map<String, Object> map) {
		Map<String, Object> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		Iterator<Map.Entry<String, Object>> iterator = treeMap.entrySet().iterator();
		StringBuffer sb = new StringBuffer();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> next = iterator.next();
			if (!StringUtils.isEmpty(next.getKey()) && !StringUtil.isEmpty(next.getValue()) && !"null".equals(next.getValue())
					&& !"signature".equals(next.getKey())) {
				sb.append(next.getValue()).append("|");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

    public static String getInputStr(Map<String, Object> map, String joinStr) {
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.putAll(map);
        Iterator<Map.Entry<String, Object>> iterator = treeMap.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            if (!StringUtils.isEmpty(next.getKey()) && !StringUtil.isEmpty(next.getValue()) && !"null".equals(next.getValue())
                    && !"signature".equals(next.getKey())) {
                sb.append(next.getKey()).append("=").append(next.getValue()).append(joinStr);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String MD5Encrypt(Map<String, Object> map, String key, String joinStr) {
        String inputStr = getInputStr(map, joinStr);
        String origin = inputStr + joinStr + "key=" + key;
        String resultString = null;
        try {
            resultString = new String(origin);
            resultString = DigestUtils.md5Hex(resultString).toUpperCase();
        } catch (Exception ex) {
        }
        return resultString;
    }

	public static void main(String[] args)
	{
		String key = "78bad72e00abbeb5";
		Map<String, Object> map = new HashMap<>();
		map.put("merchantCode", "820180727661");
		map.put("clientType", "0");
		map.put("backNotifyUrl", "http://172.17.34.8:8080/laocaibao_4.7/zdpay/backNotify");
		map.put("loginId", "111111");
		map.put("channelOrderNo", "RECHARGE_20180815133506507134127");
		map.put("payTime", "201808151345057");
		map.put("channelType", "0");
		map.put("productType", "4001");
		map.put("payAmt", "111");
		map.put("businessType", "1");
        map.put("pageUrl", "www.baidu.com");
		System.out.println(MD5Encrypt(map, key));
	}

}
