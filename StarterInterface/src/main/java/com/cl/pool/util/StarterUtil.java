package com.cl.pool.util;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Starter类 的spring输出工厂
 * 
 * @author chenlei
 *
 */
public class StarterUtil {

	private StarterUtil() {
	}

	private static ApplicationContext context = new ClassPathXmlApplicationContext("StarterContext.xml");
	
	/**
	 * 根据beanName生成对应的bean
	 * 
	 * @param beanName
	 * @return
	 */
	public static Object getStarterBean(String beanName) {
		return context.getBean(beanName);

	}
	
	public static void main(String[] args) {
		System.out.println(new Date(1439819606537l));
		
		System.out.println(Math.random());
	}
	

}
