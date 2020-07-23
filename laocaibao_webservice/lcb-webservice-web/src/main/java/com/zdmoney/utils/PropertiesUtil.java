package com.zdmoney.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class PropertiesUtil {
	public static final String GLOBAL_CONFIG_PATH = "file:"+System.getProperty("global.config.path");
    public static Map<String,String> listMap ;
    public static Map<String,String> sysMap;
	public static Map<String,String> descProperties;


	public static  synchronized Map<String,String> getDescrptionProperties(){
		if(descProperties == null){
			descProperties = new HashMap<String,String>();
			Properties props = new Properties();
			try {
				props.load(PropertiesUtil.class.getResourceAsStream("/conf/description.properties"));
				Set<Map.Entry<Object, Object>> entrySet = props.entrySet();
				for (Map.Entry<Object, Object> entry : entrySet) {
					if (!entry.getKey().toString().startsWith("#")) {
						descProperties.put(((String) entry.getKey()).trim(), ((String) entry
								.getValue()).trim());
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return descProperties;

	}

    public static  synchronized Map<String,String> getSysMap(){
        if(sysMap == null){
        	sysMap = new HashMap<String,String>();
            try {
            	Properties p = PropertiesLoader.loadProperties(GLOBAL_CONFIG_PATH+"/serviceConfig.properties");
                Set<Map.Entry<Object, Object>> entrySet = p.entrySet();
                for (Map.Entry<Object, Object> entry : entrySet) {
                    if (!entry.getKey().toString().startsWith("#")) {
                    	sysMap.put(((String) entry.getKey()).trim(), ((String) entry
                                        .getValue()).trim());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sysMap;
    }
    /** 
	* @Title:将配置文件中的中文数据用处理UTF-8 
	* @param propertyName
	* @param propertyFileName
	* @return   
	* @throws 
	* @time:2015-1-16 下午04:53:53
	* @author:Sam.J
	*/
	public static String getPropertyDealCh(String propertyName,String propertyFileName) {
		String properValue = "";
		try {
			Properties ppt = System.getProperties();
			ppt.load(PropertiesUtil.class.getResourceAsStream(propertyFileName));
			properValue = new String(ppt.getProperty(propertyName).getBytes("ISO8859-1"),"UTF-8");
		} catch (FileNotFoundException fe) {
			log.info("文件不正确");
		} catch (IOException e) {
			log.info("读取错误");
		}
		return properValue;
	}
	
	public static void main(String[] args) {
		Map<String, String> map = getSysMap();
		System.out.println(map);
	}

}
