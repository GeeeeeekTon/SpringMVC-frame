package com.dongao.core.mycomponent;

import java.util.Map;

/**
 * @author zhangzhen
 *
 */
public interface MyStaticService {

	/**
	 * 生成静态
	 * 
	 * @param templatePath
	 *            模板文件路径
	 * @param staticPath
	 *            静态文件路径
	 * @param model
	 *            数据
	 * @return 生成数量
	 */
	int build(String templatePath, String staticPath,Map<String, Object> model);
	
	
	String buildReturnSorce(String templatePath, String staticPath, Map<String, Object> model);
}