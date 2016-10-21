package com.dongao.core.myutil;


/**
 * 访问限制器。
 * zhangzhen
 */
public class RedisClientLimiter {
	/** 初始访问个数 */
	private String initialVisitCount = "0";
	private static final int delaySecond=60;
	public void setInitialVisitCount(String initialVisitCount) {
		this.initialVisitCount = initialVisitCount;
	}
	/**
	 * 是否可以继续访问。
	 * 
	 * @param key 健。
	 * @param expSeconds Key过期秒数。
	 * @param visitLimiterCount 限制的访问个数。
	 * @return 如果返回true，表示可以继续访问；否则返回false。
	 */
	public boolean allowBrowse(final String key, final int expSeconds, final long visitLimiterCount) {  
		getJedisTemplate().setnxex(key,initialVisitCount,expSeconds);
		Long currentBrowseCount = getJedisTemplate().incr(key);
		boolean flag=currentBrowseCount!=null&&currentBrowseCount.longValue()>visitLimiterCount?false:true;
		if(!flag){
			getJedisTemplate().expire(key, delaySecond);
		}
		return flag;
	}
	public static JedisTemplate getJedisTemplate(){
		return JedisTemplate.getInstance();
	}
}
