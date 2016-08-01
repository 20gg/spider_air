package com.cl.pool.executor.impl;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

/**
 * 任务拒绝策略
 * 
 * @author chenlei
 *
 */
public class MyRejectedExecutionHandler implements RejectedExecutionHandler {

	private static Logger logger = Logger.getLogger(MyRejectedExecutionHandler.class);
	
	/**
	 * 任务拒绝策略，不全有待后续补充
	 */
	@Override
	public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
		logger.debug(runnable.toString() + " : has been rejected...  ");
		//执行失败任务
        new Thread(runnable,"exception by pool").start();
	}

}
