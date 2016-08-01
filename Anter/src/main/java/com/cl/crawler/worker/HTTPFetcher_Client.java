package com.cl.crawler.worker;

import java.io.IOException;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.cl.crawler.task.impl.HttpClientCrawler;
import com.cl.crawler.util.CrawlerSpringUtil;
import com.cl.httpclient4.entity.CommonClientParameter;
import com.cl.httpclient4.util.ConmanagerInterface;
import com.cl.httpclient4.util.HttpClientFactory;
import com.cl.pool.task.manager.CrawlManagerInterface;

/**
 * 执行采集的HttpClient主线程
 * <p>
 * 初始化client,执行http请求
 * </p>
 * 
 * @author chenlei
 *
 */
public class HTTPFetcher_Client extends HTTPFetcher {

	private static final Logger logger = Logger
			.getLogger(HTTPFetcher_Client.class);

	// 是否开启client并发
	private Boolean isClientConcurrent = false;
	// 任务
	private HttpClientCrawler task;
	// 一组任务
	private HttpClientCrawler[] tasks;
	// 当前的任务管理器
	private CrawlManagerInterface manager;

	// =========================httpclient=========================
	private CommonClientParameter ccp;

	private HttpClientConnectionManager connManager;

	private CloseableHttpClient client;

	/**
	 * 构建请求线程
	 * 
	 * @param task
	 *            :任务
	 * @param manager
	 *            : 任务管理器
	 */
	public HTTPFetcher_Client(final HttpClientCrawler task,
			final CrawlManagerInterface manager) {
		super(manager);
		this.task = task;
		this.manager = manager;
		// client初始化
		this.client = HttpClientFactory.getHttpClient(null);
		setIsClientConcurrent(false);

	}

	/**
	 * 构建请求线程
	 * 
	 * @param tasks
	 *            : 任务数组
	 * @param manager
	 *            : 任务管理器
	 */
	public HTTPFetcher_Client(HttpClientCrawler[] tasks,
			CrawlManagerInterface manager) {
		super(manager);
		this.tasks = tasks;
		this.manager = manager;
		// client初始化
		this.ccp = (CommonClientParameter) CrawlerSpringUtil
				.getBean("commonClientParameter");
		this.connManager = com.cl.httpclient4.util.InitializedPoolingHttpClientConnectionManager
				.getConnManager(ccp, ConmanagerInterface.CON_DEFAULLT);
		this.client = HttpClientFactory.getHttpClient(connManager);
		setIsClientConcurrent(true);

	}

	@Override
	public void run() {
		super.run();
		try {
			excute();
		} catch (InterruptedException e) {
			logger.error("", e);
		} finally {
			try {
				HttpClientFactory.shutdown(client, null, null, null, null);
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	/**
	 * 线程执行任务的方法
	 * 
	 * @throws InterruptedException
	 * 
	 * @throws Exception
	 */
	protected void excute() throws InterruptedException {

		if (isClientConcurrent) {
			// create a thread for each task
			ClientChildThread[] threads = new ClientChildThread[tasks.length];
			for (int index = 0; index < threads.length; index++) {
				threads[index] = new ClientChildThread(index + 1, client,
						tasks[index], manager);
			}
			// start the threads
			for (int j = 0; j < threads.length; j++) {
				threads[j].start();
			}
			// join the threads
			for (int j = 0; j < threads.length; j++) {
				threads[j].join();
			}
		} else {
			task.setClient(client);
			manager.runTask(task);
		}

	}

	public Boolean getIsClientConcurrent() {
		return isClientConcurrent;
	}

	private void setIsClientConcurrent(Boolean isClientConcurrent) {
		this.isClientConcurrent = isClientConcurrent;
	}

}
