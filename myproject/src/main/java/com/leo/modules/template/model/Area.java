package com.leo.modules.template.model;

import com.leo.core.base.pojo.BasePojo;
import com.leo.core.mybase.Constants.Status;
 
 /**
 *继续教育地区
 *@date 2014-01
 *@author leo
 *@version  V1.0
 */
@SuppressWarnings("serial")
public class Area extends BasePojo implements java.io.Serializable {

	/****/
	private java.lang.Long id;
	/**地区名称**/
	private java.lang.String name;
	/**地区编码**/
	private java.lang.String areaCode;
	/**父id**/
	private java.lang.Long parentId;
	/**层级**/
	private java.lang.Integer layer;
	/**状态**/
	private java.lang.Integer status;
	
	private Integer showOrder=0;
	
	//不持久化属性
	/**父节点名称*/
	private String parentName;
	/**首页地址*/
	private String homeUrl;

	public  java.lang.Long getId() {
		return id;
	}
	public  java.lang.String getName() {
		return name;
	}
	public  java.lang.String getAreaCode() {
		return areaCode;
	}
	public  java.lang.Long getParentId() {
		return parentId;
	}
	public  java.lang.Integer getLayer() {
		return layer;
	}
	public  java.lang.Integer getStatus() {
		return status;
	}


	public void setId( java.lang.Long id) {
	    this.id = id;
	}
	public void setName( java.lang.String name) {
	    this.name = name;
	}
	public void setAreaCode( java.lang.String areaCode) {
	    this.areaCode = areaCode;
	}
	public void setParentId( java.lang.Long parentId) {
	    this.parentId = parentId;
	}
	public void setLayer( java.lang.Integer layer) {
	    this.layer = layer;
	}
	public void setStatus( java.lang.Integer status) {
	    this.status = status;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public String getStatusName() {
		return Status.getDescByValue(getStatus());
	}
	
	public String getHomeUrl() {
		return homeUrl;
	}
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}
	
	public Integer getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((areaCode == null) ? 0 : areaCode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((layer == null) ? 0 : layer.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((parentId == null) ? 0 : parentId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Area other = (Area) obj;
		if (areaCode == null) {
			if (other.areaCode != null)
				return false;
		} else if (!areaCode.equals(other.areaCode))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (layer == null) {
			if (other.layer != null)
				return false;
		} else if (!layer.equals(other.layer))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	
}
