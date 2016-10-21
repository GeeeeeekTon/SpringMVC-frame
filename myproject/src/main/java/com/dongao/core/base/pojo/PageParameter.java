package com.dongao.core.base.pojo;

/**
 * 分页参数类
 * 
 * @date 2013-10-24 下午01:36:25
 * @author dongao
 * @version 1
 */
public class PageParameter implements java.io.Serializable{

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = -1801869215131832598L;

	/**
	 * 默认页面显示条数
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 每页显示条数
	 */
	private int pageSize; 
	
	/**
	 * 当前页码
	 */
	private int currentPage; 
	
	/**
	 * 上一页
	 */
	private int prePage; 
	
	/**
	 * 下一页
	 */
	private int nextPage;  
	
	/**
	 * 总页数
	 */
	private int totalPage; 
	
	/**
	 * 总条数
	 */
	private int totalCount; 
	
	/**
	 * 查询起始－－－mysql使用
	 */
	private int limitOffset;

	/**
	 * 分页类构造方法，初始化currentPage，pageSize=DEFAULT_PAGE_SIZE
	 */
	public PageParameter() {
		this.currentPage = 1;
		this.pageSize = DEFAULT_PAGE_SIZE;
	}
	
	/**
	 * 分页类构造方法，指定当前页、页面显示条数
	 * @param currentPage
	 * @param pageSize
	 */
	public PageParameter(int currentPage, int pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	/**
	 * 获取当前页
	 * @return int
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 设置当前页
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 获取每页显示条数
	 * @return int
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页显示条数
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取上一页
	 * @return int
	 */
	public int getPrePage() {
		return prePage;
	}
	
	/**
	 * 设置上一页
	 * @param prePage
	 */
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	/**
	 * 获取下一页
	 * @return int
	 */
	public int getNextPage() {
		return nextPage;
	}

	/**
	 * 设置下一页
	 * @param nextPage
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * 获取总页数
	 * @return int
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 设置总页数
	 * @param totalPage
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 获取总条数
	 * @return int
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总条数
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getLimitOffset() {
		if(currentPage <= 0){
			return 0;
		}
		if(pageSize <= 0){
			return 0;
		}
		return (currentPage - 1) * pageSize;
	}

}
