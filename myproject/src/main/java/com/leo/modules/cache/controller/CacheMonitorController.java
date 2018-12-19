package com.leo.modules.cache.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leo.core.mybase.MySwitch;
import com.leo.core.mybase.SimplePermissionControlle;
import com.leo.core.mycache.CacheChannel;
import com.leo.core.mycache.CacheObject;
import com.leo.core.mycomponent.BrowseLimiterFilter;
import com.leo.core.mycomponent.CacheAspect;
import com.leo.core.mycomponent.OtherCacheAspect;
import com.leo.core.mycomponent.benchmark.RedisSetGetBenchmark;
import com.leo.core.myutil.Collections3;
import com.leo.core.myutil.JsonMapper;
import com.leo.core.myutil.StringUtil;
import com.leo.modules.template.service.CacheMonitorService;

/**
 * TODO 权限保护:登陆以后 直接按照基于角色判断
 * @author zhangzhen
 * v1.0
 */
@Controller
@RequestMapping("/cacheMonitor")
public class CacheMonitorController extends SimplePermissionControlle{
	
	private static final String modelPackage="com.leo.rbc.model";
	private static AtomicBoolean inUse = new AtomicBoolean(false);
    @RequestMapping("/test")
    public @ResponseBody String test() {
    	System.err.println(this);
    	return this.toString();
    }
	
