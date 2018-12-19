package com.leo.core.mydatasource;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.NestedRuntimeException;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.ReflectionUtils;

import com.leo.core.mybase.MySwitch;

/**
 * 
 * 
 * <pre>
 * 
 * 此类实现了两个职责（为了减少类的数量将两个功能合并到一起了）：
 *   读/写动态数据库选择处理器
 *   通过AOP切面实现读/写选择
 *   
 *   
 * ★★读/写动态数据库选择处理器★★
 * 1、首先读取<tx:advice>事务属性配置
 * 
 * 2、对于所有读方法设置 read-only="true" 表示读取操作（以此来判断是选择读还是写库），其他操作都是走写库
 *    如<tx:method name="×××" read-only="true"/>
 *    
 * 3、 forceChoiceReadOnWrite用于确定在如果目前是写（即开启了事务），下一步如果是读，
 *    是直接参与到写库进行读，还是强制从读库读<br/>
 *      forceChoiceReadOnWrite:true 表示目前是写，下一步如果是读，强制参与到写事务（即从写库读）
 *                                  这样可以避免写的时候从读库读不到数据
 *                                  
 *                                  通过设置事务传播行为：SUPPORTS实现
 *                                  
 *      forceChoiceReadOnWrite:false 表示不管当前事务是写/读，都强制从读库获取数据
 *                                  通过设置事务传播行为：NOT_SUPPORTS实现（连接是尽快释放）                
 *                                  『此处借助了 NOT_SUPPORTS会挂起之前的事务进行操作 然后再恢复之前事务完成的』
 * 4、配置方式
 *  <bean id="readWriteDataSourceTransactionProcessor" class="com.leo.core.mydatasource.ReadWriteDataSourceProcessor">
 *      <property name="forceChoiceReadWhenWrite" value="false"/>
 *  </bean>
 *
 * 5、目前只适用于<tx:advice>情况 TODO 支持@Transactional注解事务
 *  
 *  
 *  
 * ★★通过AOP切面实现读/写库选择★★
 * 
 * 1、首先将当前方法 与 根据之前【读/写动态数据库选择处理器】  提取的读库方法 进行匹配
 * 
 * 2、如果匹配，说明是读取数据：
 *  2.1、如果forceChoiceReadOnWrite:true，即强制走读库
 *  2.2、如果之前是写操作且forceChoiceReadOnWrite:false，将从写库进行读取
 *  2.3、否则，到读库进行读取数据
 * 
 * 3、如果不匹配，说明默认将使用写库进行操作
 * 
 * 4、配置方式
 *      <aop:aspect order="-2147483648" ref="readWriteDataSourceTransactionProcessor">
 *          <aop:around pointcut-ref="txPointcut" method="determineReadOrWriteDB"/>
 *      </aop:aspect>
 *  4.1、此处order = Integer.MIN_VALUE 即最高的优先级（请参考http://jinnianshilongnian.iteye.com/blog/1423489）
 *  4.2、切入点：txPointcut 和 实施事务的切入点一样
 *  4.3、determineReadOrWriteDB方法用于决策是走读/写库的，请参考
 *       @see com.leo.core.mydatasource.ReadWriteDataSourceDecision
 *       @see com.leo.core.mydatasource.ReadWriteDataSource
 * 
 * </pre>
 * @author zhangzhen
 * 
 * edit by zhangpj
 * 	调整内容： 
 * 			1、将之前通过事务中配置的方法来判断是读从库还是写主库操作改为使用配置的方式
 * 			2、由于停用事务，所以也没有强制走读库或者强制走主库的区别了，如果用户想走从库，方法名遵守配置的规则即可。如果想走主库，不使用配置的规则就会默认走主库。 
 *
 */
public class ReadWriteDataSourceProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ReadWriteDataSourceProcessor.class);
    
    private List<String> readMethodPrefixList = new ArrayList<String>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
    public Object determineReadOrWriteDB(ProceedingJoinPoint pjp) throws Throwable {
        if (MySwitch.readWriteEnable&&isChoiceReadDB(pjp.getSignature().getName())) {
            ReadWriteDataSourceDecision.markRead();
            logger.debug(pjp.getTarget().getClass().getName()+"."+pjp.getSignature().getName() + " use slave DB.");
        } else {
            ReadWriteDataSourceDecision.markWrite();
            logger.debug(pjp.getTarget().getClass().getName()+"."+pjp.getSignature().getName() + " use master DB.");
        }
            
        try {
            return pjp.proceed();
        } finally {
            ReadWriteDataSourceDecision.reset();
        }
    }
    
    private boolean isChoiceReadDB(String methodName) {
    	if(null != readMethodPrefixList && readMethodPrefixList.size() > 0){
    		for (String mappedName : readMethodPrefixList) {
    			if (methodName.startsWith(mappedName)) {
    				return true;
    			}
    		}
    	}
        //默认选择 写库
        return false;
    }

	public List<String> getReadMethodPrefixList() {
		return readMethodPrefixList;
	}

	public void setReadMethodPrefixList(List<String> readMethodPrefixList) {
		this.readMethodPrefixList = readMethodPrefixList;
	}
    
}

