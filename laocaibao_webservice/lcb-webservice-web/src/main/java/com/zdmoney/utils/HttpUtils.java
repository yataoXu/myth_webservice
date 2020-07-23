package com.zdmoney.utils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class HttpUtils {


    public static StringBuffer URLGet(String strUrl, Map parameterMap) throws IOException {
        String strTotalURL = "";
        strTotalURL = getTotalURL(strUrl, parameterMap);
        URL url = new URL(strTotalURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        HttpURLConnection.setFollowRedirects(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return getStringBufferFormBufferedReader(in);
    }

    public static StringBuffer URLGet(String strUrl, String content) throws IOException {
        String strTotalURL = "";
        strTotalURL = getTotalURL(strUrl, content);
        URL url = new URL(strTotalURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        HttpURLConnection.setFollowRedirects(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return getStringBufferFormBufferedReader(in);
    }

    public static StringBuffer URLGetJson(String strUrl, Map parameterMap) throws IOException {
    	String strTotalURL = "";
    	strTotalURL = getTotalURL(strUrl, parameterMap);
        URL url = new URL(strTotalURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        HttpURLConnection.setFollowRedirects(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return getStringBufferFormBufferedReader(in);
    }
    
    public static StringBuffer URLPost(String strUrl, Map map) throws IOException {
        String content = getContentURL(map);
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setAllowUserInteraction(false);
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        bout.write(content);
        bout.flush();
        bout.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
        return getStringBufferFormBufferedReader(in);
    }
    
    public static StringBuffer URLPostGBK(String strUrl, Map map) throws IOException {
    	String content = getContentURL(map);
    	URL url = new URL(strUrl);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setDoInput(true);
    	con.setDoOutput(true);
    	con.setAllowUserInteraction(false);
    	con.setUseCaches(false);
    	con.setRequestMethod("POST");
    	con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=GBK");
    	BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
    	bout.write(content);
    	bout.flush();
    	bout.close();
    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"GBK"));
    	return getStringBufferFormBufferedReader(in);
    }

    public static StringBuffer URLPost(String strUrl, String content) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setAllowUserInteraction(false);
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(),"UTF-8"));
        bout.write(content);
        bout.flush();
        bout.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
        return getStringBufferFormBufferedReader(in);
    }
    


    private static StringBuffer getStringBufferFormBufferedReader(BufferedReader in)
            throws IOException {
        StringBuffer returnStringBuffer = new StringBuffer();
        char[] tmpbuf = new char[1024];
        int num = in.read(tmpbuf);
        while (num != -1) {
            returnStringBuffer.append(tmpbuf, 0, num);
            num = in.read(tmpbuf);
        }
        in.close();
        return returnStringBuffer;
    }

    public static String getTotalURL(String strUrl, Map parameterMap) {
        String content = getContentURL(parameterMap);
        return getTotalURL(strUrl, content);
    }

    public static String getTotalURL(String strUrl, String content) {
        String totalURL = strUrl;
        if (totalURL.indexOf("?") == -1)
            totalURL = totalURL + "?";
        else
            totalURL = totalURL + "&";

        totalURL = totalURL + content;
        return totalURL;
    }

    public static String getContentURL(Map parameterMap) {
        if ((parameterMap == null) || (parameterMap.keySet().size() == 0))
            return "";
        StringBuffer url = new StringBuffer();
        Set keys = parameterMap.keySet();
        for (Iterator i = keys.iterator(); i.hasNext();) {
            String key = String.valueOf(i.next());
            if (!(parameterMap.containsKey(key)))
                break;
            Object val = parameterMap.get(key);
            String str = (val != null) ? val.toString() : "";
            try {
                str = URLEncoder.encode(str, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url.append(key).append("=").append(str).append("&");
        }
        String strURL = "";
        strURL = url.toString();
        if ('&' == strURL.charAt(strURL.length() - 1)) {
            strURL = strURL.substring(0, strURL.length() - 1);
        }
        return strURL;
    }


}