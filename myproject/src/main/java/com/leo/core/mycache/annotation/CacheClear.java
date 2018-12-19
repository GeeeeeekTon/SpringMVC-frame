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
public @interface CacheClear {//or
    String[] regions() default {}; //regions 一般不用定义此值，使用默认即可
    
    String region() default "";//一般不用定义此值，使用默认即可
    
    String[] keys() default {};//key
    
    Class[] entitys();//考虑到组合情况，一个service 涉及到多个类的修改,所以定义为数组类型，一般情况只会有一个
    
    boolean transactionSupport() default false;//是否启用事物支持，启用事物支持后，可以提高性能，避免不必要的缓存刷新，默认为false
    
    boolean methodBefore() default true;//是否在方法执行之前清除缓存，默认为true，为false的情况，方法体中如有其它查询操作，会更加充分利用缓存，提高性能，根据实际业务情况灵活设置
    
    //String id();//主键值，支持spel,eg:${#area.id} 启用优化
}
