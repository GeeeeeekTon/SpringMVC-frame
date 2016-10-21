package com.dongao.core.mycache;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 基于开源中国j2cache改造：目前只开启redis充当二级缓存
 * 
 * TODO 全面测试后，将开启两级缓存，且整合双缓存（主次）策略，解决缓存技术的面临的各种问题。
 * 
 * 扩展了并发支持、命中率监控、事物支持 
 * 
 * TODO 待扩展key自动过期支持
 * 
 * @author zhangzhen
 *
 */
public class CacheChannel implements CacheExpiredListener {
	private final static Logger log = LoggerFactory.getLogger(CacheChannel.class);
	public final static byte LEVEL_1 = 1;
	public final static byte LEVEL_2 = 2;
	
	private String name;
	private static CacheChannel instance;
	//扩展
	private static final String MonitorRbcDbCache="MonitorRbcDbCache";//no use TODO 支持集群的命中率监控
	private final AtomicLong hitCount = new AtomicLong();//TODO 通过jmx 暴露出去 暂时也可以记录到日志先 监控本集群中单个app请求 的缓存命中率
	private final AtomicLong missCount = new AtomicLong();
	private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
	private final Lock readLock = cacheLock.readLock();
	private final Lock writeLock = cacheLock.writeLock();
	private final boolean isSynchronized=false;
	
	/**
	 * 单例方法
	 * @return
	 */
	public final static CacheChannel getInstance(){
		if(instance == null){
			synchronized(CacheChannel.class){
				if(instance == null){
					instance = new CacheChannel("default");
				}
			}
		}
		return instance;
	}
	/**
	 * 初始化缓存通道并连接
	 * @param name
	 * @throws CacheException
	 */
	private CacheChannel(String name) throws CacheException {
		this.name = name;
		try{
			CacheManager.initCacheProvider(this);
			
			long ct = System.currentTimeMillis();
			
			log.info("Connected to channel:" + this.name + ", time " + (System.currentTimeMillis()-ct) + " ms.");

		}catch(Exception e){
			throw new CacheException(e);
		}
	}
	/**
	 * 获取缓存中的数据
	 * @param <T>
	 * @param level
	 * @param resultClass
	 * @param region
	 * @param key
	 * @return
	 */
	public CacheObject get(String region, Object key){
		if(isSynchronized){
			readLock.lock();
		}
		try {
			log.debug("region:{}, get key:{}", region, key);
			CacheObject obj = new CacheObject();
			obj.setRegion(region);
			obj.setKey(key);
	        if(region!=null && key != null){
	//        	obj.setValue(CacheManager.get(LEVEL_1, region, key));
	//            if(obj.getValue() == null) {
	//            	obj.setValue(CacheManager.get(LEVEL_2, region, key));
	//                if(obj.getValue() != null){
	//                	obj.setLevel(LEVEL_2);
	//                    CacheManager.set(LEVEL_1, region, key, obj.getValue());
	//                }
	//            }
	//            else
	//            	obj.setLevel(LEVEL_1);
	        	obj.setValue(CacheManager.get(LEVEL_2, region, key));
	        	if(obj.getValue() != null){//hit
	        		hitCount.incrementAndGet();
	        		obj.setLevel(LEVEL_2);
	        	}else{//miss
	        		missCount.incrementAndGet();
	        	}
	        }
	        return obj;
		}
		finally {
			if(isSynchronized){
				readLock.unlock();
			}
		}
	}
	
