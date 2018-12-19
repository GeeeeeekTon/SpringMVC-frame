package com.leo.core.mybase;

import java.util.Collection;

import com.leo.core.base.pojo.BasePojo;
import com.leo.core.base.pojo.PageParameter;
/**
 * 公共dto 支持分页设置
 * 
 * @author zhangzhen 
 * 
 * v1.0
 */
@SuppressWarnings("serial")
public class CommonDto extends BasePojo implements java.io.Serializable{
	private String sql;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public CommonDto(String sql){
		this.setSql(sql);
	}
	public CommonDto(String sql,PageParameter pageParameter){
		this.setSql(sql);
		this.setPageParameter(pageParameter);
	}
	public static CommonDto build(Collection<MySearchFilter> filters,Class cls,PageParameter pageParameter){
		return new CommonDto(MySearchFilter.getSelectSql(filters, cls),pageParameter);
	}
	public static CommonDto build(Collection<MySearchFilter> filters,Collection<Order> orders,Class cls,PageParameter pageParameter){
		return new CommonDto(MySearchFilter.getSelectSqlAndOrder(filters,orders, cls),pageParameter);
	}
}