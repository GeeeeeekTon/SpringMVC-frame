package com.dongao.core.myutil;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import com.dongao.core.mybase.Servlets;
/**
 * 集中常见情形的日志处理
 * @author zhangzhen
 *
 */
public class LogUtils {
	/** 各个域之间用,分隔 */
	private static String LOG_FILED_SEPERATOR = ",";
	/**支持Email发送*/
    private static final Logger EMAIL_LOG = LoggerFactory.getLogger("myproject-email");
    private static final Marker NotifyAdminMarker = MarkerFactory.getMarker("NOTIFY_ADMIN");
	/**慢请求日志*/
    public static final Logger SLOW_REQUEST_LOG = LoggerFactory.getLogger("myproject-slowrequest");
    /**错误日志*/
    public static final Logger ERROR_LOG = LoggerFactory.getLogger("myproject-error");
    /**系统用户操作日志*/
    public static final Logger ACCESS_LOG = LoggerFactory.getLogger("myproject-sys-user");
    /**业务日志*/
    public static final Logger BUSINESS_LOG = LoggerFactory.getLogger("myproject-business");
    /**听课日志*/
    public static final Logger LISTENCOURSE_LOG = LoggerFactory.getLogger("myproject-listencourse");
    /** 听课过程中的限制日志 */
    public static final Logger LISTENLIMIT_LOG = LoggerFactory.getLogger("myproject-listenLimit");
    
    public static Logger getAccessLog() {
        return ACCESS_LOG;
    }

    public static Logger getErrorLog() {
        return ERROR_LOG;
    }
    
    public static Logger getSlowRequestLog() {
        return SLOW_REQUEST_LOG;
    }
    
    public static Logger getBusinessLog(){
    	return BUSINESS_LOG;
    }
    public static Logger getListenCourseLog(){
    	return LISTENCOURSE_LOG;
    }
    public static Logger getEmailLog(){
    	return EMAIL_LOG;
    }
    public static Marker getNotifyAdminMarker(){
    	return NotifyAdminMarker;
    }
    /**
     * 记录慢请求
     */
    public static void logSlowRequest(HttpServletRequest request,long consumeTime) {
    	//TODO 可以从request获得更多的信息
        getSlowRequestLog().info(String.format("%s consume %d millis", request.getRequestURI(), consumeTime));
    }
    
    /**
     * 记录页面错误
     * 错误日志记录 [page/eception][username][statusCode][errorMessage][servletName][uri][exceptionName][ip][exception]
     *
     * @param request
     */
    public static void logPageError(HttpServletRequest request) {
        //String username = getUsername(); TDOO

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = (String) request.getAttribute("javax.servlet.error.message");
        String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");


        if (statusCode == null) {
            statusCode = 0;
        }

        StringBuilder s = new StringBuilder();
        s.append(getBlock(t == null ? "page" : "exception"));
       // s.append(getBlock(username));
        s.append(getBlock(statusCode));
        s.append(getBlock(message));
        s.append(getBlock(Servlets.getRemoteAddr(request)));

        s.append(getBlock(uri));
        s.append(getBlock(request.getHeader("Referer")));
        StringWriter sw = new StringWriter();

//        while (t != null) {
//            t.printStackTrace(new PrintWriter(sw));
//            t = t.getCause();
//        }
        s.append(getBlock(sw.toString()));
        if(t!=null){
        	getErrorLog().error(s.toString(),t);
        }else{
        	getErrorLog().error(s.toString());
        }

    }
    /**
     * 记录异常错误
     * 格式 [exception]
     *
     * @param message
     * @param e
     */
    public static void logError(String message, Throwable e) {
    	//TODO 获得用户名
        StringBuilder s = new StringBuilder();
        s.append(getBlock("exception"));
        s.append(getBlock(message));
        ERROR_LOG.error(s.toString(), e);
    }
    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
    
    /**
     * 记录错误日志
     * @param params
     */
    public static void logError(final Object... params) {
    	StackTraceElement element = getCallStackTraceElement();
    	logToFile(ERROR_LOG, null, element, params);
    }
    
    /**
     * 记录听课过程相关的限制日志
     * @param params
     */
    public static void logListenLimit(final Object... params) {
    	StackTraceElement element = getCallStackTraceElement();
    	logToFile(LISTENLIMIT_LOG, null, element, params);
    }
    
    /**
     * 记录听课过程相关的限制日志
     * @param params
     */
    public static void logListenLimit(final HttpServletRequest request, final Object... params) {
    	StackTraceElement element = getCallStackTraceElement();
    	logToFile(LISTENLIMIT_LOG, request, element, params);
    }
    
    /**
     * 记录访问日志
     * @param request
     * @param params
     */
    public static void logAccess(final HttpServletRequest request, final Object... params) {
    	StackTraceElement element = getCallStackTraceElement();
    	logToFile(ACCESS_LOG, request, element, params);
    }
    
    /**
     * 记录访问日志
     * @param params
     */
    public static void logAccess(final Object... params) {
    	StackTraceElement element = getCallStackTraceElement();
    	logToFile(ACCESS_LOG, null, element, params);
    }
    
    /**
     * 记录业务操作相关日志
     * @param params
     */
    public static void logBusiness(final Object... params) {
    	StackTraceElement element = getCallStackTraceElement();
    	logToFile(BUSINESS_LOG, null, element, params);
    }
    
    /**
     * 记录业务操作相关日志
     * @param params
     */
    public static void logBusiness(final HttpServletRequest request, final Object... params) {
    	StackTraceElement element = getCallStackTraceElement();
    	logToFile(BUSINESS_LOG, request, element, params);
    }
    
    private static void logToFile(final Logger logger, final HttpServletRequest request, final StackTraceElement element, final Object... params) {
    	if (params != null && params.length > 0) {
    		StringBuilder sb = new StringBuilder();
    		sb.append(element.getFileName()).append(LOG_FILED_SEPERATOR).append(element.getMethodName()).append(LOG_FILED_SEPERATOR).append(element.getLineNumber());
    		for (Object obj : params) {
    			sb.append(LOG_FILED_SEPERATOR).append(obj);
    		}
    		if (request != null) {
    			sb.append(LOG_FILED_SEPERATOR).append(Servlets.getRemoteAddr(request)).append(LOG_FILED_SEPERATOR).append(request.getHeader("Referer"));
    		}
    		logger.info(sb.toString());
    	}
    }
    
    private static StackTraceElement getCallStackTraceElement() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        return stacktrace[3];
      }

}