	/**
	 * 写入缓存
	 * @param level
	 * @param region
	 * @param key
	 * @param value
	 */
	public void set(final String region, final Object key, final Object value){
		if(isSynchronized){
			writeLock.lock();
		}
		try {
			//log.debug("region:{}, put key:{},value:{}", region,key,value);
			if(region!=null && key != null){
				if(value == null){//TODO　准备优化这里
					if (TransactionSynchronizationManager.isSynchronizationActive()) {
						TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
							@Override
							public void afterCommit() {
								evict(region, key);//TODO
							}
						});
					}else{
						evict(region, key);//TODO
					}
				}else{
					//分几种情况
					//Object obj1 = CacheManager.get(LEVEL_1, region, key);
					//Object obj2 = CacheManager.get(LEVEL_2, region, key);
					//1. L1 和 L2 都没有
					//2. L1 有 L2 没有（这种情况不存在，除非是写 L2 的时候失败
					//3. L1 没有，L2 有
					//4. L1 和 L2 都有
					//_sendEvictCmd(region, key);//清除原有的一级缓存的内容
					//CacheManager.set(LEVEL_1, region, key, value);
					if (TransactionSynchronizationManager.isSynchronizationActive()) {
						TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
							@Override
							public void afterCommit() {
								CacheManager.set(LEVEL_2, region, key, value);
							}
						});
					}else{
						CacheManager.set(LEVEL_2, region, key, value);
					}
					
				}
			}
		}
		finally {
			if(isSynchronized){
				writeLock.unlock();
			}
		}
		//log.info("write data to cache region="+region+",key="+key+",value="+value);
	}
	
	/**
	 * 删除缓存
	 * @param region
	 * @param key
	 */
	public void evict(final String region, final Object key,boolean transactionSupport) {
		if(isSynchronized){
			writeLock.lock();
		}
		try {
		  log.debug("region:{}, evict key:{}", region, key);
		//CacheManager.evict(LEVEL_1, region, key); //删除一级缓存
			if (transactionSupport&&TransactionSynchronizationManager.isSynchronizationActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						CacheManager.evict(LEVEL_2, region, key); //删除二级缓存
					}
				});
			}else{
				CacheManager.evict(LEVEL_2, region, key); //删除二级缓存
			}
		//_sendEvictCmd(region, key); //发送广播
		}
		finally {
			if(isSynchronized){
				writeLock.unlock();
			}
		}
	}
	public void evict(final String region, final Object key) {
		evict(region,key,false);
	}
	/**
	 * 批量删除缓存
	 * @param region
	 * @param keys
	 */
	@SuppressWarnings({ "rawtypes" })
	public void batchEvict(final String region, final List keys,boolean transactionSupport) {
		if(isSynchronized){
			writeLock.lock();
		}
		try {
			log.debug("region:{}, evict key:{}", region, keys);
			if (transactionSupport&&TransactionSynchronizationManager.isSynchronizationActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						CacheManager.batchEvict(LEVEL_2, region, keys);
					}
				});
			}else{
				//CacheManager.batchEvict(LEVEL_1, region, keys);
				CacheManager.batchEvict(LEVEL_2, region, keys);
				//_sendEvictCmd(region, keys);
			}
		}
		finally {
			if(isSynchronized){
				writeLock.unlock();
			}
		}
	}
	public void batchEvict(final String region, final List keys) {
		batchEvict(region,keys,false);
	}
	/**
	 * Clear the cache
	 */
	public void clear(final String region,boolean transactionSupport) throws CacheException {
		//CacheManager.clear(LEVEL_1, region);
		if(isSynchronized){
			writeLock.lock();
		}
		try {
			log.debug("region:{}, has clear", region);
			//if (TransactionSynchronizationManager.isSynchronizationActive()) {
			if(transactionSupport&&TransactionSynchronizationManager.isSynchronizationActive()){
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						CacheManager.clear(LEVEL_2, region);
					}
				});
			}else{
				CacheManager.clear(LEVEL_2, region);
			}
		}
		finally {
			if(isSynchronized){
				writeLock.unlock();
			}
		}
	}
	public void clear(final String region) throws CacheException {
		clear(region,false);
	}
	@SuppressWarnings("rawtypes")
	public List keys(String region) throws CacheException {
		if(isSynchronized){
			readLock.lock();
		}
		try {
			log.debug("region:{}, list keys", region);
			return CacheManager.keys(LEVEL_2, region);
		}
		finally {
			if(isSynchronized){
				readLock.unlock();
			}
		}
	}
	/**
	 * 删除一级缓存的键对应内容
	 * @param region
	 * @param key
	 */
//	@SuppressWarnings("rawtypes")
//	protected void onDeleteCacheKey(String region, Object key){
//		if(key instanceof List)
//			CacheManager.batchEvict(LEVEL_1, region, (List)key);
//		else
//			CacheManager.evict(LEVEL_1, region, key);
//		log.debug("Received cache evict message, region="+region+",key="+key);
//	}
	/**
	 * 关闭到通道的连接
	 */
	public void close(){
		//CacheManager.shutdown(LEVEL_1);
		CacheManager.shutdown(LEVEL_2);
		//channel.close();
	}
	@Override
	public void notifyElementExpired(String region, Object key) {
		//TODO
	}
	public long getHitCount() {
		return hitCount.get();
	}
	public long getMissCount() {
		return missCount.get();
	}
	
	
}