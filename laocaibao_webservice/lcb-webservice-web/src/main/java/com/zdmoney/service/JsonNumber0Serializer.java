package com.zdmoney.service;

import com.zdmoney.utils.CoreUtil;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class JsonNumber0Serializer extends JsonSerializer<BigDecimal>{

	@Override
	public void serialize(BigDecimal value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		String v = CoreUtil.BigDecimalAccurate(value,0);
		jgen.writeString(v); 
	}
}
