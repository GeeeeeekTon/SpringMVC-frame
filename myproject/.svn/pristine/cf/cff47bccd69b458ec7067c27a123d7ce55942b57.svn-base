package com.dongao.core.mycache;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dongao.core.base.pojo.PageParameter;
import com.dongao.core.common.utils.ReflectUtil;
import com.dongao.core.common.utils.tedis.DefaultGenCacheKey;
import com.dongao.core.common.utils.tedis.GenCacheKeyService;

public class MyGenCacheKey extends DefaultGenCacheKey implements GenCacheKeyService{
	/**查询单个对象规范**/
	private List<String> singleQueryIndexs; 
	/**多个单个对象规范**/
	private List<String> multipleQueryIndexs;
	/**
	 * 生成redis 缓存key
	 * @param targetName
	 * @param methodName
	 * @param arguments
	 * @return
	 */
	public String getRedisCacheKey(String targetName, String methodName, Object[] arguments) {
		StringBuffer cacheKey = new StringBuffer();
		String simplePackageName = processSimplePackageName(targetName); // 处理成简洁包名称，缩短redis缓存key的长度
		cacheKey.append(simplePackageName).append(".").append(methodName);
		
		// 如果查询方法为单个查询方法
		if(singleQuery(methodName)){
			if ((arguments != null) && (arguments.length != 0)) {
				for (int i = 0; i < arguments.length; i++) {
					cacheKey.append(".").append(arguments[i]);
				}
			}
		}
		// 如果查询方法为多个查询，查询list集合
		else if(multipleQuery(methodName)){
			if ((arguments != null) && (arguments.length != 0)) {
				for (int i = 0; i < arguments.length; i++) {
					// 查询集合的方法参数都为参数对象(mybatis接收的也是参数对象)，通过反射获得对象所有信息记录为key
					if(arguments[i] != null){
					    // 如果参数是MAP
						if(arguments[i] instanceof Map<?, ?>){
						    appendMapKey(arguments[i], cacheKey);
						}
						// 如果是基本类型
						else if(arguments[i].getClass().isPrimitive()){
						    cacheKey.append(".").append(arguments[i]);
						}
						// 如果参数本身是查询的对象
						else{
						    appendAllParam(cacheKey, arguments[i]);
						}
					}
				}
			}
		}
		
		return cacheKey.toString();
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
                cacheKey.append(".").append(pageParameter.getCurrentPage()).append(".").append(pageParameter.getPageSize()); 
	        }else{
	            String value = entry.getValue().toString();
	            cacheKey.append(".").append(value);
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
			// 按照编程规范，统一包命名规则(com.dongao.sysName.modelName)截取简洁包名称，返回结果modelName.service.ModelServiceImpl
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
		Field[] fields = ReflectUtil.getAllFields(target); // 获得该类所有的属性
		reflectAppendParam(cacheKey, fields, target); // 追加参数列表
		
		Field[] parentFields = ReflectUtil.getAllParentFields(target); // 获得该类父类所有的属性
		reflectAppendParentParam(cacheKey, parentFields, target); // 追加参数列表
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
			    if(!field.getName().equals("serialVersionUID")){
    				Object obj = ReflectUtil.getFieldValue(target, field.getName());
    				if(obj != null){
    					if(obj instanceof PageParameter){
    						PageParameter pageParameter = (PageParameter) obj;
    						cacheKey.append(".").append(pageParameter.getCurrentPage()).append(".").append(pageParameter.getPageSize()); 
    					}else{
    						cacheKey.append(".").append(obj.toString());
    					}
    				}
			    }
			}
		}
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
			    if(!field.getName().equals("serialVersionUID")){
    				Object obj = ReflectUtil.getParentFieldValue(target, field.getName());
    				if(obj != null){
    					if(obj instanceof PageParameter){
    						PageParameter pageParameter = (PageParameter) obj;
    						cacheKey.append(".").append(pageParameter.getCurrentPage()).append(".").append(pageParameter.getPageSize()); 
    					}else{
    						cacheKey.append(".").append(obj.toString());
    					}
    				}
			    }
			}
		}
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
