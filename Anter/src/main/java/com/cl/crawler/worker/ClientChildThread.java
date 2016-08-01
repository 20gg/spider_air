package com.cl.crawler.worker;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.cl.crawler.task.impl.HttpClientCrawler;
import com.cl.pool.task.manager.CrawlManagerInterface;

/**
 * client并发子线程
 * 
 * @author chenlei
 *
 */
public class ClientChildThread extends Thread {

	private static final Logger logger = Logger.getLogger(ClientChildThread.class);
	
	// 线程id
	private Integer childThreadId;

	private CloseableHttpClient client;

	private HttpClientCrawler task;

	private CrawlManagerInterface manager;

	/**
	 * client并发子线程构造器
	 * 
	 * @param childThreadId
	 * @param client
	 * @param task
	 * @param manager
	 */
	public ClientChildThread(Integer childThreadId, CloseableHttpClient client,
			HttpClientCrawler task, CrawlManagerInterface manager) {
		super();
		this.childThreadId = childThreadId;
		this.client = client;
		this.task = task;
		this.manager = manager;
	}

	@Override
	public void run() {
		try {
			excute();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			logger.info(childThreadId + " - get executed");
		}

	}

	/**
	 * 线程执行方法
	 */
	private void excute() {
		task.setClient(client);
		manager.runTask(task);
	}

}
