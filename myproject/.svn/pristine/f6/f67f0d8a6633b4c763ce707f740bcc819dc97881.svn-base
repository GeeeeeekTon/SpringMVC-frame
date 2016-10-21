package com.dongao.core.myexception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.dongao.core.myutil.LogUtils;

/**
 * 拦截异常,引导用户到友好页面,并根据配置给相关人员发送邮件或者短信
 * 
 * @author chenzhiguo
 * @version 1.0
 * @date 2013-11-19 17:35:56
 */
@SuppressWarnings("unchecked")
public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {

		LogUtils.logError("系统拦截到异常!", ex);
		return new ModelAndView("exception/error");
	}


}