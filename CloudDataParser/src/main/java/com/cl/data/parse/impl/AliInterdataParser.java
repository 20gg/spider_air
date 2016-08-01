package com.cl.data.parse.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cl.data.entity.ori.OriAirData;
import com.cl.data.entity.ori.OriAirData.Cabin;
import com.cl.data.parse.DBdataParser;
import com.cl.data.util.ParseUtil;

/**
 * Ali机票(国际)解析器
 * 
 * @author chenlei
 *
 */
public class AliInterdataParser extends DBdataParser {

	private final static Log logger = LogFactory
			.getLog(AliInterdataParser.class);

	public AliInterdataParser() {
		super();
	}

	/**
	 * 以树模式解析Ali国际机票数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<OriAirData> parse() {
		// 通用原始机票数据list集合
		List<OriAirData> oriAirDatas = super.parse();
		// 获取data节点的map
		Map<String, Object> data = (Map<String, Object>) rootMap.get("data");
		if (data == null || data.isEmpty()) {
			return Collections.EMPTY_LIST;
		}		
		// 获取flightMap节点的map(这里存储了机票的出发 到达地 时间)
		Map<String, Object> flightMap = (Map<String, Object>) data
				.get("flightMap");
		// 获取items节点list(这里存储了机票的票价详情)
		List<Map<String, Object>> items = (List<Map<String, Object>>) data
				.get("items");
		Cabin cabin = new Cabin();
		OriAirData oriAirData = new OriAirData();
		// 解析items生成OriAirData
		Iterator<Map<String, Object>> ite = items.iterator();
		while (ite.hasNext()) {
			OriAirData oriAirDataCopy = null;
			Cabin cabinCopy = null;
			try {
				oriAirDataCopy = oriAirData.clone();
				cabinCopy = cabin.clone();
			} catch (CloneNotSupportedException e) {
				logger.error("", e);
			}
			Map<String, Object> itemMap = ite.next();
			String id = (String) itemMap.get("id");
			oriAirDataCopy.setId(id);
			Integer transCount = (Integer) itemMap.get("transCount");
			oriAirDataCopy.setTransCount(transCount);
			// 获取subitems节点(阿里实际存储机票票价详情的数组)
			List<Map<String, Object>> subitems = (List<Map<String, Object>>) itemMap.get("subitems");
			Map<String, Object> subitemMap = subitems.get(0);
			// 获取票价
			Integer price = (Integer) subitemMap.get("price");
			Integer adultTax = (Integer) subitemMap.get("adultTax");
			cabinCopy.setPrice(price);
			cabinCopy.setTax(adultTax);
			// 获取舱位剩余数
			String cabinCount = subitemMap.get("cabinCount").toString();
			cabinCopy.setCabinQuantity(cabinCount);
			// 获取segs节点
			Map<String, Object> segs = (Map<String, Object>) subitemMap
					.get("segs");
			StringBuilder flightNoSb = new StringBuilder();
			StringBuilder flightRefSb = new StringBuilder();
			for (int index = 0; index < segs.size(); index++) {
				// 获取0n节点
				// 构造节点名
				String node = "0" + index;
				Map<String, Object> nodeMap = (Map<String, Object>) segs
						.get(node);
				if ("00".equals(node)) {
					flightNoSb.append(nodeMap.get("flightNo"));
					flightRefSb.append(nodeMap.get("flightRef"));
				} else {
					flightNoSb.append("-").append(nodeMap.get("flightNo"));
					flightRefSb.append("-").append(nodeMap.get("flightRef"));
				}
			}
			oriAirDataCopy.setFlightNos(flightNoSb.toString());
			List<Cabin> cabins = new ArrayList<Cabin>();
			cabins.add(cabinCopy);
			oriAirDataCopy.setCabins(cabins);
			String flightRef = flightRefSb.toString();
			String[] flightRefs = flightRef.split("-");
			Map<String, Object> flightRefMap1 = (Map<String, Object>) flightMap
					.get(flightRefs[0]);
			Map<String, Object> flightRefMap2 = (Map<String, Object>) flightMap
					.get(flightRefs[flightRefs.length - 1]);
			oriAirDataCopy.setDepCode((String) flightRefMap1.get("depCity"));
			oriAirDataCopy.setArrCode((String) flightRefMap2.get("arrCity"));
			oriAirDataCopy.setDepDate(ParseUtil.getDateStr(new Date((long) flightRefMap1.get("depTime")),"yyyy-MM-dd HH:mm"));
			oriAirDatas.add(oriAirDataCopy);

		}

		return oriAirDatas;
	}

}
