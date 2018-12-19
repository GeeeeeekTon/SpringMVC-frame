package com.leo.core.mycomponent;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 切点统一在此管理
 * 
 * @author zhangzhen
 *
 */
public class DongaoNamePointcut {
	@Pointcut("@within(com.leo.core.mycache.annotation.Cachable)")
	public void cacheCut(){}
	
	@Pointcut("@within(com.leo.core.mycache.annotation.OtherCachable)")
	public void otherCacheCut(){}

}
