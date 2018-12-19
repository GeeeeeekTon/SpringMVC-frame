package com.leo.core.mycomponent;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.leo.core.myutil.BrowserCacheGenerator;

/**
 * 浏览器缓存过滤器。
 * 
 * zhangzhen
 * 
 */
public class BrowserCacheFilter extends OncePerRequestFilter {

	/** 浏览器缓存生成器 */
	protected BrowserCacheGenerator browserCacheGenerator;
	/** 缓存时间 */
	protected Integer cacheSeconds;
	
	private static final String ERROR_MESSAGE="非法的请求方式！";
	
	/**
	 * 设置浏览器缓存生成器。
	 * 
	 * @param browserCacheGenerator 浏览器缓存生成器。
	 */
	public void setBrowserCacheGenerator(BrowserCacheGenerator browserCacheGenerator) {
		this.browserCacheGenerator = browserCacheGenerator;
	}
	
	/**
	 * 设置缓存时间。
	 * 
	 * @param cacheSeconds 缓存时间。
	 */
	public void setCacheSeconds(Integer cacheSeconds) {
		this.cacheSeconds = cacheSeconds;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			browserCacheGenerator.checkAndPrepare(request, response, cacheSeconds, true);
			filterChain.doFilter(request, response);
		} catch (HttpRequestMethodNotSupportedException e) {
			doRestrictions(request,response,filterChain);
		}
	}
	
	/**
	 * 若是不允许访问，应该执行的操作。
	 * 
	 * @param request 请求对象。
	 * @param response 响应对象。
	 * @param chain 执行链。
	 * @throws IOException 
	 */
	public void doRestrictions(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException{
		HttpServletResponse tttpServletResponse=(HttpServletResponse) response;
		tttpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, ERROR_MESSAGE);
		return;
	}

}
