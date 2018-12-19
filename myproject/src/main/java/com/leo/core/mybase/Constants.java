package com.leo.core.mybase;


/**
 * 全局常量
 * 
 * @author leo
 * 
 */
public abstract class Constants {
	/** 搜索字符串前缀 */
	public static final String SEARCH_PREFIX = "search_";
	/** 排序字符串前缀 */
	public static final String ORDER_PREFIX = "order_";
	/**UTF-8编码*/
	public static final String UTF8 = "UTF-8";
	public static final String OpenKey="Te@cherMe";
	public static final Long SLOW_SECOND= 5000l;//default 5秒
	/** cookie.domain*/
	public static final String COOKIEDOMAIN= "dongaoacc.com";
	/** cookie.path*/
	public static final String COOKIEPATH= "/";
	
	public static final String TEMPLATE_PATH="F:\\zznew\\Workspaces\\MyEclipse 10\\rbc\\web\\WEB-INF\\t\\index";
	
	/** 一小时多少分钟 */
	public static final long MINUTES_IN_HOUR = 60L;
	/** 一分钟多少秒 */
	public static final long SECONDS_IN_MINUTE = 60L;
	/** 一小时多少秒 */
	public static final long SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_IN_HOUR;
	/** 一秒有多少毫秒 */
	public static final long MILLISECONDS_IN_SECOND = 1000;
	
	/**是否有效 eq 字符串*/
	public static final String SEARCH_EQ_IS_VALID=SEARCH_PREFIX+MySearchFilter.Operator.EQ+"_isValid";
	
	public static enum IS_VALID{
		NO(0, "无效"), YES(1, "有效");
		private Integer value;
		private String description;
		private IS_VALID(Integer value, String description) {
			this.value = value;
			this.description = description;
		}
		public Integer getValue() {
			return this.value;
		}
		public String getDescription() {
			return this.description;
		}
	}
	
	
	public static enum Status{
		NEW(1, "新建"), ENABLE(2, "有效"), HIDDEN(3, "隐藏");
		private Integer value;
		private String description;
		private Status(Integer value, String description) {
			this.value = value;
			this.description = description;
		}
		public Integer getValue() {
			return this.value;
		}
		public String getDescription() {
			return this.description;
		}
		public static String getDescByValue(Integer value) {
			Status[] status = Status.values();
			for (Status sta : status) {
				if (sta.getValue().equals(value)) {
					return sta.getDescription();
				}
			}
			return "";
		}
		public static boolean isEnable(Integer value) {
			return Status.ENABLE.getValue().equals(value);
		}
	}
	/**
	 * 验证码类型
	 */
	public enum CaptchaType {

		/** 会员登录 */
		memberLogin,

		/** 会员注册 */
		memberRegister,

		/** 后台登录 */
		adminLogin,

		/** 找回密码 */
		findPassword,

		/** 重置密码 */
		resetPassword,

		/** 其它 */
		other
	}

}
