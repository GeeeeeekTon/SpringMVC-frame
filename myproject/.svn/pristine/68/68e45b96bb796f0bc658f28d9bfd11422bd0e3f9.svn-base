package com.dongao.core.common.utils.tedis;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.InitializingBean;

/**
 * redis缓存执行方法后切面，如果方法为设计规则方法（增 删 改），则清空该类下的所有缓存
 * @date 2013-10-29 上午09:04:38
 * @author dongao
 * @version 1
 */
public class TedisMethodAfterAdvice implements AfterReturningAdvice, InitializingBean {
	
	private CacheDataCleanUpService cacheDataCleanUpService;
	
	public TedisMethodAfterAdvice() {
		super();
	}

	public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
		cacheDataCleanUpService.processData(arg0, arg1, arg2, arg3); //调用自定义redis缓存脏数据处理类
	}

	public void afterPropertiesSet() throws Exception {
	}

	public void setCacheDataCleanUpService(
			CacheDataCleanUpService cacheDataCleanUpService) {
		this.cacheDataCleanUpService = cacheDataCleanUpService;
	}

}
