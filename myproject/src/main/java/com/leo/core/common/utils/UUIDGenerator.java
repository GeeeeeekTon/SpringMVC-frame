package com.leo.core.common.utils;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 * UUID工具类
 * @date：2013-12-16 下午06:11:10    
 * @author wangyilin@leo.com
 * @version 1
 */
public class UUIDGenerator {
	private UUIDGenerator() {
	}
	/**
	 * 生成32位字符，无"-"
	 * @return
	 */
	public static String random() {
		UUID uuid = UUID.randomUUID();
		return StringUtils.remove(uuid.toString(), '-');
	}

	public static void main(String[] args) {
		System.out.println(random());

	}
}
