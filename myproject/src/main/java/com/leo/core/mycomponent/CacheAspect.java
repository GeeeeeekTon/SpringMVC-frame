package com.leo.core.mycomponent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.leo.core.mybase.MySwitch;
import com.leo.core.mycache.CacheChannel;
import com.leo.core.mycache.CacheObject;
import com.leo.core.mycache.Expiry;
import com.leo.core.mycache.annotation.CacheClear;
import com.leo.core.mycache.annotation.CachePut;
import com.leo.core.mycache.annotation.CacheUpdate;
import com.leo.core.mycache.util.SerializationUtils;
import com.leo.core.myutil.Base64;
import com.leo.core.myutil.Collections3;
import com.leo.core.myutil.JedisTemplate;
import com.leo.core.myutil.KeyLock;
import com.leo.core.myutil.LogUtils;
import com.leo.core.myutil.Md5Util;
import com.leo.core.myutil.Reflections;
import com.leo.core.myutil.StringUtil;
import com.leo.modules.LoadService;

/**
 * @author zhangzhen
 * 
 * v3.0-Beta
 */
@Component
@Aspect
public class CacheAspect implements Ordered {
	//TODO 放到配置文件里
	private static boolean debugFlag=false;
	private final KeyLock<String> keyLock=new KeyLock<String>();
	public static final String defaultRegionName="myProject";
	public static final String operateUserIdPrefix="operateUserId:";
	public static final String operatePeriodIdPrefix="operatePeriodId:";
	public static final String lockKey="dkeylock:";
	//find by id 命名规范,这里这样要求，是为了提高性能，节约存储空间，如果不遵守也不会有问题
	private static final String load="load";
	public static final String loadPrefix="load";//reference com.leo.rbc.service.LoadService
	public static final int pojoExpire=(int) Expiry.FOUR_HOUR; 
	
	public static Map<String,String[]> propertysNameCaches=new ConcurrentHashMap<String,String[]>();//下一版   V3.1支持
	//reference org.springframework.cache.interceptor.ExpressionEvaluator
	//缓存有@OperationDescription方法参数名称  
    private static Map<String, String[]> parameterNameCaches = new ConcurrentHashMap<String, String[]>(64);  
    //缓存SPEL Expression  
    private static Map<String, Expression> spelExpressionCaches = new ConcurrentHashMap<String, Expression>(64);  
    private static ExpressionParser parser = new SpelExpressionParser();
    private static LocalVariableTableParameterNameDiscoverer parameterNameDiscovere = new LocalVariableTableParameterNameDiscoverer();  
    private static ParserContext parserContext = new ParserContext() {  
        @Override  
        public boolean isTemplate() {  
            return true;  
        }  
        @Override  
        public String getExpressionPrefix() {  
            return "${";  
        }  
        @Override  
        public String getExpressionSuffix() {  
            return "}";  
        }  
    };
    
