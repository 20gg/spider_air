package com.cl.pool.executor.impl;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cl.pool.executor.IThreadPoolExecutorService;

/**
 * 线程池创建实现类
 * 
 * @author chenlei
 *
 */
public class MyThreadPoolExecutorService implements IThreadPoolExecutorService {
	// 池初始化并发任务数
	private int corePoolSize;
	// 池中最大并发任务数
	private int maxPoolSize;
	private long keepAliveTime;
	// 任务队列最大存储任务数
	private int queueCapacity;
	// 任务拒绝策略
	private RejectedExecutionHandler myRejectedExecutionHandler;

	/**
	 * 实例化一个线程池
	 */
	@Override
	public ThreadPoolExecutor createNewThreadPool() {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(getCorePoolSize(),
				getMaxPoolSize(), getKeepAliveTime(), TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(getQueueCapacity()),
				getMyRejectedExecutionHandler());
		return executor;
	}

	@Override
	public int getCorePoolSize() {
		return corePoolSize;
	}

	@Override
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;

	}

	@Override
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	@Override
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;

	}

	@Override
	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	@Override
	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;

	}

	@Override
	public int getQueueCapacity() {
		return queueCapacity;
	}

	@Override
	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;

	}

	public RejectedExecutionHandler getMyRejectedExecutionHandler() {
		return myRejectedExecutionHandler;
	}

	public void setMyRejectedExecutionHandler(
			RejectedExecutionHandler myRejectedExecutionHandler) {
		this.myRejectedExecutionHandler = myRejectedExecutionHandler;
	}

}
