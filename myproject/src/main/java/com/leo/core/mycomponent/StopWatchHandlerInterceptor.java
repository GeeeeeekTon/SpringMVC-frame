package com.leo.core.mycomponent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.leo.core.mybase.Constants;
import com.leo.core.myutil.LogUtils;

/**
 * @author zhangzhen
 *
 */
public class StopWatchHandlerInterceptor extends HandlerInterceptorAdapter {
	//public static final long SLOW_SECOND = 5000L;//5秒
	private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>(
			"StopWatch-StartTime");

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		long beginTime = System.currentTimeMillis();
		startTimeThreadLocal.set(beginTime);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long endTime = System.currentTimeMillis();
		long beginTime = startTimeThreadLocal.get();
		long consumeTime = endTime - beginTime;
		if (consumeTime > Constants.SLOW_SECOND.longValue()) { 
			LogUtils.logSlowRequest(request, consumeTime);
		}

	}
}
