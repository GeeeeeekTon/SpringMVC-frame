package com.leo.core.base.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.leo.core.base.pojo.PageParameter;
import com.leo.core.common.utils.JsonUtil;
import com.leo.core.common.utils.tedis.TedisUtil;
import com.leo.core.mybase.SysUser;

/**
 * base controller类，所有controller继承该类
 * 
 * @date 2013-10-24 下午05:21:07
 * @author leo
 * @version 1
 */
public class BaseController extends MultiActionController {
    
    private Logger logger = LoggerFactory.getLogger(BaseController.class);
    
    @Autowired
    private TedisUtil tedisUtil;
 
	/**
	 * 当前页
	 */
	private int currentPage = 1;  
	/**
	 * 页面显示条数
	 */
	private int pageSize = 10;  

	
	/**
	 * 初始化资源文件用于显示页面的url
	 * @param model
	 */
	@ModelAttribute
	public void loadSource(Model model){
	    try{
            ResourceBundle bundle = ResourceBundle.getBundle("config/init/init",Locale.getDefault(),this.getClass().getClassLoader());
            if(bundle!=null){
                Enumeration<String> keys = bundle.getKeys();
                while(keys.hasMoreElements()){
                    String key = keys.nextElement();
                    String value = bundle.getObject(key)!=null ? bundle.getString(key):"";
                    HttpServletRequest request = getRequest();
                    if(request!=null){
                    	 request.setAttribute(key,value);
                    }else{
                    	 model.addAttribute(key, value);
                    }
                }
            }
        }catch(Exception e){
            logger.error("加载config/init.properties失败", e);
        }
	}
	
	/**
	 * 获得Request请求
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
     * 从cookie获取参数值
     * @param request
     * @return
     */
    public SysUser getCurrentUser(HttpServletRequest request){
        String sessionId = "";
        Cookie[] cookies =request.getCookies();
        if(null!=cookies){
            for (int j = 0; j < cookies.length; j++) {
                if(cookies[j].getName().equals("loginKey")){
                    //得到sessionId
                    sessionId = cookies[j].getValue();
                    break;
                }
            }
        }
        SysUser sysUser = new SysUser();
        try {
            sysUser = JsonUtil.fromJson(tedisUtil.tedisGetString("currentUser"+sessionId), new TypeReference<SysUser>() {});
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return sysUser;
    }


	/**
	 * 用于分页，获取当前页数
	 * @return int
	 */
	public int getCurrentPage() {
		if(getRequest().getParameter("iDisplayStart") == null || Integer.parseInt(getRequest().getParameter("iDisplayStart")) == 0){
			return currentPage;
		}else{
			return Integer.parseInt(getRequest().getParameter("iDisplayStart"))/getPageSize()+1;
		}
	}

	/**
	 * 用于分页，每页显示条数
	 * @return int
	 */
	public int getPageSize() {
		if(getRequest().getParameter("pageSize") == null){
			return pageSize;
		}else{
			return Integer.parseInt(getRequest().getParameter("pageSize"));
		}
	}
	
	/**
	 * 获取分页参数类，已初始化当前页，每页显示条数
	 * @return PageParameter
	 */
	public PageParameter getpagePageParameter(){
		PageParameter pageParameter = new PageParameter();
		pageParameter.setCurrentPage(getCurrentPage());
		pageParameter.setPageSize(getPageSize());
		
		return pageParameter;
	}

}
