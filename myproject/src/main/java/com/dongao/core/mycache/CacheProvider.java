package com.dongao.core.mycache;

import java.util.Properties;

/**
 * Support for pluggable caches.
 * @author zhangzhen
 */
public interface CacheProvider {

	/**
	 * 缓存的标识名称
	 * @return
	 */
	public String name();
	
	/**
	 * Configure the cache
	 *
	 * @param regionName the name of the cache region
	 * @param autoCreate autoCreate settings
	 * @param listener listener for expired elements
	 * @throws CacheException
	 */
	public Cache buildCache(String regionName, boolean autoCreate, CacheExpiredListener listener) throws CacheException;

	/**
	 * Callback to perform any necessary initialization of the underlying cache implementation
	 * during SessionFactory construction.
	 *
	 * @param properties current configuration settings.
	 */
	public void start(Properties props) throws CacheException;

	/**
	 * Callback to perform any necessary cleanup of the underlying cache implementation
	 * during SessionFactory.close().
	 */
	public void stop();
	
}