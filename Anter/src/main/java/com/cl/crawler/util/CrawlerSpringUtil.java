package com.cl.crawler.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Crawler的Spring对象输出工厂
 * 
 * @author chenlei
 *
 */
public class CrawlerSpringUtil {

	private CrawlerSpringUtil() {
	}

	private final static ApplicationContext context = new ClassPathXmlApplicationContext("CookieContext.xml",
			"ClientConcurrentContext.xml", "ParserContext.xml", "StarterContext.xml");

	/**
	 * 根据spring的beanName输出对象的对象
	 * 
	 * @param beanName
	 * @return
	 */
	public static Object getBean(final String beanName) {
		return context.getBean(beanName);

	}

}
