package com.leo.modules.template.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.leo.core.mycache.CacheChannel;
import com.leo.core.mycache.util.SerializationUtils;
import com.leo.core.mycomponent.CacheAspect;
import com.leo.core.myutil.JedisTemplate;
@Service
public class CacheMonitorServiceImpl implements CacheMonitorService{

	@Override
	public void evictUserCache(String userId) {
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		JedisTemplate jedisTemplate=JedisTemplate.getInstance();
		List<String> keys = getKeysByUserId(jedisTemplate,CacheAspect.defaultRegionName,userId);
		instance.batchEvict(CacheAspect.defaultRegionName, keys);
	}
	@Override
	public void evictPeriodCache(Long periodId) {
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		JedisTemplate jedisTemplate=JedisTemplate.getInstance();
		List<String> keys = getKeysByPeriodId(jedisTemplate,CacheAspect.defaultRegionName,periodId);
		instance.batchEvict(CacheAspect.defaultRegionName, keys);
	}
	@Override
	public void evictEntiyCache(Class entiy){
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		JedisTemplate jedisTemplate=JedisTemplate.getInstance();
		CacheAspect.evictEnityCacheByMan(instance, jedisTemplate, entiy, CacheAspect.defaultRegionName);
	}
	
	/**查找用户对应的所有keys*/
	public List<String> getKeysByUserId(JedisTemplate jedisTemplate,String region,String userId){
		List<String> list=new ArrayList<String>();
		List<byte[]> hvals = jedisTemplate.hvals(CacheAspect.getUserEntiyMapKey(region,userId).getBytes());
		for(byte[] bytes:hvals){
			list.addAll((List<String>)SerializationUtils.deserialize(bytes));
		}
		return list;
	}

	/**查找周期对应的所有keys*/
	public List<String> getKeysByPeriodId(JedisTemplate jedisTemplate,String region,Long periodId){
		List<String> list=new ArrayList<String>();
		List<byte[]> hvals = jedisTemplate.hvals(CacheAspect.getPeriodEntiyMapKey(region,periodId).getBytes());
		for(byte[] bytes:hvals){
			list.addAll((List<String>)SerializationUtils.deserialize(bytes));
		}
		return list;
	}
	@Override
	public void evictPojoCache(Class entiy, Object entityId) {
		String pojoKey = CacheAspect.getPojoKey(entiy, entityId);
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		instance.evict(CacheAspect.defaultRegionName, pojoKey);
	}



	

}
