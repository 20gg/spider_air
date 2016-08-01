package com.cl.crawler.util;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cl.crawler.task.HttpCrawlerTaskModel;

/**
 * 采集任务输出工厂
 * 
 * @author chenlei
 *
 */
public class AnterFactory {

	private AnterFactory() {
	}
/**
 * ClassPathXmlApplicationContext的后面的路径可以用以下方法获取
 * System.out.println(ClassLoader.getSystemResource("") );
 * System.out.println(Thread.currentThread().getContextClassLoader ().getResource(""));
 * 输出的是类的根目录	
 */
	private static ApplicationContext context = new ClassPathXmlApplicationContext("CrawlerTaskContext.xml");

	/**
	 * 根据SpringBeanName输出采集任务
	 * 
	 * @param beanName
	 * @return
	 */
	public static HttpCrawlerTaskModel getInstance(final String beanName) {
		HttpCrawlerTaskModel task = (HttpCrawlerTaskModel) context
				.getBean(beanName);
		return task;
	}

	/**
	 * 根据采集参数输出采集任务
	 * 
	 * @param airParam
	 * @return
	 */
	public static HttpCrawlerTaskModel getInstanceByAirParam(
			final Map<String, String> queryParams) {
		HttpCrawlerTaskModel task = getInstance(queryParams.get("beanName"));
		queryParams.remove("beanName");
		task.setQueryParams(queryParams);
		return task;

	}

}
