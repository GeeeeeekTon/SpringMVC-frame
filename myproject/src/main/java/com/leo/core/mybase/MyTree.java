package com.leo.core.mybase;

import java.io.Serializable;

/**
 * @author zhangzhen
 *
 */
public class MyTree implements Serializable{
	private static final long serialVersionUID = 1L;
	/**节点的唯一标识*/
	private String id;
	/**节点的父节点，用来标识层级:如果不写或为null，则该节点为根节点*/
	private String pId;
	/**节点名称*/
	private String name;
	/**记录 treeNode 节点的展开 / 折叠状态*/
	private String open="false";
	/**节点的 checkBox / radio 的勾选状态*/
	private String checked="false";
	/**是否禁用*/
	private String chkDisabled="false";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getChkDisabled() {
		return chkDisabled;
	}
	public void setChkDisabled(String chkDisabled) {
		this.chkDisabled = chkDisabled;
	}
	
}
