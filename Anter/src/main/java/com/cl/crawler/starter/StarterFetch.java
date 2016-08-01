package com.cl.crawler.starter;

import com.cl.crawler.task.impl.HttpClientCrawler;
import com.cl.crawler.task.impl.JsoupCrawler;
import com.cl.crawler.worker.HTTPFetcher;
import com.cl.crawler.worker.HTTPFetcher_Client;
import com.cl.crawler.worker.HTTPFetcher_Jsoup;
import com.cl.pool.starter.Starter;
import com.cl.pool.task.CrawlertaskInterface;

/**
 * fetcher 启动类
 * 
 * @author chenlei
 *
 */
public class StarterFetch extends Starter {

	public StarterFetch() {
		super();
	}

	/**
	 * 提交fetch任务到线程池,并执行
	 */
	@Override
	protected void executed() {
		if (!intstance.isEmpty()) {
			intstance.checkAllTaskStatus();
			CrawlertaskInterface[] tasks = intstance.getAll();
			for (CrawlertaskInterface task : tasks) {
				HTTPFetcher fetcher = null;
				if (task instanceof HttpClientCrawler) {
					fetcher = new HTTPFetcher_Client((HttpClientCrawler) task, intstance);
				} else if (task instanceof JsoupCrawler) {
					fetcher = new HTTPFetcher_Jsoup((JsoupCrawler) task, intstance);
				}
				executor.execute(fetcher);
			}
		} else {
			logger.debug(" 任务管理器不能为空!!! ");
		}
	}

}
