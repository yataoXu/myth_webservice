package com.zdmoney.utils;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JackJsonUtil {
    public static String objToStr(Object obj) throws JsonGenerationException, JsonMappingException, IOException{
    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.writeValueAsString(obj);
    }
    
    public static <T> T strToObj(String json, Class<T> cls) throws JsonParseException, JsonMappingException, IOException{
    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.readValue(json, cls);
    }

    public static <T> T strToObjAbs(String json, Class<T> cls, Class parentCls, Class subCls) throws JsonParseException, JsonMappingException, IOException{
    	ObjectMapper mapper = new ObjectMapper();
		SimpleModule simpleModuleMultiple = new SimpleModule("lcb_ws", new Version(1, 0, 0, null));
		simpleModuleMultiple.addAbstractTypeMapping(parentCls, subCls);
		mapper.registerModule(simpleModuleMultiple);
    	return mapper.readValue(json, cls);
    }

    public static <T> List<T> strToList(String json, Class<T> cls) throws JsonParseException, JsonMappingException, IOException{
    	ObjectMapper mapper = new ObjectMapper();
    	JsonParser parser = mapper.getJsonFactory().createJsonParser(json);
    	JsonNode nodes = parser.readValueAsTree();
    	List<T> list = new ArrayList<T>(nodes.size());
    	for(JsonNode node : nodes){
    		list.add(mapper.readValue(node, cls));
    	}
    	return list;
    }
    
//    public static String getReturnJson(BussErrorCode bussErrorCode,String code,String msg,Object o){
//    	bussErrorCode.getErrorcode()
//    	return null;
//    }
}
