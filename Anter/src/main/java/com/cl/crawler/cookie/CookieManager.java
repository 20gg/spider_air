package com.cl.crawler.cookie;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cl.cookie.manager.IcookieManager;
import com.cl.crawler.entity.BeanMap;
import com.cl.crawler.util.AppInitial;
import com.cl.crawler.util.CrawlerSpringUtil;

/**
 * Cookie管理器
 * 
 * @author chenlei
 *
 */
public class CookieManager {

	private CookieManager() {
	}

	/**
	 * 更新headers map的cookie值
	 * 
	 * @param headers
	 * @param domain
	 * @param inter
	 */
	public static void updateCookieHeader(final Map<String, String> headers, final String domain, final String inter) {
		String key = null;
		List<BeanMap> cookieMaps = AppInitial.getInstance().getCookieMapList();
		int index = 0;
		do {
			cookieMaps = AppInitial.getInstance().getCookieMapList();
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			index++;
		} while (cookieMaps.isEmpty() && index < 5);
		if (cookieMaps.isEmpty()) {
			throw new NullPointerException(
					" Our cookieMaps is empty, please check your config... ");
		}
		Iterator<BeanMap> ite = cookieMaps.iterator();
		while (ite.hasNext()) {
			BeanMap cookieMap = ite.next();
			if (domain.equals(cookieMap.getDomain())
					&& inter.equals(cookieMap.getInter())) {
				key = cookieMap.getBean();
				break;
			}

		}
		if (key == null) {
			throw new NullPointerException(" wo do not support this website: "
					+ domain + " please check your config...");
		} else {
			IcookieManager instance = (IcookieManager) CrawlerSpringUtil
					.getBean(key);
			headers.put("Cookie", instance.pollCookie());
		}

	}

	public static void main (String[] args) {
		Map<String, String> headers = new HashMap<String, String>();
		updateCookieHeader(headers, "alitrip.com", "1");
	}
	
	
}
