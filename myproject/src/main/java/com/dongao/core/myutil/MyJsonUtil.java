package com.dongao.core.myutil;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dongao.core.common.utils.JsonUtil;

/**
 * @author zhangzhen
 *
 */
public class MyJsonUtil {
	private static Logger logger = LoggerFactory.getLogger(MyJsonUtil.class);
	private static ObjectMapper mapper=JsonUtil.mapper();
	
	/**
	 * 构造Collection类型.
	 */
	public static JavaType contructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
		return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
	}
	/**
	 * 反序列化复杂Collection如List<Bean>, 先使用createCollectionType()或contructMapType()构造类型, 然后调用本函数.
	 * 
	 * @see #createCollectionType(Class, Class...)
	 */
	public static <T> T fromJson(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		try {
			return (T) mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		try {
			return JsonUtil.fromJson(jsonString, clazz);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}
    public static <T> String toJson(T src) {
		try {
			return JsonUtil.toJson(src);
		} catch (IOException e) {
			logger.warn("tojson error:" + src, e);
			return null;
		}
    }
	
}
