package com.leo.core.common.utils.tedis;

import java.lang.reflect.Method;

/**
 * redis缓存脏数据处理服务接口
 * @date 2013-11-4 下午01:53:32
 * @author leo
 * @version 1
 */
public interface CacheDataCleanUpService {

	/**
	 * 处理redis脏数据核心方法
	 * @param targetName
	 * @param method
	 * @param paramters
	 * @param obj
	 */
	public void processData(Object targetName, Method method, Object[] paramters, Object obj);
}
