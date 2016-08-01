package com.cl.crawler.worker;

import com.cl.httpclient4.util.ComUtil;
import com.cl.pool.task.manager.CrawlManagerInterface;

/**
 * 执行HTTP采集的主线程
 * 
 * @author chenlei
 *
 */
public abstract class HTTPFetcher extends Thread {

	// 线程id
	protected String threadId;
	// 当前的任务管理器
	protected CrawlManagerInterface manager;

	/**
	 * 构建请求线程
	 * 
	 * @param manager
	 *            : 任务管理器
	 */
	public HTTPFetcher(final CrawlManagerInterface manager) {
		super();
		this.threadId = ComUtil.getTaskid();
		this.manager = manager;

	}

	/**
	 * 线程执行任务的方法
	 */
	protected abstract void excute() throws Exception;
	
	/**
	 * 返回当前线程的id
	 * 
	 * @return
	 */
	public String getThreadId() {
		return threadId;
	}

}
