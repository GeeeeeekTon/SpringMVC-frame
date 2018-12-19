package com.leo.modules;

import com.alibaba.druid.filter.config.ConfigTools;

public class PasswordTest {
	public static void main(String[] args) throws Exception {
		System.out.println(1);
		System.out.println(ConfigTools.encrypt("root"));
	}
}
