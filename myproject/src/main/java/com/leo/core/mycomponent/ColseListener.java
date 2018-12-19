package com.leo.core.mycomponent;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.leo.core.mycache.CacheChannel;
/**
 * @author zhangzhen
 *
 */
@Component("colseListener")
public class ColseListener implements ServletContextAware, ApplicationListener<ContextClosedEvent>{
	private ServletContext servletContext;
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		if (servletContext != null && event.getApplicationContext().getParent() == null) {
			CacheChannel.getInstance().close();
		}
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
