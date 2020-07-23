package com.zdmoney.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


import websvc.utils.SysLogUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class HttpConnectionUtil {
	public static void send(String url,Map<String,String> map){
		PrintWriter printWriter = null;
		HttpURLConnection connection = null;
		try{
			StringBuffer params = new StringBuffer();  
			// 组织请求参数  
	        Iterator it = map.entrySet().iterator();  
	        while (it.hasNext()) {  
	            Map.Entry element = (Map.Entry) it.next();  
	            params.append(element.getKey());  
	            params.append("=");  
	            params.append(element.getValue());  
	            params.append("&");  
	        }  
	        if (params.length() > 0) {  
	            params.deleteCharAt(params.length() - 1);  
	        } 
	        URL getUrl = new URL(url);
	        connection = (HttpURLConnection) getUrl.openConnection(); 
	        connection.setRequestProperty("accept", "*/*");  
	        connection.setRequestProperty("connection", "Keep-Alive");  
	        connection.setRequestProperty("Content-Length", String.valueOf(params.length()));  
	        connection.setDoOutput(true);  
	        connection.setDoInput(true);  
	        connection.setRequestMethod("POST");  
	        //设置超时限制
	        connection.setConnectTimeout(10000); 
	        connection.setReadTimeout(10000);  
//	        connection.connect();
	        printWriter = new PrintWriter(connection.getOutputStream());  
	        printWriter.write(params.toString());  
	        printWriter.flush();  
	        int responseCode = connection.getResponseCode();  
	        if (responseCode != 200) {  
        		log.error("获取http响应失败");
	        } else {  
	        	log.info("获取http响应成功");
	        }  
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(printWriter != null){
				printWriter.close();
				printWriter=null;
			}
			if(connection != null){
				connection.disconnect();
				connection=null;
			}
		}
	}

	public static String send2(String url,Map<String,String> map){
		BufferedWriter bout = null;
		InputStream inputStream =null;
		HttpURLConnection connection = null;

		try{
			String data = JSONObject.fromObject(map).toString();
			URL getUrl = new URL(url);
			connection = (HttpURLConnection) getUrl.openConnection();
			log.info("data="+data);
			SysLogUtil.save(data,"报文参数","HttpConnectionUtil");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			//设置超时限制
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			bout = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"UTF-8"));
			bout.write("data="+data);
			bout.flush();
			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				log.error("获取http响应失败");
				throw new RuntimeException("获取http响应失败,响应码:"+responseCode);
			} else {
				log.info("获取http响应成功");
				inputStream = connection.getInputStream();
				log.info(inputStream.available()+"");
				BufferedReader br=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
				String result="";
				StringBuilder sb = new StringBuilder();
				while ((result=br.readLine())!=null){
					sb.append(result);
				}
				return sb.toString();
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(bout!=null){
				try {
					bout.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
				bout=null;
			}
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
				inputStream=null;
			}
			if(connection!=null){
				connection.disconnect();
				connection=null;
			}
		}
	}

}
