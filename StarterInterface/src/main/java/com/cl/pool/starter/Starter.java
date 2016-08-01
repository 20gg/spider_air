package com.cl.pool.starter;

import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.cl.pool.executor.IThreadPoolExecutorService;
import com.cl.pool.task.manager.CrawlManagerInterface;
import com.cl.pool.thread.IThreadPoolMonitorService;

/**
 * 启动类模版
 * 
 * @author chenlei
 *
 */
public abstract class Starter {

	// 任务管理器
	protected CrawlManagerInterface intstance;

	protected static Logger logger = Logger.getLogger(Starter.class);

	protected IThreadPoolMonitorService myThreadPoolMonitorService;

	protected IThreadPoolExecutorService myThreadPoolExecutorService;

	protected ThreadPoolExecutor executor;

	public Starter() {
		super();
	}

	/**
	 * 执行方法,启动线程池...
	 */
	public void start() {
		if (intstance == null) {
			logger.debug(" manager can not be null!!! ");
			return;
		}
		// A new thread pool is created...
		executor = myThreadPoolExecutorService.createNewThreadPool();
		executor.allowCoreThreadTimeOut(true);

		// Created executor is set to ThreadPoolMonitorService...
		myThreadPoolMonitorService.setExecutor(executor);
		myThreadPoolMonitorService.setCrawlManagerInterface(intstance);

		// ThreadPoolMonitorService is started...
		Thread monitor = new Thread(myThreadPoolMonitorService);
		monitor.start();

		// New tasks are executed...
		executed();

		// executor is shutdown...
		executor.shutdown();
	}

	/**
	 * 提交任务到线程池,并执行
	 */
	protected abstract void executed();

	// =====================================get/set方法=====================================

	public IThreadPoolMonitorService getMyThreadPoolMonitorService() {
		return myThreadPoolMonitorService;
	}

	/**
	 * 返回线程池当前持有地采集任务管理器
	 * 
	 * @return
	 */
	public CrawlManagerInterface getIntstance() {
		return intstance;
	}

	/**
	 * 向线程池注入任务管理器
	 * 
	 * @param intstance
	 */
	public void setIntstance(CrawlManagerInterface intstance) {
		this.intstance = intstance;
	}

	public void setMyThreadPoolMonitorService(
			IThreadPoolMonitorService myThreadPoolMonitorService) {
		this.myThreadPoolMonitorService = myThreadPoolMonitorService;
	}

	public IThreadPoolExecutorService getMyThreadPoolExecutorService() {
		return myThreadPoolExecutorService;
	}

	public void setMyThreadPoolExecutorService(
			IThreadPoolExecutorService myThreadPoolExecutorService) {
		this.myThreadPoolExecutorService = myThreadPoolExecutorService;
	}

}
