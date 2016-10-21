package com.dongao.core.common.utils.tedis;

/**
 * 生成redis缓存key服务接口
 * @date 2013-11-1 下午05:38:52
 * @author dongao
 * @version 1
 */
public interface GenCacheKeyService {
	
	/**
	 * 生成redis缓存key核心实现方法
	 * @param obj
	 * @param methodName
	 * @param arguments
	 * @return
	 */
	public String getRedisCacheKey(Class<?> obj, String methodName, Object[] arguments);

}
