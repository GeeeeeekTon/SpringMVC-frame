package com.leo.core.mycomponent;

/**
 * @author zhangzhen
 * 线程变量
 */
public class MyThreadVariable {
	/**
	 * 用户id线程变量：针对前台 operateUserId就是当前用户，如果是后台，则是被操作的目标UserId
	 */
	private static ThreadLocal<String> operateUserId = new ThreadLocal<String>();
	/**
	 * 周期id线程变量 针对前台 operatePeriodId就是当前合作周期Id，如果是后台，则是被操作的目标合作周期Id
	 */
	private static ThreadLocal<Long> operatePeriodId = new ThreadLocal<Long>();
	
	public static String getOperateUserId() {
		return operateUserId.get();
	}
	public static void setOperateUserId(String userId) {
		operateUserId.set(userId);
	}
	public static void removeOperateUserId() {
		operateUserId.remove();
	}
	public static Long getOperatePeriodId() {
		return operatePeriodId.get();
	}
	public static void setOperatePeriodId(Long periodId) {
		operatePeriodId.set(periodId);
	}
	public static void removeOperatePeriodId() {
		operatePeriodId.remove();
	}
}
