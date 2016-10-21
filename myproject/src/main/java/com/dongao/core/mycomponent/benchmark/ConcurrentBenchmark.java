package com.dongao.core.mycomponent.benchmark;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dongao.core.mybase.MySwitch;
import com.dongao.core.myutil.LogUtils;


/**
 * 多线程Benchmark测试框架. 提供多线程调度及定时的测试进度, tps/latency信息打印.
 * 为方便移植重用, 无任何第三方类库依赖.
 * 
 * 用户可以用benchmark.thread.count 与 benchmark.loop.count 重新定义并发线程数与循环次数。
 * 
 * @author zhangzhen
 */
public abstract class ConcurrentBenchmark {

	public static final String THREAD_COUNT_NAME = "thread.count";
	public static final String TOTAL_COUNT_NAME = "total.count";

	public final int threadCount;
	public final long loopCount;
	public final long totalCount;

	public CountDownLatch startLock;
	public CountDownLatch finishLock;

	public int intervalSeconds = 10;
	public Date startTime;
	private AtomicLong count = new AtomicLong(0);
	private long totalInvokedCount = 0L;
	private ScheduledExecutorService reportExecutor;
	private Logger logger=LoggerFactory.getLogger(ConcurrentBenchmark.class);
	

	public ConcurrentBenchmark(int defaultThreadCount, long defaultTotalCount,Logger logger) {
		// merge default setting and system properties
		this.threadCount = Integer.parseInt(System.getProperty(THREAD_COUNT_NAME, String.valueOf(defaultThreadCount)));
		this.totalCount = Long.parseLong(System.getProperty(TOTAL_COUNT_NAME, String.valueOf(defaultTotalCount)));
		this.loopCount = totalCount / threadCount;

		startLock = new CountDownLatch(threadCount);
		finishLock = new CountDownLatch(threadCount);

		reportExecutor = Executors.newSingleThreadScheduledExecutor();
		this.logger=logger;
	}

	public void execute() throws Exception {
		// override for connection & data setup
		setUp();

		// start threads
		ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
		try {
			for (int i = 0; i < threadCount; i++) {
				BenchmarkTask task = createTask();
				task.taskSequence = i;
				task.parent = this;
				threadPool.execute(task);
			}

			// wait for all threads ready
			startLock.await();

			// print start message and start reporter
			startReporter();
			startTime = new Date();
			printStartMessage();

			// wait for all threads finish
			finishLock.await();

			// print finish summary message
			printFinishMessage();
		} finally {
			threadPool.shutdownNow();
			reportExecutor.shutdownNow();
			// override for connection & data cleanup
			tearDown();
		}
	}

	private void startReporter() {
		reportExecutor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					printProgressMessage();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
	}

	protected void printProgressMessage() {
		long currentCount = count.getAndSet(0);
		totalInvokedCount += currentCount;
		long currentTps = currentCount / intervalSeconds;

		long totalTimeMillis = System.currentTimeMillis() - startTime.getTime();
		long totalTps = (totalInvokedCount * 1000) / totalTimeMillis;
		Formatter formatter=new Formatter();
		formatter.format("Current perior is %,d , tps is %,d, Total is %,d , tps is %,d.%n", currentCount, currentTps,
				totalInvokedCount, totalTps);
		logger.info(formatter.toString());
	}

	protected void printStartMessage() {
		String className = this.getClass().getSimpleName();
		Formatter formatter=new Formatter();
		formatter.format("%s started at %s.%n%d threads with %,d loops, totally %,d requests will be invoked.%n",
				className, startTime.toString(), threadCount, loopCount, totalCount);
		logger.info("***************************{} {} benchmark start******************************************",MySwitch.getAppInfo(),getBenchmarkName());
		logger.info(formatter.toString());
	}

	protected void printFinishMessage() {
		Date endTime = new Date();
		String className = this.getClass().getSimpleName();
		long totalTimeMillis = endTime.getTime() - startTime.getTime();
		long totalTps = (totalCount * 1000) / totalTimeMillis;
		//延迟
		BigDecimal totalLatency = new BigDecimal(totalTimeMillis * threadCount).divide(new BigDecimal(totalCount), 2,
				BigDecimal.ROUND_HALF_UP);
		Formatter formatter=new Formatter();
		formatter.format(
				"%s finished at %s.%n%d threads processed %,d requests after %,d ms, total tps/latency is %,d/%sms.%n",
				className, endTime.toString(), threadCount, totalCount, totalTimeMillis, totalTps,
				totalLatency.toString());
		logger.info(formatter.toString());
		logger.info(LogUtils.getNotifyAdminMarker(),"**************************{} {} benchmark end******************************************",MySwitch.getAppInfo(),getBenchmarkName());
	}

	protected void incCounter() {
		count.incrementAndGet();
	}

	protected void setIntervalSeconds(int intervalSeconds) {
		this.intervalSeconds = intervalSeconds;
	}

	/**
	 * Override for connection & data setup.
	 */
	protected void setUp() {
	}

	/**
	 * Override to connection & data cleanup.
	 */
	protected void tearDown() {
	}

	/**
	 * create a new benchmark task.
	 */
	protected abstract BenchmarkTask createTask();
	
	protected abstract String getBenchmarkName();
}
