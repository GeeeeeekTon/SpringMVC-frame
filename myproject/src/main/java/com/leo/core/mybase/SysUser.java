package com.leo.core.mybase;

import java.util.Date;

import com.leo.core.base.pojo.BasePojo;

/**
 * 系统用户基本信息表(包括学员和教师)
 * 
 * @date：2014-2-17 下午3:27:36
 * @author wangwenli@leo.com
 * @version 1
 */
public class SysUser extends BasePojo {

    private static final long serialVersionUID = -4565873633277835051L;

    /**
     * 启用状体
     */
    public static final Integer STATUS_ENABLE = 1;

    /**
     * 停用状态
     */
    public static final Integer STATUS_DISABLE = 2;

    /**
     * 学员类型
     */
    public static final Integer TYPE_STUDENT = 1;

    /**
     * 教师类型
     */
    public static final Integer TYPE_TEACHER = 2;

    /**
     * 系统用户
     */
    public static final Integer TYPE_SYSUSER = 3;

    private String id;

    private String departmentId;// 部门id

    private String loginName;// 登录名

    private String password;// 密码

    private String userName;// 真实姓名

    private String email;// email

    private Integer sex;// 性别　0：女 1：男

    private String telphone;// 座机

    private String mobile;// 移动电话

    private Integer status;// 状态:1.启用/2.停用

    private Integer isDelete;// 是否删除　0：未删除，1：已删除

    private String description;// 描述

    private Integer type;// 类型:1.学员/2.教师/3.系统用户

    private Date createTime; // 创建时间

    private String orderFields; // 排序字段

    private String order; // 顺序
    
    private String roleName; //角色名称 数据库对应表中无此字段 
    
    private String roleId; //角色id
    
    private String departmentName; //部门名称
    
    private String departmentCode; //部门编号
    
    private String salt; //盐值
    
    
    //所有用户都有这些属性，表示户用、停用，status就不建议使用了，考虑到数据和sql兼容，status属性暂时不去掉。 
    private Integer user_status;//@liuqiang 增加
    
    
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrderFields() {
        return orderFields;
    }

    public void setOrderFields(String orderFields) {
        this.orderFields = orderFields;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getUser_status() {
		return user_status;
	}

	public void setUser_status(Integer user_status) {
		this.user_status = user_status;
	}
	
    
}