	@Around("com.leo.core.mycomponent.DongaoNamePointcut.cacheCut()")
	public Object joinPointCache(ProceedingJoinPoint pjp) {
		if(!MySwitch.cacheEnable){
			try {
				return pjp.proceed();
			} catch (Throwable e) {
				LogUtils.logError("aop异常：", e);
				return null;
			}finally{
				//针对异步请求，不走ThreadHandlerInterceptor,可能这个资源会清理不掉，虽然不会引发内存泄露  ,但是手动调用remove有助于gc
				//MyThreadVariable.removeOperatePeriodId();
				//MyThreadVariable.removeOperateUserId();
			}
		}
		//long startTime = System.currentTimeMillis();
		try {
			Object result = null;// 定义方法返回值
			
			CacheChannel instance = CacheChannel.getInstance();//缓存助手
			
			JedisTemplate jedisTemplate = JedisTemplate.getInstance();//jedis 操作模板
			
			Object proxy  = pjp.getThis();//代理对象
			
			Class<?> proxyClass = proxy.getClass();//代理类
			
			Class<?> cls = pjp.getTarget().getClass();//目标类
			
			MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();  
	
			Method method =joinPointObject.getMethod();//目标方法
			
			Object[] params = pjp.getArgs();//方法参数
			
			LoadService loadService = SpringUtils.getBean("loadServiceImpl", LoadService.class);
			
			if(method!=null){//assert not  null
				if (method.isAnnotationPresent(CachePut.class)) {
					CachePut cachePut = method.getAnnotation(CachePut.class);
					String region = cachePut.region();
					if(StringUtil.isEmptyString(region)){
						region=defaultRegionName;
					}
					
					int expiry = (int) cachePut.expiry();

					String cacheKey = cachePut.key();
					
					Class entiy = cachePut.entity();
					
					if(StringUtil.isEmptyString(cacheKey)){
						cacheKey = Md5Util.encrypt(entiy.getName() + "-" +cls.getName()+"-"+ method.getName() + "-" + getObjectsString(params));
					}
					boolean isPojo=cachePut.isPojo();
					Method loadMethod = Reflections.getAccessibleMethodByName(loadService, loadPrefix+entiy.getSimpleName());
					if(loadMethod==null){//never not //ucenter TODO 
						//LogUtils.getErrorLog().error(loadPrefix+entiy.getSimpleName()+"在类:"+LoadService.class.getName()+"未定义!");
					}
					//1.从缓存中找数据
					if(isPojo&&loadMethod!=null){
						if(method.getReturnType().isAssignableFrom(List.class)){//only List? 性能优化
							result=dealListForCachePut(jedisTemplate,result,instance,region,entiy,cacheKey,loadMethod,loadService);
						}else if(method.getName().equalsIgnoreCase(load)&&params!=null&&params.length==1){
							cacheKey=getPojoKey(entiy,params[0]);
							result=dealPojoForCachePut(result,instance,region,cacheKey);
						}else if(method.getReturnType().isAssignableFrom(entiy)){//应该基于pojo的共性判断最好  why? eg:findPojobyEmail or findojoByUserName
							result=dealOtherPojoForCachePut(jedisTemplate,result,instance,region,entiy,cacheKey,loadMethod,loadService);
						}else{
							result=dealDefaultForCachePut(result,instance,cacheKey,region);
						}
					}else{
						result=dealDefaultForCachePut(result,instance,cacheKey,region);
					}
					//2.从缓存中找不到数据时
					if (result == null) {
							if(isPojo&&loadMethod!=null){
								if(method.getName().equalsIgnoreCase(load)&&params!=null&&params.length==1){
									String lockKey=getLockKey(cacheKey);
									keyLock.lock(lockKey);
									try{
										result=dealPojoForCachePut(result,instance,region,cacheKey);
										if(result==null){
											result = pjp.proceed(); // 从 DB 中获取
											if (result != null) {
												instance.set(region, cacheKey, result);
//												if(expiry!=-1){
//													jedisTemplate.expire(getKeyName(region, cacheKey).getBytes(),expiry);
//												}
												jedisTemplate.expire(getKeyName(region, cacheKey).getBytes(),pojoExpire);
												//dealRelEntitys(jedisTemplate,cacheKey,cachePut,instance,region,entiy);// not need  ,TODO  for batchUpdate
											}
										}
									}finally{
										keyLock.unlock(lockKey);
									}
								}else{
										if(method.getReturnType().isAssignableFrom(List.class)){//only List? 性能优化
											String lockKey=getLockKey(cacheKey);
											keyLock.lock(lockKey);
											try{
												result=dealListForCachePut(jedisTemplate,result,instance,region,entiy,cacheKey,loadMethod,loadService);
												if(result==null){
													result = pjp.proceed(); // 从 DB 中获取
													if (result != null) {
														List myValue = Collections3.extractToList((Collection)result, "id");//主键 约定为id
														instance.set(region, cacheKey, myValue);
														if(expiry!=-1){
															jedisTemplate.expire(getKeyName(region, cacheKey).getBytes(),expiry);
														}
														dealRelEntitys(jedisTemplate,cacheKey,cachePut,instance,region,entiy,pjp);
													}
												}
											}finally{
												keyLock.unlock(lockKey);
											}
										}else if(method.getReturnType().isAssignableFrom(entiy)){//why? eg:findPojobyEmail or findojoByUserName
											String lockKey=getLockKey(cacheKey);
											keyLock.lock(lockKey);
											try{
												result=dealOtherPojoForCachePut(jedisTemplate,result,instance,region,entiy,cacheKey,loadMethod,loadService);
												if(result==null){
													result = pjp.proceed(); // 从 DB 中获取
													if (result != null) {
														instance.set(region, cacheKey, Reflections.invokeGetter(result, "id"));
														if(expiry!=-1){
															jedisTemplate.expire(getKeyName(region, cacheKey).getBytes(),expiry);
														}
														dealRelEntitys(jedisTemplate,cacheKey,cachePut,instance,region,entiy,pjp);
													}
												}
											}finally{
												keyLock.unlock(lockKey);
											}
										}else{
											String lockKey=getLockKey(cacheKey);
											keyLock.lock(lockKey);
											try{
												result=dealDefaultForCachePut(result,instance,cacheKey,region);
												if(result==null){
													result = pjp.proceed(); // 从 DB 中获取
													if (result != null) {
														instance.set(region, cacheKey, result);// 放入 Cache 中
														if(expiry!=-1){
															jedisTemplate.expire(getKeyName(region, cacheKey).getBytes(),expiry);
														}
														dealRelEntitys(jedisTemplate,cacheKey,cachePut,instance,region,entiy,pjp);
													}
												}
											}finally{
												keyLock.unlock(lockKey);
											}
										}
								}
							}else{
								String lockKey=getLockKey(cacheKey);
								keyLock.lock(lockKey);
								try{
									result=dealDefaultForCachePut(result,instance,cacheKey,region);
									if(result==null){
										result = pjp.proceed(); // 从 DB 中获取
										if (result != null) {
											instance.set(region, cacheKey, result);
											if(expiry!=-1){
												jedisTemplate.expire(getKeyName(region, cacheKey).getBytes(),expiry);
											}
											dealRelEntitys(jedisTemplate,cacheKey,cachePut,instance,region,entiy,pjp);
										}
									}
								}finally{
									keyLock.unlock(lockKey);
								}
							}
					}
				} else if (method.isAnnotationPresent(CacheClear.class)) {
					CacheClear cacheClear = method.getAnnotation(CacheClear.class);
					boolean methodBefore = cacheClear.methodBefore();
					if(methodBefore){
						dealCacheClear(cacheClear,instance,pjp,jedisTemplate);
						result = pjp.proceed();
					}else{
						result = pjp.proceed();
						dealCacheClear(cacheClear,instance,pjp,jedisTemplate);
					}
				}else if(method.isAnnotationPresent(CacheUpdate.class)){
					CacheUpdate cacheUpdate = method.getAnnotation(CacheUpdate.class);
					dealCacheUpdate(cacheUpdate,instance,pjp,jedisTemplate);
					result = pjp.proceed();
				} else {
					// 若不带有任何的 Cache 注解，则直接进行数据库操作
					result = pjp.proceed();
				}
			}else{
				result = pjp.proceed();//never not 
				LogUtils.getErrorLog().error(cls.getName()+"在aop缓存拦截过程中发生了不可能发生的异常，拦截的method找不到！");
			}
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
			LogUtils.logError("缓存处理过程拦截到了异常：", e);
			return null;
		}finally{
			//LogUtils.BUSINESS_LOG.info("Statistic_Cost_Time_Redis_CacheAspect:{}",(System.currentTimeMillis() - startTime));
			//针对异步请求，不走ThreadHandlerInterceptor,可能这个资源会清理不掉，虽然不会引发内存泄露  ,但是手动调用remove有助于gc
//			MyThreadVariable.removeOperatePeriodId();
//			MyThreadVariable.removeOperateUserId();
		}
	}
	private void dealCacheClear(CacheClear cacheClear,CacheChannel instance,ProceedingJoinPoint pjp,JedisTemplate jedisTemplate){
		Class[] entiys = cacheClear.entitys();
		boolean transactionSupport=cacheClear.transactionSupport();
		String[] regionNames = cacheClear.regions();
		if (Collections3.isNotEmpty(regionNames)) {
			for (String region : regionNames) {
				instance.clear(region);
			}
		}else{
			String region = cacheClear.region();
			if(StringUtil.isEmptyString(region)){
				region=defaultRegionName;
			}
			String[] keys = cacheClear.keys();
			if(Collections3.isNotEmpty(keys)){
				instance.batchEvict(region, Arrays.asList(keys));
			}else{
				if(Collections3.isNotEmpty(entiys)){//assert not null
					List<String> list=new ArrayList<String>();
					for(Class enity:entiys){
						if(StringUtil.isNotEmptyString(MyThreadVariable.getOperateUserId())){
							String lockKey=getLockKey(enity.getName()+MyThreadVariable.getOperateUserId());
							keyLock.lock(lockKey);
							try{
								List<String> ks = getKeysByUserEntiy(jedisTemplate, region, enity, MyThreadVariable.getOperateUserId());
								if(Collections3.isNotEmpty(ks)){
									list.addAll(ks);
									delUserEntiyMap(jedisTemplate, region, enity, MyThreadVariable.getOperateUserId());
								}
							}finally{
								keyLock.unlock(lockKey);
							}
						}else if(MyThreadVariable.getOperatePeriodId()!=null&&MyThreadVariable.getOperatePeriodId()!=0l){
							String lockKey=getLockKey(enity.getName()+MyThreadVariable.getOperatePeriodId());
							keyLock.lock(lockKey);
							try{
								List<String> ks = getKeysByPeriodEntiy(jedisTemplate, region, enity, MyThreadVariable.getOperatePeriodId());
								if(Collections3.isNotEmpty(ks)){
									list.addAll(ks);
									delPeriodEntiyMap(jedisTemplate, region, enity, MyThreadVariable.getOperatePeriodId());
								}
							}finally{
								keyLock.unlock(lockKey);
							}
						}else{
							String lockKey=getLockKey(enity.getName());//是否有必要加锁？
							keyLock.lock(lockKey);
							try{
								List<String> ks = getKeysByEntiy(jedisTemplate,region,enity);
								if(Collections3.isNotEmpty(ks)){
									list.addAll(ks);
									delEntiyMap(jedisTemplate, region, enity);
									MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();  
									//LogUtils.getErrorLog().warn(enity.getName()+"上的缓存被清除，性能会有下降，请注意检查是否必须，调用的类:"+pjp.getTarget().getClass().getName()+",方法"+joinPointObject.getMethod().getName());
								}
							}finally{
								keyLock.unlock(lockKey);
							}
						}
					}
					instance.batchEvict(region, list,transactionSupport);
				}
			}
		}
	}
	/**手动清除一个实体的所有缓存:apply 通过别的方式直接操作了一个表*/
	public static void  evictEnityCacheByMan(CacheChannel instance,JedisTemplate jedisTemplate,Class enity,String region){
		synchronized ("MANLOCK"+enity.getName()) {
			List<String> list=new ArrayList<String>();
			List<String> ks = getKeysByEntiy(jedisTemplate,region,enity);
			if(Collections3.isNotEmpty(ks)){
				list.addAll(ks);
				delEntiyMap(jedisTemplate, region, enity);
			}
			list.add(enity.getName()+"-"+"*");//pojo keys
			instance.batchEvict(region, list,false);
		}
	}
	private  void dealCacheUpdate(CacheUpdate cacheUpdate,CacheChannel instance,ProceedingJoinPoint pjp,JedisTemplate jedisTemplate){
		String region = cacheUpdate.region();
		if(StringUtil.isEmptyString(region)){
			region=defaultRegionName;
		}
		boolean transactionSupport=cacheUpdate.transactionSupport();
		Class entity = cacheUpdate.entity();
		String idTemplate = cacheUpdate.id();
		if(StringUtil.isNotEmptyString(idTemplate)){
			Object entiyId = executeTemplate(idTemplate,pjp);
			String entiyKey=entity.getName()+"-"+entiyId;
			List<String> list=new ArrayList<String>();
			list.add(entiyKey);//?? TODO 区别 CacheClear 下一版 优化  性能提升 V3.1
			if(StringUtil.isNotEmptyString(MyThreadVariable.getOperateUserId())){ 
				String lockKey=getLockKey(entiyId+entity.getName()+MyThreadVariable.getOperateUserId());
				keyLock.lock(lockKey);
				try{
					List<String> ks = getKeysByUserEntiy(jedisTemplate, region, entity, MyThreadVariable.getOperateUserId());
					if(Collections3.isNotEmpty(ks)){
						list.addAll(ks);
						delUserEntiyMap(jedisTemplate, region, entity, MyThreadVariable.getOperateUserId());
					}
				}finally{
					keyLock.unlock(lockKey);
				}
			}else if(MyThreadVariable.getOperatePeriodId()!=null&&MyThreadVariable.getOperatePeriodId()!=0l){
				String lockKey=getLockKey(entiyId+entity.getName()+MyThreadVariable.getOperatePeriodId());
				keyLock.lock(lockKey);
				try{
					List<String> ks = getKeysByPeriodEntiy(jedisTemplate, region, entity, MyThreadVariable.getOperatePeriodId());
					if(Collections3.isNotEmpty(ks)){
						list.addAll(ks);
						delPeriodEntiyMap(jedisTemplate, region, entity, MyThreadVariable.getOperatePeriodId());
					}
				}finally{
					keyLock.unlock(lockKey);
				}
			}else{
				String lockKey=getLockKey(entiyId+entity.getName());
				keyLock.lock(lockKey);
				try{
					List<String> ks = getKeysByEntiy(jedisTemplate,region,entity);
					if(Collections3.isNotEmpty(ks)){
						list.addAll(ks);
						delEntiyMap(jedisTemplate, region, entity);
						MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();  
						LogUtils.getErrorLog().warn(entity.getName()+"上的缓存被清除，性能会有下降，请注意检查是否必须，调用的类:"+pjp.getTarget().getClass().getName()+",方法"+joinPointObject.getMethod().getName());
					}
				}finally{
					keyLock.unlock(lockKey);
				}
			}
			instance.batchEvict(region, list,transactionSupport);
		}
	}
	private Object dealOtherPojoForCachePut(JedisTemplate jedisTemplate,Object result,CacheChannel instance,String region,Class entiy,String cacheKey,Method loadMethod,LoadService loadService)
			throws Exception{
		CacheObject cacheObject = instance.get(region, cacheKey);
		if(cacheObject.getValue()!=null){
			Object entiyId= cacheObject.getValue();
			if(entiyId!=null){
				CacheObject singleObject = instance.get(region, getPojoKey(entiy,entiyId));
				Object ret=singleObject.getValue();
				if(ret==null){
					ret= loadMethod.invoke(loadService, entiyId);
					if(ret==null){
						LogUtils.getErrorLog().error(entiy.getName()+"对应id为"+entiyId+"的对象不存在，数据库数据混乱！");
					}else{
						instance.set(region, getPojoKey(entiy,entiyId), ret);
						jedisTemplate.expire(getKeyName(region, cacheKey).getBytes(),pojoExpire);
					}
				}
				result=ret;
			}
		}
		return result;
	}
	private Object dealDefaultForCachePut(Object result,CacheChannel instance,String cacheKey,String region){
		CacheObject cacheObject = instance.get(region, cacheKey); // 从Cache中获取 assert not  null
		result = cacheObject.getValue();
		return result;
	}
	private Object dealPojoForCachePut(Object result,CacheChannel instance,String region,String cacheKey){
		CacheObject singleObject = instance.get(region, cacheKey); // 从Cache中获取 assert not  null
		result = singleObject.getValue();
		return result;
	}
	private Object dealListForCachePut(JedisTemplate jedisTemplate,Object result,CacheChannel instance,String region,Class entiy,String cacheKey,Method loadMethod,LoadService loadService)throws Exception{
		CacheObject cacheObject = instance.get(region, cacheKey);
		if(cacheObject.getValue()!=null&&cacheObject.getValue() instanceof List ){
			List myValue= (List) cacheObject.getValue();
			if(Collections3.isNotEmpty(myValue)){
				List list=new ArrayList();
				for(Object id:myValue){
					CacheObject singleObject = instance.get(region, getPojoKey(entiy,id));
					Object ret=singleObject.getValue();
					if(ret==null){
						ret= loadMethod.invoke(loadService, id);
						if(ret==null){
							LogUtils.getErrorLog().error(entiy.getName()+"对应id为"+id+"的对象不存在，数据库数据混乱！");
							continue;
						}
						instance.set(region, getPojoKey(entiy,id), ret);
						jedisTemplate.expire(getKeyName(region, cacheKey).getBytes(),pojoExpire);
					}
					list.add(ret);//assert ret not null
				}
				result=list;
			}
		}
		return result;
	}
	private  void dealRelEntitys(JedisTemplate jedisTemplate,String cacheKey,CachePut cachePut,CacheChannel instance,String region,Class entiy,ProceedingJoinPoint pjp){
		Class[] relEntiys = cachePut.relEntitys();//放入相关联的实体 支持一对多
		CacheModuleType cacheModuleType = cachePut.type();
		Class[] newRelEntiys=null;
		if (Collections3.isEmpty(relEntiys)) {
			newRelEntiys=new Class[]{entiy};
		}else{
			newRelEntiys=new Class[relEntiys.length];
			for(int i=0;i<relEntiys.length;i++){
				newRelEntiys[i]=relEntiys[i];
			}
		}
		for(Class ent:newRelEntiys){//TODO 不排除重复
			if(debugFlag){
				if(cacheModuleType==CacheModuleType.OPERATEUSERID&&StringUtil.isEmptyString(MyThreadVariable.getOperateUserId())){
					MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();  
					LogUtils.getErrorLog().warn("缓存使用方式为CacheModuleType.OPERATEUSERID，但是MyThreadVariable.getOperateUserId却为null ，请注意检查是否正确使用，调用的类:"+pjp.getTarget().getClass().getName()+",方法"+joinPointObject.getMethod().getName());
				}
				if(cacheModuleType==CacheModuleType.OPERATEPERIODID&&(MyThreadVariable.getOperatePeriodId()==null||MyThreadVariable.getOperatePeriodId()==0l)){
					MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();  
					LogUtils.getErrorLog().warn("缓存使用方式为CacheModuleType.OPERATEPERIODID，但是MyThreadVariable.getOperatePeriodId却为null，请注意检查是否正确使用，调用的类:"+pjp.getTarget().getClass().getName()+",方法"+joinPointObject.getMethod().getName());
				}
			}
			if(cacheModuleType==CacheModuleType.OPERATEUSERID&&StringUtil.isNotEmptyString(MyThreadVariable.getOperateUserId())){
				setUserEntiyMap(jedisTemplate, region, ent, cacheKey, MyThreadVariable.getOperateUserId());
			}else if(cacheModuleType==CacheModuleType.OPERATEPERIODID&&MyThreadVariable.getOperatePeriodId()!=null&&MyThreadVariable.getOperatePeriodId()!=0l){
				setPeriodEntiyMap(jedisTemplate, region, ent, cacheKey, MyThreadVariable.getOperatePeriodId());
			}else{
				//TODO 
			}
			setEntiyMap(jedisTemplate, region, ent, cacheKey);
		}
	}
	public static String getPojoKey(Class entiy,Object id){
		return entiy.getName()+"-"+id;
	}
	
