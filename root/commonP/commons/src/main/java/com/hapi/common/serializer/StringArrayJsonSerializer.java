package com.hapi.common.serializer;

import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 吧 字符串 的  图片数组 转成 对象
 * @author Admin
 *
 */
public class StringArrayJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    	try {
    		
    		List<String >  list = JSONObject.parseArray( value ,  String.class );
            String[] array = (value == null ? null :  list.toArray( new String[]{} ) );
            
            if (array != null) {
                jsonGenerator.writeStartArray();
                
                for(int i=0; i<array.length ;i++  ) {
                	jsonGenerator.writeString( array[i] );
                }
                
                jsonGenerator.writeEndArray();
            }
    		
    	}catch (Exception e) {
    		e.printStackTrace();
    		jsonGenerator.writeString("[]");
    	}
    	
    }
}