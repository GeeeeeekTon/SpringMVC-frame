package com.leo.core.base.pojo;

/**
 * 分页类
 * 
 * @date 2013-10-24 下午03:16:46
 * @author leo
 * @version 1
 */

public interface Paginable {

	/**
	 * 总记录数
	 * 
	 * @return
	 */
	public int getTotalCount();

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getTotalPage();

	/**
	 * 每页记录数
	 * 
	 * @return
	 */
	public int getPageSize();

	/**
	 * 当前页号
	 * 
	 * @return
	 */
	public int getPageNo();

	/**
	 * 是否第一页
	 * 
	 * @return
	 */
	public boolean isFirstPage();

	/**
	 * 是否最后一页
	 * 
	 * @return
	 */
	public boolean isLastPage();

	/**
	 * 返回下页的页号
	 * 
	 * @return
	 */
	public int getNextPage();

	/**
	 * 返回上页的页号
	 * 
	 * @return
	 */
	public int getPrePage();
}
