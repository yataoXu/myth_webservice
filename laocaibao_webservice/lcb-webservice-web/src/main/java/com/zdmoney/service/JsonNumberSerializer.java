package com.zdmoney.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.stereotype.Component;
import com.zdmoney.utils.CoreUtil;

@Component
public class JsonNumberSerializer  extends JsonSerializer<BigDecimal>{

	@Override
	public void serialize(BigDecimal value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		String v = CoreUtil.BigDecimalAccurate(value,2);
		jgen.writeString(v); 
	}
}
