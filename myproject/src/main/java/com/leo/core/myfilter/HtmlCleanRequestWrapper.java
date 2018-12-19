package com.leo.core.myfilter;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.leo.core.myutil.StringUtil;

/**
 * @author zhangzhen
 * 
 * v1.0
 */
public class HtmlCleanRequestWrapper extends HttpServletRequestWrapper {
	private final static Whitelist user_content_filter = Whitelist.relaxed();
	/** 是否将空转换为null */
	private static volatile boolean emptyAsNull=true;
	static {
		user_content_filter.addTags("embed","object","param","span","div","br");
		user_content_filter.addAttributes(":all", "style", "class", "id", "name");
		user_content_filter.addAttributes("object", "width", "height","classid","codebase");	
		user_content_filter.addAttributes("param", "name", "value");
		user_content_filter.addAttributes("embed", "src","quality","width","height","allowFullScreen","allowScriptAccess","flashvars","name","type","pluginspage");
	}
	public static String filterUserInputContent(String html,ServletRequest request) {
		if(StringUtil.isEmptyString(html)) {
			if(emptyAsNull){
				return null;
			}else{
				return html;
			}
		}
		/**为了支持src相对和绝对路径*/
		String baseUri = "http://baseuri";
		return Jsoup.clean(html, baseUri, user_content_filter).replaceAll("src=\"http://baseuri", "src=\"");
	}
	public HtmlCleanRequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	@Override
	public String[] getParameterValues(String parameter) {
		String[] results = super.getParameterValues(parameter); // FIXME 总返回null
		if (results == null) {
			return null;
		}

		int count = results.length;

		String[] trimmedResults = new String[count];

		for (int i = 0; i < count; i++) {
			trimmedResults[i] = filterUserInputContent(results[i].trim(),super.getRequest()); 
		}		
		return trimmedResults;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = super.getParameterMap();
		
		for(Iterator<Map.Entry<String,String[]>> iterator = map.entrySet().iterator(); iterator.hasNext();){   
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) iterator.next();

			if (entry.getValue() != null) {
				for (int i = 0; i < entry.getValue().length; i++) {
					entry.getValue()[i] = filterUserInputContent(entry.getValue()[i].trim(),super.getRequest()); 
				}
			}
            
        }
		return map;
	}

	@Override
	public String getParameter(String name) {		
		String result = super.getParameter(name);
		if (result==null) {
			return null;
		}
		HttpServletRequest request = (HttpServletRequest) super.getRequest();
		return filterUserInputContent(result.trim(),super.getRequest());// trim xss
	}
	/*temp test*/
	public static void main(String[] args) {
			String str=" <scirpt>alert(120);</script> <img src=\"/1.png\" alt='' /> </hr>";
			System.out.println(filterUserInputContent(str,null));
			PathMatcher matcher = new AntPathMatcher();
	        // 完全路径url方式路径匹配
	         String requestPath="/user/list.htm?username=aaa&departmentid=2&pageNumber=1&pageSize=20";//请求路径
	         String patternPath="/user/list.htm**";//路径匹配模式
	        // 不完整路径uri方式路径匹配
	        // String requestPath="/app/pub/login.do";//请求路径
	        // String patternPath="/**/login.do";//路径匹配模式
	        // 模糊路径方式匹配
	        // String requestPath="/app/pub/login.do";//请求路径
	        // String patternPath="/**/*.do";//路径匹配模式
	        // 包含模糊单字符路径匹配
	        //String requestPath = "/app/pub/login.do";// 请求路径
	        //String patternPath = "/**/lo?in.do";// 路径匹配模式
	        boolean result = matcher.match(patternPath, requestPath);
	        System.out.println(result);
	}
}
