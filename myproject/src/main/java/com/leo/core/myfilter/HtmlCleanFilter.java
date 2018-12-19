package com.leo.core.myfilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * trim() and 防止注入攻击
 * 
 * @author zhangzhen
 *  
 *  v1.0
 */
public class HtmlCleanFilter extends MyBaseFilter {

	 public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		 chain.doFilter(new HtmlCleanRequestWrapper(request), response);
	 }

}
