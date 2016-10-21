package com.dongao.core.mycomponent;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.dongao.core.mybase.MySwitch;
import com.dongao.core.mybase.Servlets;
import com.dongao.core.myutil.RedisClientLimiter;

/**
 * 浏览访问限制过滤器。
 * zhangzhen
 */
public class BrowseLimiterFilter extends OncePerRequestFilter {

	/** 缓存时间 */
	protected int cacheSeconds;
	/** 访问次数限制 */
	protected int visitLimiterCount;
	
	private static final String ERROR_MESSAGE="访问次数太频繁，请喝杯茶，稍等一会儿再进行访问!";
	
	public static final String defaultRegionName="RbcLimitCache";
	/** 访问限制器 */
	protected RedisClientLimiter redisClientLimiter;
	public void setCacheSeconds(int cacheSeconds) {
		this.cacheSeconds = cacheSeconds;
	}
	public void setRedisClientLimiter(RedisClientLimiter redisClientLimiter) {
		this.redisClientLimiter = redisClientLimiter;
	}
	/***
	 * 设置访问次数限制。
	 * 
	 * @param visitLimiterCount 访问次数限制。
	 */
	public void setVisitLimiterCount(int visitLimiterCount) {
		this.visitLimiterCount = visitLimiterCount;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if(MySwitch.browseLimitEnable){
			String key = getKey(request, response);
			boolean allowBrowse = redisClientLimiter.allowBrowse(key, cacheSeconds, visitLimiterCount);
			if (allowBrowse) {
				filterChain.doFilter(request, response);
			} else {
				doRestrictions(request, response, filterChain);
			}
		}else{
			filterChain.doFilter(request, response);
		}
	}
	
	/**
	 * 获取缓存KEY。
	 * 
	 * @param request 请求对象。
	 * @param response 响应对象。
	 * @return 缓存KEY。
	 */
	public String getKey(ServletRequest request, ServletResponse response){
		StringBuffer sb=new StringBuffer();
		sb.append(defaultRegionName).append(":").append(Servlets.getRemoteAddr(request)).append(":").append(((HttpServletRequest)request).getRequestURI());
		return sb.toString();
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