	//TODO 优化,序列化效率大比拼
	private String getObjectsString(Object[] os){
		if(os!=null&&os.length!=0){
//			StringBuilder sb=new StringBuilder();
//			for(Object o:os){
//				sb.append(Base64.encodeToString(SerializationUtils.serialize(o)));
//			}
//			return sb.toString();
			return Base64.encodeToString(SerializationUtils.serialize(os));
		}
		return "";
	}
	/**获得entiy的映射key*/
	private static String getEntiyMapKey(String region,Class entiy){		
		return getKeyName(region,entiy.getName());
	}
	/**维护entiy的映射关系*/
	private void setEntiyMap(JedisTemplate jedisTemplate,String region,Class entiy,String key){
		if(entiy!=null){
			jedisTemplate.rpush(getEntiyMapKey(region,entiy), key);//TODO 采用list 会忽略值de重复
		}
	}
	/**清理entiy的映射关系*/
	private static void delEntiyMap(JedisTemplate jedisTemplate,String region,Class entiy){
		if(entiy!=null){
			jedisTemplate.del(getEntiyMapKey(region,entiy));
		}
	}
	/**查询实体对应的所有keys*/
	private static List<String> getKeysByEntiy(JedisTemplate jedisTemplate,String region,Class entiy){
		return jedisTemplate.lrangeAll(getEntiyMapKey(region,entiy));
	}
	
