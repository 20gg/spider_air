package com.cl.crawler.manager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cl.pool.task.CrawlertaskInterface;
import com.cl.pool.task.StatusCode;
import com.cl.pool.task.manager.CrawlManagerInterface;

/**
 * 机票采集任务管理器
 * 
 * @author chenlei
 *
 */
public class CrawlerManagerHandel implements CrawlManagerInterface {

	/** 采集任务队列 */
	private final Queue<CrawlertaskInterface> tasks;

	private static final Log logger = LogFactory
			.getLog(CrawlerManagerHandel.class);

	/** 判断checkAlltaskStatus()方法是否已调用 */
	private static boolean ischeckde = false;

	/** 机票采集任务管理器－－构造方法 */
	public CrawlerManagerHandel() {
		super();
		/** 初始化采集任务队列 */
		this.tasks = new LinkedList<CrawlertaskInterface>();
	}

	@Override
	public void putIn(CrawlertaskInterface task) {
		this.tasks.offer(task);

	}

	@Override
	public CrawlertaskInterface polltask() {
		return this.tasks.poll();
	}

	@Override
	public CrawlertaskInterface getTaskById(String taskId) {
		CrawlertaskInterface[] taskArr = getAll();
		for (CrawlertaskInterface task : taskArr) {
			if (taskId.equals(task.getTaskId())) {
				return task;
			}
		}
		return null;

	}

	@Override
	public CrawlertaskInterface[] getAll() {
		CrawlertaskInterface[] taskArr = this.tasks
				.toArray(new CrawlertaskInterface[getSize()]);
		return taskArr;

	}

	@Override
	public Queue<CrawlertaskInterface> getTasks() {
		return this.tasks;
	}

	@Override
	public boolean isEmpty() {
		return this.tasks.isEmpty();
	}

	@Override
	public Integer getSize() {
		return this.tasks.size();
	}

	@Override
	public void clear() {
		this.tasks.clear();
		ischeckde = false;
	}

	/**
	 * 检查所有任务的状态，剔除不合法的任务；仅能在线程池启动前调用一次；每个任务管理器最多调用一次。
	 */
	@Override
	public void checkAllTaskStatus() {
		if (ischeckde) {
			return;
		}
		logger.info(" 进入checkAlltaskStatus()方法,当前管理器中存储的任务数： " + getSize());
		Iterator<CrawlertaskInterface> ite = this.tasks.iterator();
		while (ite.hasNext()) {
			CrawlertaskInterface task = ite.next();
			if (task.getStatusCode() != StatusCode.TASK_WAIT) {
				ite.remove();
			}
		}
		ischeckde = true;
		logger.info(" 退出checkAlltaskStatus()方法,当前管理器中存储的任务数： " + getSize());

	}

	/**
	 * 任务管理器，启动任务
	 */
	@Override
	public boolean runTask(final CrawlertaskInterface task) {
		boolean result = true;
		try {
			task.excute();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	@Override
	public void checkTaskStatus(CrawlertaskInterface task) throws Exception {
		if (task.getStatusCode() == StatusCode.TASK_FAILED) {
			// 任务执行失败，在错误目录输出
			logger.info((task + " :is failed"));
			logger.info(task.toString(StatusCode.TASK_FAILED));
		} else if (task.getStatusCode() == StatusCode.TASK_WAIT) {
			// 异常原因导致任务未执行，进入...

		}
	}

}
