package com.leo.core.mycomponent;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.leo.core.mybase.Servlets;

/**
 * Interceptor - 令牌
 * 
 * 	zhangzhen
 * 
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

	/** "令牌"属性名称 */
	private static final String TOKEN_ATTRIBUTE_NAME = "token";

	/** "令牌"Cookie名称 */
	private static final String TOKEN_COOKIE_NAME = "token";

	/** "令牌"参数名称 */
	private static final String TOKEN_PARAMETER_NAME = "token";

	/** 错误消息 */
	private static final String ERROR_MESSAGE = "Bad or missing token!";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = Servlets.getCookie(request, TOKEN_COOKIE_NAME);
		if (request.getMethod().equalsIgnoreCase("POST")) {//post请求校验cookie
			String requestType = request.getHeader("X-Requested-With");
			if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
				if (token != null && token.equals(request.getHeader(TOKEN_PARAMETER_NAME))) {
					//删除cookie
					Servlets.removeCookie(request, response, TOKEN_COOKIE_NAME);
					return true;
				} else {
					response.addHeader("tokenStatus", "accessDenied");
				}
			} else {
				if (token != null && token.equals(request.getParameter(TOKEN_PARAMETER_NAME))) {
					//删除cookie
					Servlets.removeCookie(request, response, TOKEN_COOKIE_NAME);
					return true;
				}
			}
			if (token == null) {
				token = UUID.randomUUID().toString();
				Servlets.addCookie(request, response, TOKEN_COOKIE_NAME, token);
			}
			response.sendError(HttpServletResponse.SC_FORBIDDEN, ERROR_MESSAGE);
			return false;
		} else {//get 请求设置cookie
			if (token == null) {
				token = UUID.randomUUID().toString();
				Servlets.addCookie(request, response, TOKEN_COOKIE_NAME, token);//利用cookie实现服务端存储
			}
			request.setAttribute(TOKEN_ATTRIBUTE_NAME, token);
			return true;
		}
	}

}