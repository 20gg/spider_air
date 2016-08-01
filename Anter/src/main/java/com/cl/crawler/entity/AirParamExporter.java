package com.cl.crawler.entity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.cl.crawler.util.AppInitial;
import com.cl.httpclient4.util.ComUtil;

/**
 * 机票参数输出器
 * 
 * @author chenlei
 *
 */
public class AirParamExporter {

	private AirParamExporter() {
	}

	private static final Log logger = LogFactory.getLog(AirParamExporter.class);

	/**
	 * 机票参数输出方法
	 * 
	 * @param airPoJo
	 * @param inter
	 * @return
	 */
	public static List<Map<String, String>> exprotAirMap(final AirPoJo airPoJo) {
		List<Map<String, String>> paraMaplist = new ArrayList<Map<String, String>>();
		Document doc = null;
		String paramDir = AppInitial.getInstance().getParamModelDir();
		try {
			doc = Jsoup.parse(new File(paramDir), "UTF-8");
			Elements paramMapelements = doc.select("paramMap[status=true][inter=" + airPoJo.getInter() + "]");
			paramMapelements.addAll(doc.select("paramMap[status=true][inter=2]"));
			Iterator<Element> ite = paramMapelements.listIterator();
			while (ite.hasNext()) {
				// 根据一个参数模板,组装参数
				Element paramMapelement = ite.next();
				String beanName = paramMapelement.attr("beanName");
				Map<String, String> paraMap = new HashMap<String, String>();
				paraMap.put("beanName", beanName);
				List<Node> nodes = paramMapelement.childNodes();
				for (Node node : nodes) {
					Object obj = compute(airPoJo, node);
					if (obj != null) {
						if (obj instanceof String) {
							paraMap.put(node.attr("key"), (String) obj);
						} else {
							String trans = node.attr("trans");
							if ("json".equals(trans)) {
								String jsonValue = ComUtil.toJson(obj);
								paraMap.put(node.attr("key"), jsonValue);
							}
						}
					}
				}
				paraMaplist.add(paraMap);

			}

		} catch (IOException e) {
			logger.error("", e);
		}
		return paraMaplist;

	}

	/**
	 * 迭代算法生成机票参数map
	 * 
	 * @param airPoJo
	 * @param node
	 * @param obj
	 * @return
	 */
	protected static Object compute(final AirPoJo airPoJo, final Node node) {
		// 提取method属性值
		String method = node.attr("method");
		// 提取type属性值
		String type = node.attr("type");
		// 提取filter属性值
		String filter = node.attr("filter");		
		if ("text".equals(type)) {
			String value = ((Element) node).text();
			switch (method) {
			case "millis":
				value = String.format(value, System.currentTimeMillis());
				break;
			case "uuid":
				value = String.format(value, ComUtil.getTaskid());
				break;
			case "db":
				String describe = node.attr("describe");
				if ("出发地".equals(describe)) {
					value = String.format(value, airPoJo.getDepCode());
				} else if ("到达地".equals(describe)) {
					value = String.format(value, airPoJo.getArrCode());
				} else if ("出发日期".equals(describe)) {
					value = String.format(value, airPoJo.getDepDate());
				} else if ("国内/国际航班".equals(describe)) {
					value = String.format(value, airPoJo.getInter());
				} else {
					throw new IllegalArgumentException(
							" wo do not support this describe= " + describe + " please check config...");
				}
				break;
			case "rand":
				value = String.format(value, Math.random());
				break;
			default:
				break;
			}
			if (!StringUtils.isEmpty(filter)) {
				value = value.replaceAll(filter, "");
			}
			return value;
		} else if ("map".equals(type)) {
			Map<String, Object> nodeMap = new HashMap<String, Object>();
			List<Node> childNodes = node.childNodes();
			for (Node childNode : childNodes) {
				Object obj = compute(airPoJo, childNode);
				if (obj != null) {
					nodeMap.put(childNode.attr("key"), obj);
				}
			}
			return nodeMap;
		} else if ("list".equals(type)) {
			List<Map<String, Object>> nodeList = new LinkedList<Map<String, Object>>();
			Map<String, Object> segmap = new HashMap<String, Object>();
			List<Node> childNodes = node.childNodes();
			for (Node childNode : childNodes) {
				Object obj = compute(airPoJo, childNode);
				if (obj != null) {
					segmap.put(childNode.attr("key"), obj);
				}
			}
			nodeList.add(segmap);
			return nodeList;
		} else {
			return null;
		}

	}

	public static void main(String[] args) {
		AppInitial.getInstance().setParamModelDir("D:/workspace/myproject/Anter/conf/params/ParamModels.xml");
		System.out.println(exprotAirMap(new AirPoJo(Iair.INTERNATIONAL, "NKG", "SEL", "2015-09-26")));
	}

}
