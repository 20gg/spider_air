package com.cl.crawler.starter;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.cl.crawler.manager.CrawlerManagerHandel;
import com.cl.pool.task.CrawlertaskInterface;
import com.cl.pool.task.manager.CrawlManagerInterface;
import com.cl.pool.thread.IThreadPoolMonitorService;

/**
 * 具体的监控线程
 * 
 * @author chenlei
 *
 */
public class MyThreadPoolMonitorService implements IThreadPoolMonitorService {

	private static final Logger logger = Logger
			.getLogger(MyThreadPoolMonitorService.class);

	// 被监控的线程池
	private ThreadPoolExecutor executor;
	// 监控时间间隔(单位:s)
	private Integer monitoringPeriod;
	// 任务管理器
	private CrawlerManagerHandel intstance;
	// 任务队列
	private Queue<CrawlertaskInterface> tasks;

	// 线程运行方法
	@Override
	public void run() {

		tasks = intstance.getTasks();
		while (!tasks.isEmpty()) {

			Iterator<CrawlertaskInterface> ite = tasks.iterator();
			while (ite.hasNext()) {
				CrawlertaskInterface task = ite.next();
				if (task.getStatusCode() != CrawlertaskInterface.TASK_WAIT) {
					threadexport(task);
					ite.remove();
				}
			}

			// 每执行过一次,等待 monitoringPeriod
			try {
				Thread.sleep(monitoringPeriod * 1000L);
			} catch (InterruptedException e) {
				logger.error("", e);
			} finally {
				monitorThreadPool();
			}

		}

	}

	// 监控方法:log出线程池当前的状态
	@Override
	public void monitorThreadPool() {
		if (executor != null) {
			StringBuilder strBuff = new StringBuilder();
			strBuff.append("CurrentPoolSize : ").append(executor.getPoolSize());
			strBuff.append(" - CorePoolSize : ").append(
					executor.getCorePoolSize());
			strBuff.append(" - MaximumPoolSize : ").append(
					executor.getMaximumPoolSize());
			strBuff.append(" - ActiveTaskCount : ").append(
					executor.getActiveCount());
			strBuff.append(" - CompletedTaskCount : ").append(
					executor.getCompletedTaskCount());
			strBuff.append(" - TotalTaskCount : ").append(
					executor.getTaskCount());
			strBuff.append(" - isTerminated : ")
					.append(executor.isTerminated());

			logger.debug(strBuff.toString());
		}
	}

	/**
	 * 输出执行过的任务的数据信息
	 * 
	 * @param task
	 */
	private void threadexport(final CrawlertaskInterface task) {
		try {
			intstance.checkTaskStatus(task);
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	@Override
	public ThreadPoolExecutor getExecutor() {
		return executor;
	}

	@Override
	public void setExecutor(ThreadPoolExecutor executor) {
		this.executor = executor;
	}

	public Integer getMonitoringPeriod() {
		return monitoringPeriod;
	}

	public void setMonitoringPeriod(Integer monitoringPeriod) {
		this.monitoringPeriod = monitoringPeriod;
	}

	@Override
	public void setCrawlManagerInterface(CrawlManagerInterface manager) {
		this.intstance = (CrawlerManagerHandel) manager;

	}

}
