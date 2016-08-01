package com.cl.pool.executor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池接口
 * 
 * @author chenlei
 *
 */
public interface IThreadPoolExecutorService {

	/** 创建线程池 */
	public ThreadPoolExecutor createNewThreadPool();

	/** 返回核心池大小 */
	public int getCorePoolSize();

	/** 设置核心池大小 */
	public void setCorePoolSize(int corePoolSize);

	/** 返回最大池大小 */
	public int getMaxPoolSize();

	/** 设置最大池大小 */
	public void setMaxPoolSize(int maxPoolSize);

	/** */
	public long getKeepAliveTime();

	/** */
	public void setKeepAliveTime(long keepAliveTime);

	/** 返回队列容量 */
	public int getQueueCapacity();

	/** 设置队列容量 */
	public void setQueueCapacity(int queueCapacity);

}
