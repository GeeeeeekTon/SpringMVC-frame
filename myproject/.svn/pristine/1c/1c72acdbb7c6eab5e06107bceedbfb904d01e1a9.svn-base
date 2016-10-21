package com.dongao.core.common.utils.tedis;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认处理redis脏数据类
 * @date 2013-11-4 下午01:55:57
 * @author dongao
 * @version 1
 */
@SuppressWarnings("unchecked")
public class DefaultCacheDataCleanUp implements CacheDataCleanUpService{
	
	private static Logger logger = LoggerFactory.getLogger(DefaultCacheDataCleanUp.class);
	
	/**
     * 缓存总开关
     */
    protected boolean cacheEnable; 
    
    /**
     * redis数据库工具类服务
     */
    protected TedisUtil  tedisUtil;

	/**
	 * 处理脏数据核心类
	 */
	public void processData(Object targetName, Method method, Object[] paramters, Object obj) {
	    // cacheEnable为spring配置文件中的缓存总开关
        if(cacheEnable){
    		String entityName = parseSimpleEntityName(obj);
    		
    		delCache(entityName);
    		
    		if(logger.isInfoEnabled()){
    			logger.info("delete redis cache data " + entityName + " from redis server");	
    		}
        }
	}
	
	protected void delCache(String entityName){
		tedisUtil.batchDel(entityName);
	}
	
	/**
	 * 通过类名称获得实体名称
	 * @param targetName
	 * @return
	 */
	private String parseSimpleEntityName(Object targetName){
		String entityName = StringUtils.EMPTY;
		
		Class [] clazz = targetName.getClass().getInterfaces();
        String serviceName =  clazz[0].getSimpleName();
        
        entityName = serviceName.substring(0, serviceName.indexOf("Service"));
		
		return entityName;
	}

	public void setTedisUtil(TedisUtil tedisUtil) {
		this.tedisUtil = tedisUtil;
	}

    public void setCacheEnable(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }

}
