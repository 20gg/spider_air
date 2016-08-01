package com.cl.pool.task.manager;

import java.util.Queue;

import com.cl.pool.task.CrawlertaskInterface;

/**
 * 采集任务管理器接口
 * 
 * @author chenlei
 *
 */
public interface CrawlManagerInterface {

	/**
	 * 向任务队列添加任务
	 * 
	 * @param task
	 */
	public void putIn(final CrawlertaskInterface task);

	/**
	 * 从任务队列弹出任务
	 * 
	 * @return
	 */
	public CrawlertaskInterface polltask();

	/**
	 * 根据任务id获取任务
	 * 
	 * @param taskId
	 * @return
	 */
	public CrawlertaskInterface getTaskById(String taskId);

	/**
	 * 以数组形式，返回所有任务
	 * 
	 * @return
	 */
	public CrawlertaskInterface[] getAll();

	/**
	 * 获取任务队列
	 * 
	 * @return
	 */
	public Queue<CrawlertaskInterface> getTasks();

	/**
	 * 判断任务队列是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * 获取任务队列大小
	 * 
	 * @return
	 */
	public Integer getSize();

	/**
	 * 清空任务队列
	 */
	public void clear();

	/**
	 * 检查所有的任务的状态
	 */
	public void checkAllTaskStatus();

	/**
	 * 启动任务
	 * 
	 * @param client
	 * @param task
	 * @return
	 */
	public boolean runTask(final CrawlertaskInterface task);

	/**
	 * 检查此任务的状态
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 */
	public void checkTaskStatus(final CrawlertaskInterface task)
			throws Exception;

}
