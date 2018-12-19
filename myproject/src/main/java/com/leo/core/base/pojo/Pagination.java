package com.leo.core.base.pojo;

import java.util.List;

/**
 * 分页类（使用泛型）
 * 
 * @date 2013-10-24 下午03:15:13
 * @author leo
 * @version 1
 * @param <T>
 */
@SuppressWarnings("serial")
public class Pagination<T> extends SimplePage implements java.io.Serializable,
		Paginable {

	/**
	 * 默认构造器
	 */
	public Pagination() {
	}

	/**
	 * 构造器，初始化pageNo，pageSize，totalCount
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 */
	public Pagination(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}

	/**
	 * 构造器，初始化pageNo，pageSize，totalCount,list
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 * @param list
	 */
	public Pagination(int pageNo, int pageSize, int totalCount, List<T> list) {
		super(pageNo, pageSize, totalCount);
		this.list = list;
	}
	
	/**
	 * 构造器，通过分页参数类，初始化pageNo，pageSize，totalCount,list
	 * @param list
	 * @param pageParameter
	 */
	public Pagination(List<T> list, PageParameter pageParameter){
		super(pageParameter);
		this.list = list;
	}

	/**
	 * 已显示总条数
	 * @return int
	 */
	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	/**
	 * 当前页的数据
	 */
	private List<T> list;

	/**
	 * 获取当前页数据
	 * @return List<T>
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 设置当前页数据
	 * @param list
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

}
