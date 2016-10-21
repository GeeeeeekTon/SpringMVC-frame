package com.dongao.core.mycache;

import java.util.List;

/**
 * 空的缓存Provider
 * 
 * @author zhangzhen
 */
public class NullCache implements Cache {

	@Override
	public Object get(Object key) throws CacheException {
		return null;
	}

	@Override
	public void put(Object key, Object value) throws CacheException {
	}

	@Override
	public void update(Object key, Object value) throws CacheException {
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List keys() throws CacheException {
		return null;
	}

	@Override
	public void evict(Object key) throws CacheException {
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void evict(List keys) throws CacheException {
	}

	@Override
	public void clear() throws CacheException {
	}

	@Override
	public void destroy() throws CacheException {
	}

}
