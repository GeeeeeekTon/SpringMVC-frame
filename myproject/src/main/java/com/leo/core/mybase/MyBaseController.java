package com.leo.core.mybase;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.leo.core.base.controller.BaseController;
import com.leo.core.base.pojo.PageParameter;
import com.leo.core.mybase.Constants.Status;
import com.leo.core.mycomponent.templateManager.RealPathResolver;

/**
 * @author zhangzhen
 *
 */
public abstract class MyBaseController extends BaseController{
	@Autowired
	private RealPathResolver absoluteRealPathResolver;
	public static final String JSONPCALLBACK = "callback";
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		/**empty as null*/
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new SimpleDateFormat("yyyy-MM-dd"), true));
	}
	public Collection<MySearchFilter> getFilters(){
		Map<String, String[]> params = Servlets.getParameterValuesMap(this.getRequest(),
				Constants.SEARCH_PREFIX);
		Map<String, MySearchFilter> map = MySearchFilter.parse(params);
		return map.values();
	}
	public Collection<Order> getOrders(){
		Map<String, String[]> params = Servlets.getParameterValuesMap(this.getRequest(),
				Constants.ORDER_PREFIX);
		Map<String, Order> map = Order.parse(params);
		return map.values();
	}
	
	
	@Override
	public PageParameter getpagePageParameter(){
		return super.getpagePageParameter();
	}
	@Override
	public HttpServletRequest getRequest(){
		return super.getRequest();
	}
	
	public String getJsonString(String json){
		StringBuffer sb=new StringBuffer();
		String jsonp = super.getRequest().getParameter(JSONPCALLBACK);
		if(jsonp!=null&&!"".equals(jsonp)){
			sb.append(jsonp).append("(").append(json).append(")");
			return sb.toString();
		}else{
			return json;
		}
	}
	public Status[] getStatus() {
		return Constants.Status.values();
	}
	
}

