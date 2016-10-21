package com.dongao.core.common.utils;

import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 资源文件访问工具类
 * 
 * @date：2013-12-31 下午05:31:05
 * @author dongao@dongao.com
 * @version 1
 */
public class MessageSourceUtil {

    /**
     * 资源文件源
     */
    private ResourceBundleMessageSource messageSource;

    /**
     * 根据code(key)获得对应value
     * 
     * @param code
     * @param args
     * @return
     */
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * 根据code(key)获得对应value
     * 
     * @param code
     * @param args
     * @return
     */
    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, null, Locale.getDefault());

    }

    /**
     * 根据code(key)获得对应value
     * 
     * @param code
     * @param args
     * @param defaultMessage
     * @param locale
     * @return
     */
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        String msg = messageSource.getMessage(code, args, defaultMessage, locale);
        return msg != null ? msg.trim() : msg;
    }
    /**
     * set资源文件源
     * 
     * @param messageSource
     */
    public void setMessageSource(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

}
