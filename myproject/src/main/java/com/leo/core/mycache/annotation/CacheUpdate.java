package com.leo.core.mycache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangzhen
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheUpdate {
//	Class[] entitys();//考虑到组合情况，一个service 涉及到多个类的修改,所以定义为数组类型，一般情况只会有一个
//	String[] ids() default{};//主键值，支持批量操作
	String region() default "";//一般不用定义此值，使用默认即可
	Class entity();
	String id();//主键值，支持spel,eg:${#area.id}
	Class idType() default Long.class;//主键类型，为了照顾其他系统的String类型的id
	boolean transactionSupport() default false;//是否启用事物支持，启用事物支持后，可以提高性能，避免不必要的缓存刷新，默认为false
	Class[] updatePropertys() default {};//更新的属性
}
