package com.leo.core.common.utils.tedis;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leo.core.base.pojo.PageParameter;
import com.leo.core.common.utils.ReflectUtil;
import com.leo.core.common.utils.SsistUtils;

/**
 * 默认生成redis cache key 类,按照编程规范，统一包命名规则(com.leo.sysName.modelName)截取简洁包名称，
 * 返回结果modelName.service.ModelServiceImpl
 * 
 * @date 2013-11-1 上午11:34:55
 * @author leo
 * @version 1
 */
public class DefaultGenCacheKey implements GenCacheKeyService{
	
	private static final Logger logger = LoggerFactory .getLogger(DefaultGenCacheKey.class);
	
	private String singleQueryIndex; //查询单个对象规范
	private String multipleQueryIndex;

	/**
	 * 生成redis 缓存key
	 * @param targetName
	 * @param methodName
	 * @param arguments
	 * @return
	 */
	public String getRedisCacheKey(Class<?> obj, String methodName, Object[] arguments) {
		//计时使用
		long startTime = System.currentTimeMillis();
		StringBuffer cacheKey = new StringBuffer();
		String simplePackageName = processSimplePackageName(obj.getName()); // 处理成简洁包名称，缩短redis缓存key的长度
		cacheKey.append(simplePackageName).append(".").append(methodName);
		
//		// 如果查询方法为单个查询方法
//		if(methodName.startsWith(singleQueryIndex)){
//			if ((arguments != null) && (arguments.length != 0)) {
//				for (int i = 0; i < arguments.length; i++) {
//					cacheKey.append(".").append(arguments[i]);
//				}
//			}
//		}
//		// 如果查询方法为多个查询，查询list集合
//		else if(methodName.startsWith(multipleQueryIndex)){
//			if ((arguments != null) && (arguments.length != 0)) {
//				for (int i = 0; i < arguments.length; i++) {
//					// 查询集合的方法参数都为参数对象(mybatis接收的也是参数对象)，通过反射获得对象所有信息记录为key
//					if(arguments[i] != null){
//					    // 如果参数是MAP
//						if(arguments[i] instanceof Map<?, ?>){
//						    appendMapKey(arguments[i], cacheKey);
//						}
//						// 如果是基本类型
//						else if(arguments[i].getClass().isPrimitive()){
//						    cacheKey.append(".").append(arguments[i]);
//						}
//						// 如果参数本身是查询的对象
//						else{
//						    appendAllParam(cacheKey, arguments[i]);
//						}
//					}
//				}
//			}
//		}
		if ((arguments != null) && (arguments.length != 0)) {
			// 调用Ssist工具类，获得参数名称数组
			String [] paramNames = SsistUtils.getParamNames(obj, methodName);
			
			for (int i = 0; i < arguments.length; i++) {
				deepAppendParam(cacheKey, arguments[i], paramNames[i]);
			}
		}
		long endTime = System.currentTimeMillis();
		logger.debug("cache key gen time cost :"+(endTime-startTime)+"ms");
		return cacheKey.toString();
	}
	/**
	 * 根据参数对象深度追加key
	 * @param cacheKey
	 * @param param
	 * @paramName
	 */
	@SuppressWarnings("unchecked")
	protected void deepAppendParam(StringBuffer cacheKey,Object param, String paramName){
		if(param != null){
			if(param instanceof Map<?, ?>){
			    appendMapKey(param, cacheKey);
			}
			// 如果是基本类型
			else if(param.getClass().isPrimitive()){
			    cacheKey.append(".").append(param).append(paramName.isEmpty() ? "" : "#" + paramName);
			}
			//基本类型包装类
			else if(isPrimitiveWarp(param)){
				cacheKey.append(".").append(param).append(paramName.isEmpty() ? "" : "#" + paramName);
			}
			//如果是数组
			else if(param.getClass().isArray()){
				appendArrayKey(cacheKey, param);
				cacheKey.append("#").append(paramName);
			}
			//集合类
			else if(param instanceof Collection){
				Collection c = (Collection)param;
				for(Iterator it = c.iterator();it.hasNext();){
					deepAppendParam(cacheKey, it.next(), StringUtils.EMPTY);
				}
				if(c != null && c.size() >0){
					cacheKey.append("#").append(paramName);
				}
			}
			else if(param instanceof PageParameter){
				PageParameter pageParameter = (PageParameter) param;
				cacheKey.append(".").append(pageParameter.getCurrentPage()).append("#currentPage").append(".").
					append(pageParameter.getPageSize()).append("#pageSize"); 
			}
			// 如果参数本身是查询的对象
			else{
			    appendAllParam(cacheKey, param);
			}
		}
	}
	
	protected boolean isPrimitiveWarp(Object obj){
		return obj instanceof String
			|| obj instanceof Character
			|| obj instanceof Boolean
			|| obj instanceof Byte
			|| obj instanceof Short
			|| obj instanceof Integer
			|| obj instanceof Long
			|| obj instanceof Float
			|| obj instanceof Double
			;
	}
	
