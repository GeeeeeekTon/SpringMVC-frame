package com.leo.modules.sample.model;

import com.leo.core.base.pojo.BasePojo;
 
 /**
 *用户表（样例）
 *@date 2014-01
 *@author leo
 *@version  V1.0
 */
@SuppressWarnings("serial")
public class SampleUser extends BasePojo implements java.io.Serializable {

	/**编号**/
	private java.lang.Long id;
	/**登录名**/
	private java.lang.String loginName;
	/**密码**/
	private java.lang.String password;
	/**登录时间**/
	private java.util.Date loginDate;
	/**类型**/
	private java.lang.Integer type;
	/**是否有效**/
	private java.lang.Integer isValid;
	/**创建日期**/
	private java.util.Date createDate;
	/**创建人**/
	private java.lang.Long createUser;
	/**更新日期**/
	private java.util.Date updateDate;
	/**更新人**/
	private java.lang.Long updateUser;

	public  java.lang.Long getId() {
		return id;
	}
	public  java.lang.String getLoginName() {
		return loginName;
	}
	public  java.lang.String getPassword() {
		return password;
	}
	public  java.util.Date getLoginDate() {
		return loginDate;
	}
	public  java.lang.Integer getType() {
		return type;
	}
	public  java.lang.Integer getIsValid() {
		return isValid;
	}
	public  java.util.Date getCreateDate() {
		return createDate;
	}
	public  java.lang.Long getCreateUser() {
		return createUser;
	}
	public  java.util.Date getUpdateDate() {
		return updateDate;
	}
	public  java.lang.Long getUpdateUser() {
		return updateUser;
	}


	public void setId( java.lang.Long id) {
	    this.id = id;
	}
	public void setLoginName( java.lang.String loginName) {
	    this.loginName = loginName;
	}
	public void setPassword( java.lang.String password) {
	    this.password = password;
	}
	public void setLoginDate( java.util.Date loginDate) {
	    this.loginDate = loginDate;
	}
	public void setType( java.lang.Integer type) {
	    this.type = type;
	}
	public void setIsValid( java.lang.Integer isValid) {
	    this.isValid = isValid;
	}
	public void setCreateDate( java.util.Date createDate) {
	    this.createDate = createDate;
	}
	public void setCreateUser( java.lang.Long createUser) {
	    this.createUser = createUser;
	}
	public void setUpdateDate( java.util.Date updateDate) {
	    this.updateDate = updateDate;
	}
	public void setUpdateUser( java.lang.Long updateUser) {
	    this.updateUser = updateUser;
	}
}