	/**获得UserEntiy的映射key*/
	public static String getUserEntiyMapKey(String region,String userId){
		return getKeyName(region,operateUserIdPrefix+userId);
		
	}
	public String getUserEntiyListKeys(String region,Class entiy,String userId){
		StringBuilder sb=new StringBuilder();
		sb.append(getEntiyMapKey(region,entiy)).append(":").append(operateUserIdPrefix).append(userId);
		return sb.toString();
	}
	/**维护UserEntiy的映射关系*/
	private void setUserEntiyMap(JedisTemplate jedisTemplate,String region,Class entiy,String key,String userId){
		if(entiy!=null){
			String sb=getUserEntiyListKeys(region,entiy,userId);
			jedisTemplate.rpush(sb,key);		
			jedisTemplate.hset(getUserEntiyMapKey(region,userId).getBytes(),entiy.getName().getBytes(),SerializationUtils.serialize(jedisTemplate.lrangeAll(sb)));
		}
	}
	/**清理UserEntiy的映射关系*/
	private void delUserEntiyMap(JedisTemplate jedisTemplate,String region,Class entiy,String userId){
		if(entiy!=null){
			jedisTemplate.hdel(getUserEntiyMapKey(region,userId).getBytes(),entiy.getName().getBytes());
			jedisTemplate.del(getUserEntiyListKeys(region,entiy,userId));
		}
	}
	/**通过用户id和实体查询对应的所有keys*/
	private List<String> getKeysByUserEntiy(JedisTemplate jedisTemplate,String region,Class entiy,String userId){
		 byte[] bytes = jedisTemplate.hget(getUserEntiyMapKey(region,userId).getBytes(), entiy.getName().getBytes());
		 Object deserialize = SerializationUtils.deserialize(bytes);
		 return (List<String>) deserialize;
	}

