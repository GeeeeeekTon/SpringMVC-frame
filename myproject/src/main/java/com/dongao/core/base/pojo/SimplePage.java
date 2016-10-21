package com.dongao.core.base.pojo;

/**
 * 分页类
 * 
 * @author liuzy
 * @date 2012-8-19 下午05:53:52
 * @version 1
 */

public class SimplePage implements Paginable {
	private static final long serialVersionUID = 1L;
	/**
	 * 默认页面显示条数
	 */
	public static final int DEF_COUNT = 10;
	
	/**
	 * 总条数
	 */
	protected int totalCount = 0;
	
	/**
	 * 页面显示条数
	 */
	protected int pageSize = 10;
	
	/**
	 * 页数
	 */
	protected int pageNo = 1;
	protected int filterNo;
	
	protected PageParameter pageParameter;

	/**
	 * 默认构造器
	 */
	public SimplePage() {
	}
	
	/**
	 * 构造器，通过分页参数类来初始化totalCount，pageSize，pageNo
	 * @param pageParameter
	 */
	public SimplePage(PageParameter pageParameter){
		init(pageParameter);
	}
	
	void init(PageParameter pageParameter){
		this.pageParameter = pageParameter;
		if(pageParameter.getTotalCount() <=0){
			this.totalCount = 0;
		}else{
			this.totalCount = pageParameter.getTotalCount();
		}
		if(pageParameter.getPageSize() <=0 ){
			this.pageSize = DEF_COUNT;
		}else {
			this.pageSize = pageParameter.getPageSize();
		}
		if (pageParameter.getCurrentPage() <= 0) {
			this.pageNo = 1;
		} else {
			this.pageNo = pageParameter.getCurrentPage();
		}
		if ((this.pageNo - 1) * this.pageSize >= pageParameter.getTotalCount()) {
			this.pageNo = pageParameter.getTotalCount() / pageParameter.getPageSize();
			if (this.pageNo == 0) {
				this.pageNo = 1;
			}
		}
	}

	/**
	 * 构造器，初始化pageNo，pageSize，totalCount
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 */
	public SimplePage(int pageNo, int pageSize, int totalCount) {
		PageParameter p = new PageParameter();
		if (totalCount <= 0) {
			p.setTotalCount(0);
		} else {
			p.setTotalCount(totalCount);
		}
		if (pageSize <= 0) {
			p.setPageSize(DEF_COUNT);
		} else {
			p.setPageSize(pageSize);
		}
		if (pageNo <= 0) {
			p.setCurrentPage(1);
		} else {
			p.setCurrentPage(pageNo);
		}
		if ((p.getCurrentPage() - 1) * p.getPageSize() >= p.getTotalCount()) {
			p.setCurrentPage(p.getTotalCount() / p.getPageSize());
			if (p.getCurrentPage() == 0) {
				p.setCurrentPage(1);
			}
		}
		init(p);
	}

	/**
	 * 调整分页参数，使合理化
	 */
	public void adjustPage() {
		if (totalCount <= 0) {
			totalCount = 0;
		}
		if (pageSize <= 0) {
			pageSize = DEF_COUNT;
		}
		if (pageNo <= 0) {
			pageNo = 1;
		}
		if ((pageNo - 1) * pageSize >= totalCount) {
			pageNo = totalCount / pageSize;
		}
	}

	/**
	 * 获取当前页
	 * @return int
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 获取页面显示条数
	 * @return int
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	/**
	 * 获取总条数
	 * @return int
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 获取总页数
	 * @return int
	 */
	public int getTotalPage() {
		int totalPage = totalCount / pageSize;
		if (totalCount % pageSize != 0 || totalPage == 0) {
			totalPage++;
		}
		return totalPage;
	}

	/**
	 * 判断当前页是否为首页
	 * @return boolean
	 */
	public boolean isFirstPage() {
		return pageNo <= 1;
	}

	/**
	 * 判断当前页是否为末尾页
	 * @return boolean
	 */
	public boolean isLastPage() {
		return pageNo >= getTotalPage();
	}

	/**
	 * 获取下一页
	 * @return int
	 */
	public int getNextPage() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}

	/**
	 * 获取上一页
	 * @return int
	 */
	public int getPrePage() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	/**
	 * 设置总条数
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 设置每页显示条数
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 设置当前页数
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}


	public int getFilterNo() {
		return filterNo;
	}

	public void setFilterNo(int filterNo) {
		this.filterNo = filterNo;
	}

	public PageParameter getPageParameter() {
		return pageParameter;
	}

}
