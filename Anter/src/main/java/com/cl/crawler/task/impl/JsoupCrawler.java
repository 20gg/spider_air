package com.cl.crawler.task.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.cl.crawler.task.HttpCrawlerTaskModel;

/**
 * 使用Jsoup工具执行http请求的采集任务模型
 * 
 * @author chenlei
 *
 */
public class JsoupCrawler extends HttpCrawlerTaskModel {
	
	private static final Log logger = LogFactory.getLog(JsoupCrawler.class);

	/**
	 * JsoupCrawler构造器
	 */
	public JsoupCrawler() {
		super();
	}

	/**
	 * 任务的执行方法
	 */
	@Override
	public void excute() {
		logger.info(" 当前任务 " + getTaskId() + " 的采集对象: " + domain);
		// 建立连接
		Connection conn = Jsoup.connect(getUrl());
		// 模拟浏览器
		conn.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:38.0) Gecko/20100101 Firefox/38.0");
		// 添加请求头
		Set<Entry<String, String>> set = getHeaders().entrySet();
		Iterator<Entry<String, String>> ite = set.iterator();
		while (ite.hasNext()) {
			Entry<String, String> entry = ite.next();
			conn.header(entry.getKey(), entry.getValue());
		}
		// 添加cookies
		
		// 设置代理

		// 设置连接超时
		conn.timeout(15000);
		// 设置参数
		conn.data(getQueryParams());
		// 执行method获取数据
		Document doc = null;
		try {
			if ("GET".equals(method)) {
				doc = conn.get();
			} else if ("POST".equals(method)) {
				doc = conn.post();
			} else {
				throw new IllegalArgumentException(" we do not support this request method= " + method + " please check your config...");
			}
		} catch (IOException e) {
			logger.error(" id=" + getTaskId() + " : ", e);
		} finally {
			if (doc == null) {
				setStatusCode(TASK_FAILED);
				checkCode.set(CHECK_BLANK);
			} else {
				String repStr = null;
				if ("json".equals(dataType)) {
					repStr = doc.text();
				} else if ("html".equals(dataType)) {
					repStr = doc.html();
				}
				doAfter(repStr);
			}
			logger.info("纪录任务的结束状态 : " + domain + "; id=" + getTaskId() + " ;statusCode="
					+ getStatusCode());

		}

	}

}
