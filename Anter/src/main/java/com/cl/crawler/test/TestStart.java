package com.cl.crawler.test;

import java.util.List;
import java.util.Map;

import com.cl.crawler.entity.AirParamExporter;
import com.cl.crawler.entity.AirPoJo;
import com.cl.crawler.entity.Iair;
import com.cl.crawler.manager.CrawlerManagerHandel;
import com.cl.crawler.starter.StarterFetch;
import com.cl.crawler.task.HttpCrawlerTaskModel;
import com.cl.crawler.util.AnterFactory;
import com.cl.crawler.util.AppInitial;
import com.cl.crawler.util.CrawlerSpringUtil;
import com.cl.pool.starter.Starter;
import com.cl.pool.task.manager.CrawlManagerInterface;

/**
 * 测试入口
 * 
 * @author chenlei
 *
 */
public class TestStart {

	public static void main(String[] args) throws Exception {
		System.out.println(String.format("%S", Math.random()));
		System.out.println("Java类路径：" + System.getProperty("java.class.path")); // Java类路径
		System.out.println("Java输入输出临时路径：" + System.getProperty("java.io.tmpdir")); // Java输入输出临时路径
		System.out.println("操作系统用户的主目录：" + System.getProperty("user.home")); // 用户的主目录
		System.out.println("当前程序所在目录：" + System.getProperty("user.dir")); // 当前程序所在目录
		
		String relativelyPath=System.getProperty("user.dir");
		relativelyPath = relativelyPath.replace("\\", "/");
		AppInitial.getInstance().setParamModelDir(relativelyPath+"/src/main/resource/params/ParamModels.xml");
		AppInitial.getInstance().setCookieMapDir(relativelyPath+"/src/main/resource/cookies/CookieMap.xml");
		AppInitial.getInstance().setExtractorMapDir(relativelyPath+"/src/main/resource/extractors/ExtractorMap.xml");
		AppInitial.getInstance().init();

		CrawlManagerInterface manage = new CrawlerManagerHandel();
		List<Map<String, String>> paraMapList = AirParamExporter.exprotAirMap(new AirPoJo(Iair.DOMESTIC, "SHA", "CTU", "2016-01-29"));
		for (Map<String, String> paraMap : paraMapList) {
 			HttpCrawlerTaskModel task = AnterFactory.getInstanceByAirParam(paraMap);
			manage.putIn(task);
		}

		Starter starter = (StarterFetch) CrawlerSpringUtil.getBean("starterFetch");
		starter.setIntstance(manage);
		starter.start();

	}

}
