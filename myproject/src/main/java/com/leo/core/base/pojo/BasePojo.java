package com.leo.core.base.pojo;

/**
 * 基础类，所有pojo都要继承
 * 
 * @date 2013-10-24 下午03:20:16
 * @author leo
 * @version 1
 */
public class BasePojo implements java.io.Serializable{

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 6795255294545467100L;
	
	/**
	 * 分页类
	 */
	private PageParameter pageParameter;  

	/**
	 * 获取分页参数类
	 * @return PageParameter
	 */
	public PageParameter getPageParameter() {
		if (pageParameter == null) {
			pageParameter = new PageParameter();
		}
		return pageParameter;
	}

	/**
	 * 设置分页参数类
	 * @param pageParameter
	 */
	public void setPageParameter(PageParameter pageParameter) {
		this.pageParameter = pageParameter;
	}

}
