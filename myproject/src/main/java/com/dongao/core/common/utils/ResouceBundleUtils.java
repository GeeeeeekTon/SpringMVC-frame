package com.dongao.core.common.utils;

import java.util.ResourceBundle;

/**
 * 访问资源文件工具
 * 
 * @date 2013-10-24 上午11:00:18
 * @author dongao
 * @version 1
 */
public class ResouceBundleUtils {

	private String resourceFileName;

	private ResourceBundle bundle;

	/**
	 * 默认构造器
	 */
	public ResouceBundleUtils() {
	}

	/**
	 * 构造器，初始化资源
	 * 
	 * @param resourceFileName
	 */
	public ResouceBundleUtils(String resourceFileName) {
		this.resourceFileName = resourceFileName;
		// 初始化资源
		init(this.resourceFileName);
	}

	/**
	 * 初始化资源
	 * 
	 * @param resourceFileName
	 */
	private void init(String resourceFileName) {
		bundle = ResourceBundle.getBundle(resourceFileName);
	}

	/**
	 * 根据资源文件key值获得value值
	 * 
	 * @param key
	 * @return String
	 */
	public String getValueByKey(String key) {
		return bundle.getString(key);
	}

	public String getResourceFileName() {
		return resourceFileName;
	}

	public void setResourceFileName(String resourceFileName) {
		this.resourceFileName = resourceFileName;
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

}
