package com.leo.core.mybase;

import java.io.Serializable;

/**
 * 
 * @author zhengyongxiang
 *
 */
@SuppressWarnings("serial")
public class CoursewareTypeDto implements Serializable{
	private String typeName;
	private Integer type;
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