	@RequestMapping(value = "/redisTest", produces="text/html;charset=utf-8")
	public @ResponseBody
	String redisTest(HttpServletRequest request,HttpServletResponse response) {
		String threadCount=request.getParameter("threadCount");
		if(StringUtil.isEmptyString(threadCount)){
			return "请传入threadCount";
		}
		String totalCount=request.getParameter("totalCount");
		if(StringUtil.isEmptyString(totalCount)){
			return "请传入totalCount";
		}
		int defaultThreadCount=RedisSetGetBenchmark.DEFAULT_THREAD_COUNT;
		long defaultTotalCount=RedisSetGetBenchmark.DEFAULT_TOTAL_COUNT;
		try {
			defaultThreadCount=Integer.parseInt(threadCount);
			defaultTotalCount=Long.parseLong(totalCount);
		} catch (Exception e) {
			e.printStackTrace();
			return "请传入合法的数据格式";
		}
		if (!inUse.compareAndSet(false, true)) {
			return MySwitch.getAppInfo()+" Needs to be used by one client at a time";
		}
		try {
			RedisSetGetBenchmark benchmark = new RedisSetGetBenchmark(
					defaultThreadCount, defaultTotalCount);
			benchmark.execute();
			return MySwitch.getAppInfo()+" done";
		} catch (Exception e) {
			e.printStackTrace();
			return MySwitch.getAppInfo()+" exception";
		} finally {
			inUse.set(false);
		}
	}
	/**慎用**/
	@RequestMapping(value = "/clearCacheByRegionName", produces="text/html;charset=utf-8")
	public @ResponseBody String clearCacheByRegionName(String regionName){
		if(StringUtil.isEmptyString(regionName)){
			return "false";
		}
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		instance.clear(regionName);
		return "true";
	}
	/**慎用，OtherCache的全局清除*/
	@RequestMapping(value = "/clearOtherCache", produces="text/html;charset=utf-8")
	public @ResponseBody String cleaOtherCache(){
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		instance.clear(OtherCacheAspect.defaultRegionName);
		return "true";
	}
	/**慎用，影响性能*/
	@RequestMapping(value = "/getOtherCacheKeys", produces="text/html;charset=utf-8")
	public @ResponseBody String getOtherCacheKeys(){
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		List keys = instance.keys(OtherCacheAspect.defaultRegionName);
		if(Collections3.isNotEmpty(keys)){
			return super.getJsonString(JsonMapper.toJson(keys));
		}
		return "";
	}
	/**慎用，影响性能*/
	@RequestMapping(value = "/getDbCacheKeys", produces="text/html;charset=utf-8")
	public @ResponseBody String getDbCacheKeys(){
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		List keys = instance.keys(CacheAspect.defaultRegionName);
		if(Collections3.isNotEmpty(keys)){
			return super.getJsonString(JsonMapper.toJson(keys));
		}
		return "";
	}
	/**慎用，LimitCache的全局清除*/
	@RequestMapping(value = "/clearLimitCache", produces="text/html;charset=utf-8")
	public @ResponseBody String cleaLimitCache(){
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		instance.clear(BrowseLimiterFilter.defaultRegionName);
		return "true";
	}
	/**慎用，DbCache的全局清除*/
	@RequestMapping(value = "/clearDbCache", produces="text/html;charset=utf-8")
	public @ResponseBody String clearDbCache(){
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		instance.clear(CacheAspect.defaultRegionName);
		return "true";
	}
	/**获得具体key对应的值*/
	@RequestMapping(value = "/getValueByKey", produces="text/html;charset=utf-8")
	public @ResponseBody String getValueByKey(String key){
		if(StringUtil.isEmptyString(key)){
			return "false";
		}
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		CacheObject cacheObject = instance.get(CacheAspect.defaultRegionName, key);
		Object value = cacheObject.getValue();
		if(value!=null){
			return super.getJsonString(JsonMapper.toJson(value));
		}else{
			return "false";
		}
	}
	/**解码key*/
//	@RequestMapping(value = "/decodeKey", produces="text/html;charset=utf-8")
//	public @ResponseBody String decodeKey(String key){
//		if(StringUtil.isEmptyString(key)){
//			return "false";
//		}
//		byte[] decode = Base64.decode(key);
//		if(decode!=null&&decode.length>0){
//			Object object = SerializationUtils.deserialize(decode);
//			if(object!=null){
//				return super.getJsonString(JsonMapper.toJson(object));
//			}
//		}
//		return "false";
//	}
	/**清除所有实体的缓存信息*/
//	@RequestMapping(value = "/evictAllEntityCache", produces="text/html;charset=utf-8")
//	public @ResponseBody String evictAllEntityCache(){
//		List<Class<?>> classList = Reflections.getClassList(modelPackage,false);
//		CacheChannel instance = CacheChannel.getInstance();//缓存助手
//		List<String> keys=new ArrayList<String>();
//		for(Class c:classList){
//			keys.add(c.getName()+"*");
//			keys.add(CacheAspect.noOptimizePrefix+c.getName()+"*");
//		}
//		instance.batchEvict(CacheAspect.defaultRegionName,keys);
//		return "true";
//	}
	/**清除单个实体的缓存信息*/
	@RequestMapping(value = "/evictEntityCache", produces="text/html;charset=utf-8")
	public @ResponseBody String evictEntityCache(String entityName){
		if(StringUtil.isEmptyString(entityName)){
			return "false";
		}
		try {
			Class<?> cls =Class.forName(entityName);
			cacheMonitorService.evictEntiyCache(cls);
		} catch (ClassNotFoundException e) {
			return "false";
		}
		return "true";
	}
//	/**清除多个实体的缓存信息*/
//	@RequestMapping(value = "/evictEntitysCache", produces="text/html;charset=utf-8")
//	public @ResponseBody String evictEntitysCache(String entitysName){
//		if(StringUtil.isEmptyString(entitysName)){
//			return "false";
//		}
//		String[] split = entitysName.split(",");
//		try {
//			for(String entityName:split){
//				Class.forName(entityName);
//			}
//		} catch (ClassNotFoundException e) {
//			LogUtils.logError("CacheMonitorController evictEntityCache entityName not exists", e);
//			return "false";
//		}
//		//TODO
//		return "true";
//	}
	/**清除指定用户的缓存信息*/
	@RequestMapping(value = "/evictUserCache", produces="text/html;charset=utf-8")
	public @ResponseBody String evictUserCache(String userId){
		if(StringUtil.isEmptyString(userId)){
			return "false";
		}
		cacheMonitorService.evictUserCache(userId);
		return "true";
	}
	/**清除指定合作周期的缓存信息*/
	@RequestMapping(value = "/evictPeriodCache", produces="text/html;charset=utf-8")
	public @ResponseBody String evictPeriodCache(Long periodId){
		if(periodId==null||periodId.longValue()==0l){
			return "false";
		}
		cacheMonitorService.evictPeriodCache(periodId);
		
		return "true";
	}
	
	/**清除pojo缓存信息*/
	@RequestMapping(value = "/evictPojoCache", produces="text/html;charset=utf-8")
	public @ResponseBody String evictEntityCache(String entityName,String entityId){
		if(StringUtil.isEmptyString(entityName)||StringUtil.isEmptyString(entityId)){
			return "false";
		}
		try {
			Class<?> cls = Class.forName(entityName);
			cacheMonitorService.evictPojoCache(cls, entityId);
		} catch (ClassNotFoundException e) {
			return "false";
		}
		return "true";
	}
	/**这里的命中信息只是集群中的单个app的统计信息*/
	@RequestMapping(value = "/getMonitorInfo", produces="text/html;charset=utf-8")
	public @ResponseBody String getMonitorInfo(){
		Map map=new HashMap();
		CacheChannel instance = CacheChannel.getInstance();//缓存助手
		map.put("appInfo", MySwitch.getAppInfo());
		map.put("hitCount", instance.getHitCount());
		map.put("missCount", instance.getMissCount());
		return super.getJsonString(JsonMapper.toJson(map));
	}
	@Autowired
	private CacheMonitorService cacheMonitorService;
	
}
