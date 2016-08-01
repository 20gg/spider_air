package com.cl.pool.task;

/**
 * 采集任务接口
 * 
 * @author chenlei
 * 
 */
public interface CrawlertaskInterface extends StatusCode {

	/**
	 * 返回当前任务的id
	 * 
	 * @return
	 */
	public abstract String getTaskId();

	/**
	 * 返回当前任务的状态码
	 * 
	 * @return
	 */
	public abstract Integer getStatusCode();

	/**
	 * 返回当前任务执行后的数据
	 * 
	 * @return
	 */
	public abstract String getDataStr();

	/**
	 * 任务的执行方法
	 */
	public abstract void excute();

	/**
	 * 自定义地任务数据输出方法
	 * 
	 * @param statusCode
	 *            <p>
	 *            statusCode=1时,输出错误信息
	 *            </p>
	 * 
	 * @return
	 */
	public abstract String toString(Integer statusCode);

}
