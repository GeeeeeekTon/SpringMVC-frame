package com.dongao.core.myfilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 支持黑白名单的Filter:黑名单直接放过，白名单为要匹配的即走filter的名单
 * 
 * 黑白名单均支持Ant语法
 * 
 * @author zhangzhen
 * 
 * v1.0
 */
public abstract class MyBaseFilter implements Filter {

    private FilterConfig config = null;

    private final String[] NULL_STRING_ARRAY = new String[0];
    private final String URL_SPLIT_PATTERN = "[, ;\r\n]";//逗号  空格 分号  换行

    private final PathMatcher pathMatcher = new AntPathMatcher();

    private final Logger logger = LoggerFactory.getLogger(MyBaseFilter.class);

    /** 白名单*/
    private String[] whiteListURLs = null;

    /**黑名单*/
    private String[] blackListURLs = null;

    @Override
    public final void init(FilterConfig config) throws ServletException {
        this.config = config;
        this.initConfig();
        this.init();
    }
    public void init() throws ServletException {

    }

    /**
     * 1、黑名单匹配
     * 2、白名单匹配
     */
    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String currentURL = httpRequest.getServletPath();

        logger.debug("url filter : current url : [{}]", currentURL);

        if (isBlackURL(currentURL)) {
            chain.doFilter(request, response);
            return;
        }
        if (!isWhiteURL(currentURL)) {
            chain.doFilter(request, response);
            return;
        }
        doFilter(httpRequest, httpResponse, chain);
        return;
    }

    private boolean isWhiteURL(String currentURL) {
        for (String whiteURL : whiteListURLs) {
            if (pathMatcher.match(whiteURL, currentURL)) {
                logger.debug("url filter : white url list matches : [{}] match [{}] continue", currentURL, whiteURL);
                return true;
            }
        }
        logger.debug("url filter : white url list not matches : [{}] not match [{}]",
                currentURL, Arrays.toString(whiteListURLs));
        return false;
    }

    private boolean isBlackURL(String currentURL) {
        for (String blackURL : blackListURLs) {
            if (pathMatcher.match(blackURL, currentURL)) {
                logger.debug("url filter : black url list matches : [{}] match [{}] break", currentURL, blackURL);
                return true;
            }
        }
        logger.debug("url filter : black url list not matches : [{}] not match [{}]",
                currentURL, Arrays.toString(blackListURLs));
        return false;
    }

    /**
     * 子类覆盖
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    /**
     * 子类覆盖
     */
    @Override
    public void destroy() {

    }

    private void initConfig() {
        String whiteListURLStr = this.config.getInitParameter("whiteListURL");
        whiteListURLs = strToArray(whiteListURLStr);


        String blackListURLStr = this.config.getInitParameter("blackListURL");
        blackListURLs = strToArray(blackListURLStr);

    }

    private String[] strToArray(String urlStr) {
        if (urlStr == null) {
            return NULL_STRING_ARRAY;
        }
        String[] urlArray = urlStr.split(URL_SPLIT_PATTERN);

        List<String> urlList = new ArrayList<String>();

        for (String url : urlArray) {
            url = url.trim();
            if (url.length() == 0) {
                continue;
            }

            urlList.add(url);
        }

        return urlList.toArray(NULL_STRING_ARRAY);
    }

    public FilterConfig getConfig() {
        return config;
    }
}
