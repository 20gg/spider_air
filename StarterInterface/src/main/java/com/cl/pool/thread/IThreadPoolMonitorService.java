package com.cl.pool.thread;

import java.util.concurrent.ThreadPoolExecutor;

import com.cl.pool.task.manager.CrawlManagerInterface;

/**
 * 线程池监控线程接口
 * 
 * @author chenlei
 *
 */
public interface IThreadPoolMonitorService extends Runnable {

	/** 监控方法 */
	public void monitorThreadPool();

	/** 返回监控线程当前监控的线程池 */
	public ThreadPoolExecutor getExecutor();

	/** 设置监控线程当前监控的线程池 */
	public void setExecutor(ThreadPoolExecutor executor);
	
	public void setCrawlManagerInterface(CrawlManagerInterface manager);
	
}
