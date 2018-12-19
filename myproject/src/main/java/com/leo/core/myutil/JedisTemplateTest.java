package com.leo.core.myutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leo.core.mycache.CacheChannel;

public class JedisTemplateTest {
	private static Logger logger = LoggerFactory.getLogger(JedisTemplateTest.class);
	public static void main(String[] args) {
		CacheChannel cache = CacheChannel.getInstance();
		JedisTemplate jedisTemplate = JedisTemplate.getInstance();
		jedisTemplate.flushDB();
	}
}
