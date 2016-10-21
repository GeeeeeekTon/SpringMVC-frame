package com.dongao.core.common.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * jackson返回日期格式
 * @date 2013-11-15 下午05:59:13
 * @author dongao
 * @version 1
 */
public class JsonDateSerializer extends JsonSerializer<Date>{

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) 
            throws IOException, JsonProcessingException { 

        String formattedDate = SIMPLE_DATE_FORMAT.format(date); 

        gen.writeString(formattedDate); 
    } 

}