	/**
	 * 参数类型为map，解析map中的每个参数拼接CacheKey
	 * @param argument
	 * @param cacheKey
	 */
	@SuppressWarnings("unchecked")
	private void appendMapKey(Object argument, StringBuffer cacheKey){
	    Map<String, Object> param = (Map<String, Object>) argument; 
	    for(Map.Entry<String, Object> entry : param.entrySet()){
	        // 如果Map中参数中为分页的参数
	        if(entry.getValue() instanceof PageParameter){
	            PageParameter pageParameter = (PageParameter) entry.getValue();
                cacheKey.append(".").append(pageParameter.getCurrentPage()).append("#currentPage.").append(pageParameter.getPageSize()).append("#pageSize"); 
	        }else{
	            String value = entry!=null && entry.getValue()!=null?entry.getValue().toString():"";
	            String keyName = entry!=null && entry.getKey()!=null?entry.getKey().toLowerCase():"";
	            cacheKey.append(".").append(value).append("#").append(keyName);
	        }
	    }
	}
	
	protected void appendArrayKey(StringBuffer cacheKey,Object obj){
		if(obj instanceof int[]){
			int [] objs = (int [])obj;
			for(Object o:objs){
				deepAppendParam(cacheKey, o, StringUtils.EMPTY);
			}
		}
		else if(obj instanceof long[]){
			long [] objs = (long [])obj;
			for(Object o:objs){
				deepAppendParam(cacheKey, o, StringUtils.EMPTY);
			}
		}
		else if(obj instanceof boolean[]){
			boolean [] objs = (boolean [])obj;
			for(Object o:objs){
				deepAppendParam(cacheKey, o, StringUtils.EMPTY);
			}
		}
		else if(obj instanceof char[]){
			char [] objs = (char [])obj;
			for(Object o:objs){
				deepAppendParam(cacheKey, o, StringUtils.EMPTY);
			}
		}
		else if(obj instanceof byte[]){
			byte [] objs = (byte [])obj;
			for(Object o:objs){
				deepAppendParam(cacheKey, o, StringUtils.EMPTY);
			}
		}
		else if(obj instanceof short[]){
			short [] objs = (short [])obj;
			for(Object o:objs){
				deepAppendParam(cacheKey, o, StringUtils.EMPTY);
			}
		}
		else if(obj instanceof float[]){
			float [] objs = (float [])obj;
			for(Object o:objs){
				deepAppendParam(cacheKey, o, StringUtils.EMPTY);
			}
		}
		else if(obj instanceof double[]){
			double [] objs = (double [])obj;
			for(Object o:objs){
				deepAppendParam(cacheKey, o, StringUtils.EMPTY);
			}
		}
		else{
			Object [] objs = (Object[])obj;
			for(Object o:objs){
				deepAppendParam(cacheKey, o, StringUtils.EMPTY);
			}
		}
	}
	
	

	/**
	 * 处理成简洁包名称
	 * @param targetName
	 * @return
	 */
	private String processSimplePackageName(String targetName) {
		if (StringUtils.isNotEmpty(targetName)) {
			// 按照编程规范，统一包命名规则(com.leo.sysName.modelName)截取简洁包名称，返回结果modelName.service.ModelServiceImpl
			return targetName.substring(targetName.indexOf(".", targetName.indexOf(".") + 1) + 1, targetName.length());
		}

		return null;
	}
	
	/**
	 * 追加所有参数列表
	 * @param cacheKey
	 * @param target
	 */
	public void appendAllParam(StringBuffer cacheKey, Object target){
		Field[] fields =  getFields(target);// 获得该类所有的属性
		reflectAppendParam(cacheKey, fields, target); // 追加参数列表
		
		Field[] parentFields = getParentFields(target); // 获得该类父类所有的属性
		reflectAppendParentParam(cacheKey, parentFields, target); // 追加参数列表
	}
	
	protected Field[] getFields(Object target){
		return ReflectUtil.getAllFields(target);
	}
	
	protected Field[] getParentFields(Object target){
		return ReflectUtil.getAllParentFields(target);
	}
	
	/**
	 * 通过反射追加参数列表
	 * @param cacheKey
	 * @param fields
	 * @param target
	 */
	private void reflectAppendParam(StringBuffer cacheKey, Field [] fields, Object target){
		if(fields != null && fields.length > 0){
			for(Field field : fields){
			    if(field.getName() != "serialVersionUID"){
    				Object obj = getFieldValue(target, field);
    				deepAppendParam(cacheKey, obj, field.getName());
			    }
			}
		}
	}
	
	private Object getFieldValue(Object target,Field field){
		return ReflectUtil.getFieldValue(target, field.getName());
	}
	private Object getParentFieldValue(Object target,Field field){
		return ReflectUtil.getParentFieldValue(target, field.getName());
	}
	
	/**
	 * 通过反射追加父类参数列表
	 * @param cacheKey
	 * @param fields
	 * @param target
	 */
	private void reflectAppendParentParam(StringBuffer cacheKey, Field [] fields, Object target){
		if(fields != null && fields.length > 0){
			for(Field field : fields){
			    if(field.getName() != "serialVersionUID"){
    				Object obj = getParentFieldValue(target, field);
    				deepAppendParam(cacheKey, obj, field.getName());
			    }
			}
		}
	}

	public void setSingleQueryIndex(String singleQueryIndex) {
		this.singleQueryIndex = singleQueryIndex;
	}

	public void setMultipleQueryIndex(String multipleQueryIndex) {
		this.multipleQueryIndex = multipleQueryIndex;
	}
	
}
