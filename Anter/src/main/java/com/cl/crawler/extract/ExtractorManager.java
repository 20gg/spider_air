package com.cl.crawler.extract;

import java.util.Iterator;
import java.util.List;

import com.cl.crawler.entity.BeanMap;
import com.cl.crawler.util.AppInitial;
import com.cl.crawler.util.CrawlerSpringUtil;
import com.cl.data.parse.IdataParse;

/**
 * 抽取管理器
 * 
 * @author chenlei
 *
 */
public class ExtractorManager {

	private ExtractorManager() {
	}

	/**
	 * 数据抽取方法
	 * 
	 * @param datastr
	 * @param domain
	 * @param inter
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public static int extractData(final String datastr, final String domain, final String inter, final String taskId) {
		String key = null;
		List<BeanMap> extractorMapList = AppInitial.getInstance().getExtractorMapList();
		int index = 0;
		do {
			extractorMapList = AppInitial.getInstance().getExtractorMapList();
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			index ++;
		} while (extractorMapList.isEmpty() && index < 5);
		Iterator<BeanMap> ite = extractorMapList.iterator();
		while (ite.hasNext()) {
			BeanMap extractorMap = ite.next();
			if (domain.equals(extractorMap.getDomain()) && inter.equals(extractorMap.getInter())) {
				key = extractorMap.getBean();
				break;
			}
		}
		if (key == null) {
			throw new NullPointerException(" wo do not support this website: "
					+ domain + " please check your config...");
		} else {
			IdataParse instance = (IdataParse) CrawlerSpringUtil.getBean(key);
			instance.setDataStr(datastr);
			return instance.parseData(taskId);
		}

	}

}
