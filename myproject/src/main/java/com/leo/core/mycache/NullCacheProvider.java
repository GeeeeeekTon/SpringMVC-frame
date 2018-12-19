package com.leo.core.mycache;

import java.util.Properties;

/**
 * @author zhangzhen
 */
public class NullCacheProvider implements CacheProvider {

	private final static NullCache cache = new NullCache();

	@Override
	public String name() {
		return "none";
	}
	@Override
	public Cache buildCache(String regionName, boolean autoCreate,
			CacheExpiredListener listener) throws CacheException {
		return cache;
	}

	@Override
	public void start(Properties props) throws CacheException {
	}

	@Override
	public void stop() {
	}

}
