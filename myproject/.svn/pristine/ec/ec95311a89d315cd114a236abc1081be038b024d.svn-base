package com.dongao.core.common.utils.tedis;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

/**
 * Tedis拦截，拦截所有符合规则的方法，把查询结果缓存到redis服务器，如果redis服务器存在该数据，则直接使用redis缓存数据
 * @date 2013-10-28 上午09:46:17
 * @author dongao
 * @version 1
 */
public class TedisMethodInterceptor implements MethodInterceptor, InitializingBean {
	
	private CacheService cacheService; // redis缓存处理自定义实现

	public TedisMethodInterceptor() {
		super();
	}

	/**
	 * 拦截所有符合规则的方法，把查询结果缓存到redis服务器，如果redis服务器存在该数据，则直接使用redis缓存数据
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		Object result = cacheService.processCache(invocation); // 调用自定义实现redis缓存类处理缓存
		
		return result;
	}

	public void afterPropertiesSet() throws Exception {
		
	}

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

}
