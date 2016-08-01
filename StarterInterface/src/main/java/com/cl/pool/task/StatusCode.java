package com.cl.pool.task;

/**
 * 任务状态接口
 * 
 * @author chenlei
 *
 */
public interface StatusCode {

	/** 任务状态 －－ 等待*/
	public static final int TASK_WAIT = 0;
	
	/** 任务状态 －－ 失败*/
	public static final int TASK_FAILED = 1;
	
	/** 任务状态 －－ 成功*/
	public static final int TASK_SUCCESS = 2;
	
}
