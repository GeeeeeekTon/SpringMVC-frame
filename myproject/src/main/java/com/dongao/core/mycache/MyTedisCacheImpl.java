package com.dongao.core.mycache;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dongao.core.base.pojo.PageParameter;
import com.dongao.core.base.pojo.Pagination;
import com.dongao.core.common.utils.JsonUtil;
import com.dongao.core.common.utils.ReflectUtil;
import com.dongao.core.common.utils.tedis.CacheService;
import com.dongao.core.common.utils.tedis.GenCacheKeyService;
import com.dongao.core.common.utils.tedis.TedisCacheImpl;
import com.dongao.core.common.utils.tedis.TedisUtil;

public class MyTedisCacheImpl extends TedisCacheImpl implements CacheService{
	private static final Logger logger = LoggerFactory .getLogger(MyTedisCacheImpl.class);
	/**
	 * 生成redis key自定义实现
	 */
	private GenCacheKeyService genCacheKeyService; 
	/**
	 * tedis工具类
	 */
	private TedisUtil tedisUtil; 
	/**
	 * 缓存总开关
	 */
	private boolean cacheEnable; 
	/**
	 * 单个对象缓存开关
	 */
	private boolean singleCacheEnable;
	/**查询单个对象规范**/
	private List<String> singleQueryIndexs; 
	/**多个单个对象规范**/
	private List<String> multipleQueryIndexs;

	/**
	 * tedis核心处理缓存方法
	 */
	public Object processCache(MethodInvocation invocation) {
		Object result = null;
		
		try {
			if(TedisUtil.getManagers().size() <= 0){
				if(logger.isInfoEnabled()){
					logger.info(".....redis service is not connected.....");
				}
				
				return result = invocation.proceed();
			} else {
				// cacheEnable为spring配置文件中的缓存总开关
				if(cacheEnable){
					Class<?> cls = invocation.getThis().getClass();
					String methodName =  invocation.getMethod().getName();
					Object[] arguments = invocation.getArguments();
					
					String key = genCacheKeyService.getRedisCacheKey(cls, methodName, arguments); // 根据默认规则生成redis cache key
					
					/**
					 * 根据系统生成的cache key在redis缓存中查找，如果根据key没有找到value，则查询mysql数据库，将查询到的结果返回，并把查询得到的集合缓存，缓存时，
					 * 该key(simplePackageName+methodName+paramtersValueList)对应的value为查询出集合的ID列表(如 User1,User2,User3,User4)，然后分别缓存集合中的
					 * 每个实体对象，缓存规则key 为实体名称+ID表示(如User1)，value为User实体json序列
					 */
					
					if(StringUtils.isNotEmpty(methodName)){
						// 根据编程规范凡是查询单个对象规则方法都是单个实体查询，返回单个实体对象，则查看系统是否开启了单个对象缓存机制，如果没有直接查询mysql数据，如果开启了此服务，
						if(singleQuery(methodName)){
							// 如果开启了单个对象缓存机制，则根据key读取缓存，如果缓存中不存在查询数据，将查询到的数据缓存到redis
							if(singleCacheEnable){
								if(tedisUtil.isExists(key)){
									String json = tedisUtil.tedisGetString(key);
									result = JsonUtil.fromJson(json, invocation.getMethod().getReturnType());
								}else{
									result = invocation.proceed();
									tedisUtil.tedisSetString(key, JsonUtil.toJson(result));
								}
							}
							// 如果没有开启单个对象缓存机制，则直接查询数据库，查询结果不缓存到redis
							else{
								result = invocation.proceed();
							}
						}
						// 根据编程规范凡是查询规则开头的方法，并且结尾不是以查询单个结尾的方法为查询结合方法（包括分页和所有查询）
						else if(multipleQuery(methodName)){
							// 查询是否存在redis缓存中，如果存在则直接读取redis缓存数据(先获得集合ID列表，再取出其中的每一个对象)
							if(tedisUtil.isExists(key)){
								String paginationCacheValue = tedisUtil.tedisGetString(key);
								result = parseListValueByKey(paginationCacheValue, invocation, methodName);
							}
							// 如果redis缓存中不存在该数据，则直接查询数据库，返回数据并将查询结果缓存到redis
							else{
								result = invocation.proceed();
								
								// 根据查询数据库结果result的类型进行不同类型的缓存（类型分为分页数据和不分页数据）
								if(("Pagination").equals(result.getClass().getSimpleName())){
									cachePaginationData(result, key);
								}
								else{
									cacheListData(result, key); // 不分页数据缓存处理
								}
							}
						}else{
							result = invocation.proceed();
						}
					}
				}
				// 如果redis缓存总开关没有开启，则直接查询mysql数据库
				else{
					result = invocation.proceed();
				}
			}
		} catch (Throwable e) {
			logger.error(e.toString());
		}
		
		return result;
	}
	
