package com.dongao.modules.template.service;

public interface CacheMonitorService {
	/**
	 * 清除指定用户的缓存
	 * @param userId
	 */
	void evictUserCache(String userId);
	/**
	 * 清除指定周期缓存
	 * @param periodId
	 */
	void evictPeriodCache(Long periodId);
	/**
	 * 如果手动在数据更新了一个对象需要调用这个方法来刷新对应pojo缓存
	 * @param entiy
	 * @param entityId
	 */
	void evictPojoCache(Class entiy,Object entityId);
	/**
	 * 手动清除一个实体上所有的缓存
	 * @param entiy
	 */
	void evictEntiyCache(Class entiy);
}
