package com.dongao.core.mycomponent.benchmark;

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dongao.core.mycache.CacheChannel;
import com.dongao.core.mycache.CacheObject;
import com.dongao.core.myutil.JedisTemplate;
import com.dongao.core.myutil.LogUtils;

/**
 * 
 * 可用系统参数重置变量改变测试规模与连接参数， @see RedisCounterBenchmark
 * 
 * @author zhangzhen
 */
public class RedisSetGetBenchmark extends ConcurrentBenchmark {
	public static final int DEFAULT_THREAD_COUNT = 10;
	public static final long DEFAULT_TOTAL_COUNT = 1000000;
	private String keyPrefix = "dongaoacc.redistest:";
	private String region = "dongaoacc.redistest:";
	private CacheChannel cache=CacheChannel.getInstance();
//	public static void main(String[] args) throws Exception {
//		RedisSetGetBenchmark benchmark = new RedisSetGetBenchmark();
//		benchmark.execute();
//	}
	public RedisSetGetBenchmark() {
		super(DEFAULT_THREAD_COUNT, DEFAULT_TOTAL_COUNT,LogUtils.getEmailLog());
	}
	public RedisSetGetBenchmark(int defaultThreadCount, long defaultTotalCount) {
		super(defaultThreadCount, defaultTotalCount,LogUtils.getEmailLog());
	}
	@Override
	protected void setUp() {
	}

	@Override
	protected void tearDown() {
		//jedisTemplate.getJedisPool().destroy();
	}

	@Override
	protected BenchmarkTask createTask() {
		return new RedisTask();
	}

	public class RedisTask extends BenchmarkTask {
		private SecureRandom random = new SecureRandom();
		@Override
		protected void execute(final int requestSequnce) {
			int randomIndex = random.nextInt((int) loopCount);
			final String key = new StringBuilder().append(keyPrefix).append(taskSequence).append(":")
					.append(randomIndex).toString();
			cache.set(region, key, key);
			cache.get(region, key);
		}
	}

	@Override
	protected String getBenchmarkName() {
		return "redisBenchmark";
	}
}
