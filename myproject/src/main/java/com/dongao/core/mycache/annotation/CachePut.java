package com.dongao.core.mycache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dongao.core.mycache.Expiry;
import com.dongao.core.mycomponent.CacheModuleType;

/**
 * @author zhangzhen
 * 
 * TODO 下一版本：考虑组合情况:在一个service有多步多个对象（有的是单纯的pojo,有的是自定义对象）缓存的情况
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CachePut {
	/**cache region 一般使用默认值即可*/
    String region() default "";
    /**缓存的实体对象，cache的基础维度，只有它是必填的*/
    Class entity();
    /**cache的第二维度，更细粒度的缓存控制,默认不启用*/
    CacheModuleType type() default CacheModuleType.NONE;
    /**是否单纯的pojo*/
    boolean isPojo() default true;
    /**cache清除时，要同时清除的关联实体,如果只是自身实体，留空即可*/
    Class[] relEntitys() default {};
    /**cache key 会覆盖默认key生成规则,default key:entity.getName()-method.getName()-Arrays.toString(params)*/
    /**将来会考虑支持书写spel表达式*/
    String key() default "";// cache key
    /**有效时长*/
    long expiry() default Expiry.ONE_DAY;
    
    String[] relPropertys() default {};//对应属性变动后，需要清除Me,还是建议类似经常更新的字段，通过程序去过滤，如status 
   
}
