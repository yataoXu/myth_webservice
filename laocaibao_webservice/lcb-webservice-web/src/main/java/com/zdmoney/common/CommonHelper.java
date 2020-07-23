package com.zdmoney.common;

import com.zdmoney.constant.AppConstants;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

public class CommonHelper {
	private static Log log = LogFactory.getLog(CommonHelper.class);
	
	public synchronized static List<String> getJsonElement(String json){
			
			List<String> list = new ArrayList<String>();
			try {
				JSONObject  jsonObj  = JSONObject.fromObject(json);
				Iterator keyIter = jsonObj.keys() ;  
				 while (keyIter.hasNext())  
			       {
					 	String  key = (String) keyIter.next();  
					 	list.add(key);					 
			       }  
			}catch(JSONException je){
				log.error(je.getMessage(),je);
			}catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
			}
			
			return list;
		}
	
	
	public synchronized static Map<String,String> jsonToMap(String json){
		Map<String,String> map = new HashMap<String, String>();
		try{
			 JSONObject jsonObj = JSONObject.fromObject(json);
		        for(Object k : jsonObj.keySet()){
		            Object v = jsonObj.get(k); 
		            map.put(k.toString(), v.toString());
		        }
		}catch(JSONException je){
			log.error(je.getMessage(),je);
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		
		return map;
	}
	
	
	public synchronized static Map<String,Object> jsonToMapObj(String json){
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			 JSONObject jsonObj = JSONObject.fromObject(json);
		        for(Object k : jsonObj.keySet()){
		            Object v = jsonObj.get(k); 
		            map.put(k.toString(), v.toString());
		        }
		}catch(JSONException je){
			log.error(je.getMessage(),je);
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		
		return map;
	}
	
	public synchronized static Map<String, Object> parseJSON2Map(String jsonStr){
        Map<String, Object> map = new HashMap<String, Object>();
        //最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k); 
            //如果内层还是数组的话，继续解析
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                Iterator<JSONObject> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
               // log.info("  list   ===> " +k.toString() );
            } else {
                map.put(k.toString(), v);
               // log.info("  ob   ===:  " +k.toString() );
            }
        }
        return map;
    }
	
	//判断字符串中是否包含特定字符串
	public static boolean containsAny(String str, String searchChars){
		return str.contains(searchChars);
	}
	
	//取得数字与小数点
	public static String getNum(String str){
			
	        char[] b = str.toCharArray();
	        String result = "";
	        for (int i = 0; i < b.length; i++)
	        {
	            if (("0123456789.").indexOf(b[i] + "") != -1)
	            {
	                result += b[i];
	            }
	        }
	        return result;
	}
	
	public static String cleanNum(String contentStr,String cleanStr){
		return contentStr.replaceAll(cleanStr,"");
	}
	
	public static String returnException(Map rtnStr){

		log.info(" [ returnException ] :  "+rtnStr.toString());
		if(rtnStr.get("Status").equals(AppConstants.CrmStatus.EXCEPTION_1) || 
				rtnStr.get("Status").equals(AppConstants.CrmStatus.EXCEPTION_2)  || 
				rtnStr.get("Status").equals(AppConstants.CrmStatus.EXCEPTION_3)  || 
				rtnStr.get("Status").equals(AppConstants.CrmStatus.EXCEPTION_4)  || 
				rtnStr.get("Status").equals(AppConstants.CrmStatus.EXCEPTION_7)){	
			
			return AppConstants.returnDataException(rtnStr.get("MSG").toString());
		
		}else if(rtnStr.get("Status").equals(AppConstants.CrmStatus.EXCEPTION)){	
			
			return AppConstants.returnException(AppConstants.CrmStatus.ERROR_SERVER);
		}else if(rtnStr.get("Status").equals(AppConstants.CrmStatus.EXCEPTION_MEMBER)){	
			return AppConstants.returnException(rtnStr.get("MSG").toString());
		}else if(rtnStr.get("Status").equals(AppConstants.CrmStatus.EXCEPTION_5) || 
				rtnStr.get("Status").equals(AppConstants.CrmStatus.EXCEPTION_6)){
			return AppConstants.returnExceptions(rtnStr.get("MSG").toString());
		} else {
			return AppConstants.returnException();
		}
	}
	
	/**
	  *
	  * @Title: transBean2Map
	  * @Description: bean转map
	  * @param @param obj
	  * @param @return    设定文件
	  * @return Map<String,Object>    返回类型
	  * @throws
	  * @author liyez
	  * @date 2015年1月31日 下午1:30:16
	  */
	public static Map<String, Object> transBean2Map(Object obj) {  
		  
      if(obj == null){  
          return null;  
      }          
      Map<String, Object> map = new HashMap<String, Object>();  
      try {  
          BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
          PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
          for (PropertyDescriptor property : propertyDescriptors) {  
              String key = property.getName();  
              // 过滤class属性  
              if (!key.equals("class")) {  
                  // 得到property对应的getter方法  
                  Method getter = property.getReadMethod();  
                  Object value = getter.invoke(obj);  
                  map.put(key, value);  
              }
          }  
      } catch (Exception e) {  
      	log.info("transBean2Map Error " + e);
      }  
      return map;  
  }  

}
