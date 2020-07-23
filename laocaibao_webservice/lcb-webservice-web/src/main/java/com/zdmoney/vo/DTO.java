package com.zdmoney.vo;

import org.apache.commons.beanutils.ConvertUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;



/**
 * dto
 * 
 * @author Erich
 * 
 */
public class DTO extends LinkedHashMap<String, Object>  {

	private static final long serialVersionUID = -2415132608720172214L;
	
	public static DTO of(String key, Object value) {
		DTO dto = new DTO();
		dto.put(key, value);
		return dto;
	}

	@Override
	public Object put(String key, Object value) {
		if (containsKey(key.toUpperCase()) && !containsKey(key)) {
			remove(key.toUpperCase());
		}
		return super.put(key, value);
	}
	
	public <T> T get(String key, Class<T> returnType) {
		Object value =  get(key);
		if(value != null) {
			return returnType.cast(value);
		}
		return null;
	}
	
	public Integer getAsInteger(String key)	{
		return (Integer)ConvertUtils.convert(get(key), Integer.class);
	}

	public Long getAsLong(String key)	{
		return (Long)ConvertUtils.convert(get(key), Long.class);
	}

	public Float getAsFloat(String key)	{
		return (Float)ConvertUtils.convert(get(key), Float.class);
	}

	public Double getAsDouble(String key) {
		return (Double)ConvertUtils.convert(get(key), Double.class);
	}

	public Boolean getAsBoolean(String key)	{
		return (Boolean)ConvertUtils.convert(get(key), Boolean.class);
	}

	public BigDecimal getAsBigDecimal(String key){
		return (BigDecimal)ConvertUtils.convert(get(key), BigDecimal.class);
	}

	public String getAsString(String key){
		return (String)ConvertUtils.convert(get(key), String.class);
	}

	public List getAsList(String key){
		return (List)ConvertUtils.convert(get(key), List.class);
	}


	
}
