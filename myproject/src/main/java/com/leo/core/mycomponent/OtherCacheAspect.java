package com.leo.core.mycomponent;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.leo.core.mybase.MySwitch;
import com.leo.core.mycache.CacheChannel;
import com.leo.core.mycache.CacheObject;
import com.leo.core.mycache.annotation.CachePut;
import com.leo.core.mycache.util.SerializationUtils;
import com.leo.core.myutil.Base64;
import com.leo.core.myutil.KeyLock;
import com.leo.core.myutil.LogUtils;
import com.leo.core.myutil.StringUtil;

/**
 * @author zhangzhen
 * 
 * v1.0
 */
@Component
@Aspect
public class OtherCacheAspect implements Ordered {
	private final KeyLock<String> keyLock = new KeyLock<String>();
	public static final String defaultRegionName = "RbcOtherCache";

	@Around("com.leo.core.mycomponent.DongaoNamePointcut.otherCacheCut()")
	public Object joinPointCache(ProceedingJoinPoint pjp) {
		if (!MySwitch.otherCacheEnable) {
			try {
				return pjp.proceed();
			} catch (Throwable e) {
				LogUtils.logError("aop异常：", e);
				return null;
			} finally {
			}
		}
		try {
			Object result = null;// 定义方法返回值
			CacheChannel instance = CacheChannel.getInstance();// 缓存助手
			Class<?> cls = pjp.getTarget().getClass();// 目标类
			MethodSignature joinPointObject = (MethodSignature) pjp
					.getSignature();
			Method method = joinPointObject.getMethod();// 目标方法
			Object[] params = pjp.getArgs();// 方法参数
			if (method != null) {// assert not null
				if (method.isAnnotationPresent(CachePut.class)) {
					CachePut cachePut = method.getAnnotation(CachePut.class);
					String region = cachePut.region();
					if (StringUtil.isEmptyString(region)) {
						region = defaultRegionName;
					}
					long expiry = cachePut.expiry();// TODO expiry 下一版支持

					String cacheKey = cachePut.key();

					Class entiy = cachePut.entity();// idea：不再以目标类为维度了，改用entiy，这样重复的方法定义在不同的类，只要key一致，就能重复利用

					if (StringUtil.isEmptyString(cacheKey)) {
						cacheKey = entiy.getName() + "-" + cls.getName() + "-"
								+ method.getName() + "-"
								+ getObjectsString(params);
					}
					result = dealDefaultForCachePut(result, instance, cacheKey,
							region);
					if (result == null) {
						keyLock.lock(cacheKey);
						try {
							result = dealDefaultForCachePut(result, instance,
									cacheKey, region);
							if (result == null) {
								result = pjp.proceed(); // 从 DB 中获取
								if (result != null) {
									instance.set(region, cacheKey, result);
								}
							}
						} finally {
							keyLock.unlock(cacheKey);
						}
					}
				}else{
					result = pjp.proceed();
				}

			} else {
				result = pjp.proceed();// never not
				LogUtils.getErrorLog().error(
						cls.getName() + "在aop缓存拦截过程中发生了不可能发生的异常，拦截的method找不到！");
			}
			return result;
		} catch (Throwable e) {
			LogUtils.logError("other缓存处理过程拦截到了异常：", e);
			return null;
		} finally {

		}
	}

	private Object dealDefaultForCachePut(Object result, CacheChannel instance,
			String cacheKey, String region) {
		CacheObject cacheObject = instance.get(region, cacheKey);
		result = cacheObject.getValue();
		return result;
	}

	// TODO 优化,序列化效率大比拼
	private String getObjectsString(Object[] os) {
		if (os != null && os.length != 0) {
			StringBuilder sb = new StringBuilder();
			for (Object o : os) {
				sb.append(Base64.encodeToString(SerializationUtils.serialize(o)));
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE - 1;
	}

}
