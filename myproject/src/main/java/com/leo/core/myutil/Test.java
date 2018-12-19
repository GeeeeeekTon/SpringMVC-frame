package com.leo.core.myutil;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

//场景：多线程并发转账
public class Test {
	private final int[] account; // 账户数组，其索引为账户ID，内容为金额

	public Test(int count, int money) {
		account = new int[count];
		Arrays.fill(account, money);
	}

	boolean transfer(int from, int to, int money) {
		if (account[from] < money)
			return false;
		account[from] -= money;
		try {
			Thread.sleep(2);
		} catch (Exception e) {
		}
		account[to] += money;
		return true;
	}
	
	int getAmount() {
		int result = 0;
		for (int m : account)
			result += m;
		return result;
	}

	public static void main(String[] args) throws Exception {
//		int count = 10000000;		//账户个数
//		int money = 10000000;		//账户初始金额
//		int threadNum = 1000;		//转账线程数
//		int number = 10000000;		//转账次数
//		int maxMoney = 1000;	//随机转账最大金额
		
        int count = 100;        //账户个数  
        int money = 10000;      //账户初始金额  
        int threadNum = 8;      //转账线程数  
        int number = 10000;     //转账次数  
        int maxMoney = 1000;    //随机转账最大金额  
		Test test = new Test(count, money);
		
		//不加锁
//		Runner runner = test.new NonLockRunner(maxMoney, number);
		//加synchronized锁
//		Runner runner = test.new SynchronizedRunner(maxMoney, number);
		//加ReentrantLock锁
//		Runner runner = test.new ReentrantLockRunner(maxMoney, number);
		//加KeyLock锁
		Runner runner = test.new KeyLockRunner(maxMoney, number);
		
		Thread[] threads = new Thread[threadNum];
		for (int i = 0; i < threadNum; i++)
			threads[i] = new Thread(runner, "thread-" + i);
		long begin = System.currentTimeMillis();
		for (Thread t : threads)
			t.start();
		for (Thread t : threads)
			t.join();
		long time = System.currentTimeMillis() - begin;
		System.out.println("类型：" + runner.getClass().getSimpleName());
		System.out.printf("耗时：%dms\n", time);
		System.out.printf("初始总金额：%d\n", count * money);
		System.out.printf("终止总金额：%d\n", test.getAmount());
	}

	// 转账任务
	abstract class Runner implements Runnable {
		final int maxMoney;
		final int number;
		private final Random random = new Random();
		private final AtomicInteger count = new AtomicInteger();

		Runner(int maxMoney, int number) {
			this.maxMoney = maxMoney;
			this.number = number;
		}

		@Override
		public void run() {
			while(count.getAndIncrement() < number) {
				int from = random.nextInt(account.length);
				int to;
				while ((to = random.nextInt(account.length)) == from)
					;
				int money = random.nextInt(maxMoney);
				doTransfer(from, to, money);
			}
		}

		abstract void doTransfer(int from, int to, int money);
	}

	// 不加锁的转账
	class NonLockRunner extends Runner {
		NonLockRunner(int maxMoney, int number) {
			super(maxMoney, number);
		}

		@Override
		void doTransfer(int from, int to, int money) {
			transfer(from, to, money);
		}
	}

	// synchronized的转账
	class SynchronizedRunner extends Runner {
		SynchronizedRunner(int maxMoney, int number) {
			super(maxMoney, number);
		}

		@Override
		synchronized void doTransfer(int from, int to, int money) {
			transfer(from, to, money);
		}
	}

	// ReentrantLock的转账
	class ReentrantLockRunner extends Runner {
		private final ReentrantLock lock = new ReentrantLock();

		ReentrantLockRunner(int maxMoney, int number) {
			super(maxMoney, number);
		}

		@Override
		void doTransfer(int from, int to, int money) {
			lock.lock();
			try {
				transfer(from, to, money);
			} finally {
				lock.unlock();
			}
		}
	}

	// KeyLock的转账
	class KeyLockRunner extends Runner {
		private final KeyLock<Integer> lock = new KeyLock<Integer>();

		KeyLockRunner(int maxMoney, int number) {
			super(maxMoney, number);
		}

		@Override
		void doTransfer(int from, int to, int money) {
			Integer[] keys = new Integer[] {from, to};
			Arrays.sort(keys);
			lock.lock(keys);
			try {
				transfer(from, to, money);
			} finally {
				lock.unlock(keys);
			}
		}
	}
}