	/**
	 * 根据缓存中的获得的集合key，解析集合ID列表，根据列表中缓存的每个id，查询redis缓存中的集合对象
	 * @param paginationCacheValue
	 * @param invocation
	 * @param methodName
	 * @return
	 */
	private Object parseListValueByKey(String paginationCacheValue, MethodInvocation invocation, String methodName){
		Object result = null;
		
		if(StringUtils.isNotEmpty(paginationCacheValue)){
			// 如果返回是不带分页的list类型
			if(invocation.getMethod().getReturnType().isAssignableFrom(List.class)){
				result = parseListByCacheValue(paginationCacheValue, invocation);
			}
			// 如果返回是带分页的Pagination类型
			else if(invocation.getMethod().getReturnType().isAssignableFrom(Pagination.class)){
				Pagination pagination = null;
				List list = parseListByCacheValue(paginationCacheValue, invocation);
				PageParameter pageParameter = (PageParameter) ReflectUtil.getParentFieldValue(list.get(0), "pageParameter");
				pagination = new Pagination(list, pageParameter);
				pagination.setPageNo(pageParameter.getCurrentPage());
				pagination.setPageSize(pageParameter.getPageSize());
				pagination.setTotalCount(pageParameter.getTotalCount());
				
				result = pagination;
			}
		}
		
		return result;
	}
	