	/**获得PeriodEntiy的映射key*/
	public static String getPeriodEntiyMapKey(String region,Long periodId){
		return getKeyName(region,operatePeriodIdPrefix+periodId);
		
	}
	public String getPeriodEntiyListKeys(String region,Class entiy,Long periodId){
		StringBuilder sb=new StringBuilder();
		sb.append(getEntiyMapKey(region,entiy)).append(":").append(operatePeriodIdPrefix).append(periodId);
		return sb.toString();
	}
	/**维护PeriodEntiy的映射关系*/
	private void setPeriodEntiyMap(JedisTemplate jedisTemplate,String region,Class entiy,String key,Long periodId){
		if(entiy!=null){
			String sb=getPeriodEntiyListKeys(region,entiy,periodId);
			jedisTemplate.rpush(sb,key);
			jedisTemplate.hset(getPeriodEntiyMapKey(region,periodId).getBytes(),entiy.getName().getBytes(),SerializationUtils.serialize(jedisTemplate.lrangeAll(sb)));
		}
	}
	/**清理PeriodEntiy的映射关系*/
	private void delPeriodEntiyMap(JedisTemplate jedisTemplate,String region,Class entiy,Long periodId){
		if(entiy!=null){
			jedisTemplate.hdel(getPeriodEntiyMapKey(region,periodId).getBytes(),entiy.getName().getBytes());
			jedisTemplate.del(getPeriodEntiyListKeys(region,entiy,periodId));
		}
	}
	/**通过周期id和实体查询对应的所有keys*/
	private List<String> getKeysByPeriodEntiy(JedisTemplate jedisTemplate,String region,Class entiy,Long periodId){
		 byte[] bytes = jedisTemplate.hget(getPeriodEntiyMapKey(region,periodId).getBytes(), entiy.getName().getBytes());
		 Object deserialize = SerializationUtils.deserialize(bytes);
		 return (List<String>) deserialize;
	}
	private String getLockKey(String key){
		StringBuffer sb=new StringBuffer();
		return sb.append(lockKey).append(key).toString();
	}
	public static String getKeyName(String region,Object key) {

		if(key instanceof Number)
			return region + ":I:" + key;
		else{
			Class keyClass = key.getClass();
			if(String.class.equals(keyClass) || StringBuffer.class.equals(keyClass) || StringBuilder.class.equals(keyClass))
				return region + ":S:" + key;
		}
		return region + ":O:" + key;
	}
    private Object executeTemplate(String template, ProceedingJoinPoint joinPoint) {  
    	String methodLongName = joinPoint.getSignature().toLongString();  
        String[] parameterNames =  parameterNameCaches.get(methodLongName);  
        if(parameterNames == null) {  
			MethodSignature joinPointObject = (MethodSignature) joinPoint.getSignature();
			Method method =joinPointObject.getMethod();
            parameterNames = parameterNameDiscovere.getParameterNames(method);  
            parameterNameCaches.put(methodLongName, parameterNames);  
        }  
        // add args to expression context  
        StandardEvaluationContext context = new StandardEvaluationContext();  
        Object[] args = joinPoint.getArgs();  
        if(args.length == parameterNames.length) {  
            for(int i=0, len=args.length; i<len; i++)  
                context.setVariable(parameterNames[i], args[i]);  
        }  
        //cacha expression  
        Expression expression = spelExpressionCaches.get(template);  
        if(expression == null) { 
            expression = parser.parseExpression(template,parserContext);  
            spelExpressionCaches.put(template, expression);  
        }  
        Object value = expression.getValue(context);  
        return value;  
    }
	@Override
	public int getOrder() {
		return  LOWEST_PRECEDENCE;
	}
//	public static void main(String[] args) {
//		System.out.println(LOWEST_PRECEDENCE);
//		System.out.println(HIGHEST_PRECEDENCE);
//	}
}