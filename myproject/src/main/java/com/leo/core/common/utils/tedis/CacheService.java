package com.leo.core.common.utils.tedis;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 系统处理缓存服务接口
 * @date 2013-11-1 下午05:20:50
 * @author leo
 * @version 1
 */
public interface CacheService {

	/**
	 * 处理缓存方法
	 * @param invocation spring拦截
	 */
	public Object processCache(MethodInvocation invocation);
	
}
