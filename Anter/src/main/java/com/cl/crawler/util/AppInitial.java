package com.cl.crawler.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cl.crawler.entity.BeanMap;

/**
 * 一些配置缓存
 * 
 * @author chenlei
 *
 */
public class AppInitial {

	/** ParamModels配置文件路径 */
	private String ParamModelDir;

	/** CookieMap配置文件路径 */
	private String CookieMapDir;
	/** ExtractorMap配置文件路径 */
	private String ExtractorMapDir;

	private final List<BeanMap> CookieMapList;

	private final List<BeanMap> ExtractorMapList;

	private AppInitial() {
		CookieMapList = new ArrayList<BeanMap>();
		ExtractorMapList = new ArrayList<BeanMap>();
	}

	private static volatile AppInitial instance = null;

	public static AppInitial getInstance() {
		if (instance == null) {
			synchronized (AppInitial.class) {
				instance = new AppInitial();
			}
		}
		return instance;
	}

	/**
	 * 这里初始化了一些全局变量
	 * 
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public synchronized void init() throws IOException, CloneNotSupportedException {
		if (StringUtils.isEmpty(CookieMapDir)) {
			throw new NullArgumentException(" CookieMapDir can not be empty ");
		} else {
			CookieMapList.clear();
			list(CookieMapList, CookieMapDir, new BeanMap());
		}
		
		if (StringUtils.isEmpty(ExtractorMapDir)) {
			throw new NullArgumentException(" ExtractorMapDir can not be empty ");
		} else {
			ExtractorMapList.clear();
			list(ExtractorMapList, ExtractorMapDir, new BeanMap());
		}

	}

	/**
	 * 填装list集合
	 * 
	 * @param list
	 * @param dir
	 * @param beanMap
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 */
	private void list(final List<BeanMap> list, final String dir, final BeanMap beanMap)
			throws IOException, CloneNotSupportedException {
		Document doc = Jsoup.parse(new File(dir), "UTF-8");
		Elements maps = doc.select("map");
		if (!maps.isEmpty()) {
			ListIterator<Element> elementite = maps.listIterator();
			while (elementite.hasNext()) {
				Element map = elementite.next();
				BeanMap beanMapCopy = beanMap.clone();
				beanMapCopy.setBean(map.attr("bean"));
				beanMapCopy.setDomain(map.attr("domain"));
				beanMapCopy.setInter(map.attr("inter"));
				list.add(beanMapCopy);
			}
		}

	}

	// ===================================get/set===================================

	/**
	 * 返回AppInitial缓存的CookieMapList
	 * 
	 * @return
	 */
	public List<BeanMap> getCookieMapList() {
		return CookieMapList;
	}

	/**
	 * 返回AppInitial缓存的ExtractorMapList
	 * 
	 * @return
	 */
	public List<BeanMap> getExtractorMapList() {
		return ExtractorMapList;
	}

	/**
	 * 注入CookieMap.xml
	 * 
	 * @param cookieMapDir
	 */
	public synchronized void setCookieMapDir(String cookieMapDir) {
		if (CookieMapDir == null) {
			CookieMapDir = cookieMapDir;
		}
	}

	/**
	 * 获取机票ParamModels配置文件路径
	 * 
	 * @return
	 */
	public String getParamModelDir() {
		return ParamModelDir;
	}

	/**
	 * 注入ParamModels.xml
	 * 
	 * @param paramModelDir
	 */
	public synchronized void setParamModelDir(String paramModelDir) {
		if (ParamModelDir == null) {
			ParamModelDir = paramModelDir;
		}

	}

	/**
	 * 注入ExtractorMap.xml
	 * 
	 * @param extractorMapDir
	 */
	public synchronized void setExtractorMapDir(String extractorMapDir) {
		if (ExtractorMapDir == null) {
			ExtractorMapDir = extractorMapDir;
		}

	}

}
