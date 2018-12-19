package com.leo.core.common.utils;

/**
 * 运行环境信息
 * @author liuqiang
 *
 */
public class EnviromentUtil {

	public static boolean isDevelopMode(){
		return "develop".equals(System.getProperty("da.runmode"));
	}
}
