package com.cl.crawler.worker;

import org.apache.log4j.Logger;

import com.cl.crawler.task.impl.JsoupCrawler;
import com.cl.pool.task.manager.CrawlManagerInterface;

/**
 * 执行采集的Jsoup主线程
 * 
 * @author chenlei
 *
 */
public class HTTPFetcher_Jsoup extends HTTPFetcher {

	private static final Logger logger = Logger.getLogger(HTTPFetcher_Jsoup.class);
	
	// 任务	
	private JsoupCrawler task;

	/**
	 * 构建请求线程
	 * 
	 * @param task
	 *            :任务
	 * @param manager
	 *            : 任务管理器
	 */
	public HTTPFetcher_Jsoup(JsoupCrawler task,
			CrawlManagerInterface manager) {
		super(manager);
		this.task = task;
	}

	@Override
	public void run() {
		super.run();
		try {
			excute();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	protected void excute() throws Exception {
		manager.runTask(task);
	}

}