	/**
	 * 根据实际返回类型封装好list(从redis缓存取得数据)
	 * @param paginationCacheValue
	 * @param invocation
	 * @return
	 */
	private List parseListByCacheValue(String paginationCacheValue, MethodInvocation invocation){
		List list = new ArrayList();
		Class catuClazz = parseClassByInvocation(invocation); // 根据invocation获得返回list集合或分页中的泛型实际对象
		
		String [] cacheLists = paginationCacheValue.split(",");
		for(String cacheKey : cacheLists){
			String json = tedisUtil.tedisGetString(cacheKey);
			try {
				list.add(JsonUtil.fromJson(json,catuClazz));
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		
		return list;
	}
	
	/**
	 * 根据invocation获得返回list集合或分页中的泛型实际对象
	 * @param invocation
	 * @return
	 */
	private Class<?> parseClassByInvocation(MethodInvocation invocation){
		Class clazz = null;
		
		if(invocation == null){
			return null;
		}
		
		Type type = invocation.getMethod().getGenericReturnType();
		if(type instanceof ParameterizedType){
			ParameterizedType parameterizedType = (ParameterizedType) type;
			clazz = (Class) parameterizedType.getActualTypeArguments()[0];
		} 
		
		return clazz;
	}
	
	/**
	 * 缓存集合数据对象(不分页)
	 * @param result
	 * @param key
	 */
	private void cacheListData(Object result, String key){
		// 缓存key对象的value(查询结果list集合)
		String cacheVal = parseListDataCacheValue(result);
		
		tedisUtil.tedisSetString(key, cacheVal);
	}
	
	/**
	 * 根据查询到的数据集合，拼接key并将每个集合缓存到redis(不分页集合)
	 * @param obj
	 * @return
	 */
	private String parseListDataCacheValue(Object result){
		StringBuffer cacheVal = new StringBuffer(StringUtils.EMPTY);
		String className = StringUtils.EMPTY;
		
		List<Object> data = (List<Object>) result;
		if(data != null && data.size() > 0){
			className = data.get(0).getClass().getSimpleName();
			int size  = data.size();
			
			for(int i=0; i<size; i++){
				Object obj = data.get(i);
				String id = ReflectUtil.getFieldValue(obj, "id").toString();
				
				// 将集合中的每个对象缓存到redis
				cacheSingleVal(obj, className, id);
				
				// 拼接集合ID列表
				if(i == size-1){
					cacheVal.append(className).append(id);
				}else{
					cacheVal.append(className).append(id).append(",");
				}
			}
		}
		
		return cacheVal.toString();
	}
 	
	/**
	 * 缓存分页数据对象
	 * @param result
	 * @param key
	 */
	private void cachePaginationData(Object result, String key){
		// 缓存key对象的value（查询结果ID集合列表）
		String cacheVal = parsePaginationDataCacheValue(result);
		
		tedisUtil.tedisSetString(key, cacheVal);
	}
	
	/**
	 * 根据查询到的数据，拼接对应该集合key的value，规则：将集合中所有对象的类名和ID拼接
	 * @param result
	 * @return
	 */
	private String parsePaginationDataCacheValue(Object result){
		StringBuffer cacheVal = new StringBuffer(StringUtils.EMPTY);
		String className = StringUtils.EMPTY;
		
		Pagination<Object> data = (Pagination<Object>) result;
		if(data != null && data.getList() != null && data.getList().size() > 0){
			className = data.getList().get(0).getClass().getSimpleName();
			int size  = data.getList().size();
			
			PageParameter pageParameter =  new PageParameter(data.getPageNo(), data.getPageSize());
			pageParameter.setTotalCount(data.getTotalCount());
			pageParameter.setTotalPage(data.getTotalPage());
			
			for(int i=0; i<size; i++){
				Object obj = data.getList().get(i);
				String id = ReflectUtil.getFieldValue(obj, "id").toString();
				ReflectUtil.setParentFieldValue(obj, "pageParameter", PageParameter.class, pageParameter);  // 将分页信息直接写入对象
				
				// 将集合中的每个对象缓存到redis
				cacheSingleVal(obj, className, id);
				
				// 拼接集合ID列表
				if(i == size-1){
					cacheVal.append(className).append(id);
				}else{
					cacheVal.append(className).append(id).append(",");
				}
			}
		}
		
		return cacheVal.toString();
	}
	
	/**
	 * 缓存单个对象
	 * @param obj
	 * @param className
	 * @param id
	 */
	private void cacheSingleVal(Object obj, String className, String id){
		StringBuffer key = new StringBuffer(className);
		key.append(id);
		
		try {
			if(!tedisUtil.isExists(key.toString())){
				tedisUtil.tedisSetString(key.toString(), JsonUtil.toJson(obj));
			}
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}

	public void setTedisUtil(TedisUtil tedisUtil) {
		this.tedisUtil = tedisUtil;
	}

	public void setCacheEnable(boolean cacheEnable) {
		this.cacheEnable = cacheEnable;
	}

	public void setSingleCacheEnable(boolean singleCacheEnable) {
		this.singleCacheEnable = singleCacheEnable;
	}

	public void setGenCacheKeyService(GenCacheKeyService genCacheKeyService) {
		this.genCacheKeyService = genCacheKeyService;
	}
	private boolean singleQuery(String methodName) {
		// assert methodName not null
		boolean flag = false;
		for (String str : singleQueryIndexs) {
			if (methodName.startsWith(str)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	private boolean multipleQuery(String methodName) {
		// assert methodName not null
		boolean flag = false;
		for (String str : multipleQueryIndexs) {
			if (methodName.startsWith(str)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public void setSingleQueryIndexs(List<String> singleQueryIndexs) {
		this.singleQueryIndexs = singleQueryIndexs;
	}

	public void setMultipleQueryIndexs(List<String> multipleQueryIndexs) {
		this.multipleQueryIndexs = multipleQueryIndexs;
	}

	
}
