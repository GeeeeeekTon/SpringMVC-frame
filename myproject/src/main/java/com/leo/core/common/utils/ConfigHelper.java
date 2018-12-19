package com.leo.core.common.utils;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.PropertyPlaceholderHelper;

/**
 * 配置文件帮助类
 * 可以在多个配置文件之间使用占位符
 * 便于配置项统一管理
 * @author liuqiang
 *
 */
public class ConfigHelper {
	
	private Logger logger = LoggerFactory.getLogger(ConfigHelper.class);
	
	PropertyPlaceholderHelper helper = null;
	Properties props = null;
	
	private ConfigHelper(){
		String[] configLocation = new String[]{
				"config/ip/ip.properties",
				"config/redis/redis_config.properties",
		};
		init(configLocation);
	}
	
	private ConfigHelper(String [] configs){
		init(configs);
	}
	
	private void init(String[] configLocation){
		
		props = new Properties();
		try {
			for(String loc:configLocation){
				props.load(new ClassPathResource(loc).getInputStream());
				props.load(new ClassPathResource(loc).getInputStream());
			}
		} catch (IOException e) {
			logger.error("配置文件位置不存在", e);
		}
		helper = new PropertyPlaceholderHelper("${", "}");
	}
	
	private static class SingletonInstance{
		private static ConfigHelper c = new ConfigHelper();
	}
	
	public static ConfigHelper getInstance(){
		return SingletonInstance.c;
	}
	
	public static ConfigHelper getInstance(String[] configs){
		ConfigHelper h = new ConfigHelper(configs);
		return h;
	}
	
	public String getValue(String key){
		String v = (String) props.get(key);
		return helper.replacePlaceholders(v, props);
	}
	
	public int getIntValue(String key,int def){
		String v = getValue(key);
		if(v != null){
			return Integer.parseInt(v);
		}
		return def;
	}
	
	public static void main(String[] args) {
		System.out.println(ConfigHelper.getInstance().getValue("uc_ip"));
		System.out.println(ConfigHelper.getInstance().getValue("ucenter_ip"));
		System.out.println(ConfigHelper.getInstance().getValue("uc_ip"));
		System.out.println(ConfigHelper.getInstance().getValue("ucenter_ip"));
	}
}
