package com.cl.data.parse.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cl.data.entity.ori.OriAirData;
import com.cl.data.entity.ori.OriAirData.Cabin;
import com.cl.data.parse.DBdataParser;

/**
 * Ali机票(国内)解析器
 * 
 * @author chenlei
 *
 */
public class AliDomdataParser extends DBdataParser {
	
	private final static Log logger = LogFactory.getLog(AliDomdataParser.class);

	public AliDomdataParser() {
		super();
	}

	/**
	 * 以树模式解析Ali国内机票数据
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
		// 抽取出发地,到达地信息
		String depCityCode = (String) data.get("depCityCode");
		String arrCityCode = (String) data.get("arrCityCode");
		// 获取flight数组节点
		List<Map<String, Object>> flight = (List<Map<String, Object>>) data
				.get("flight");
		// 遍历flight抽取机票数据
		OriAirData oriAirData = new OriAirData();
		Cabin cabin = new Cabin();
		Iterator<Map<String, Object>> flightite = flight.iterator();
		try {
			while (flightite.hasNext()) {
				Map<String, Object> flightMap = flightite.next();
				OriAirData oriAirDataCopy = oriAirData.clone();
				List<Cabin> cabins = new ArrayList<Cabin>();
				cabins.add(cabin.clone());
				oriAirDataCopy.setCabins(cabins);
				oriAirDataCopy.setDepCode(depCityCode);
				oriAirDataCopy.setArrCode(arrCityCode);
				// 获取原机票id
				String id = flightMap.get("id").toString();
				oriAirDataCopy.setId(id);
				// 获取原机票出发时间
				String depTime = (String) flightMap.get("depTime");
				oriAirDataCopy.setDepDate(depTime);
				// 获取原机票航班号组
				getOriAirData(oriAirDataCopy, flightMap, true);
				// 判断是否是中转机票
				Boolean isTransfer = (Boolean) flightMap.get("isTransfer");
				if (isTransfer != null && isTransfer) {
					// 抽取transferFlight数组节点
					List<Map<String, Object>> transferFlight = (List<Map<String, Object>>) flightMap
							.get("transferFlight");
					Iterator<Map<String, Object>> ite = transferFlight
							.iterator();
					while (ite.hasNext()) {
						Map<String, Object> transferFlightMap = ite.next();
						getOriAirData(oriAirDataCopy, transferFlightMap, false);
					}

				}
				oriAirDatas.add(oriAirDataCopy);

			}
		} catch (CloneNotSupportedException e) {
			logger.error("", e);
		}

		return oriAirDatas;
	}

	/**
	 * 设置机票值
	 * 
	 * @param oriAirData
	 * @param map
	 * @param isFirst
	 *            :是否是航班号一
	 */
	@SuppressWarnings("unchecked")
	private void getOriAirData(OriAirData oriAirData, Map<String, Object> map,
			Boolean isFirst) {
		String flightNo = (String) map.get("flightNo");
		Map<String, Object> cabinMap = (Map<String, Object>) map.get("cabin"); // 舱位信息
		String cabinCode = (String) cabinMap.get("cabin");
		Integer price = null;
		Object bestPriceObj = cabinMap.get("bestPrice"); // 判断是否有bestPrice;如有,bestPrice是最低价/若无,price是最低价
		if (bestPriceObj == null) {
			price = (Integer) cabinMap.get("price");
		} else {
			price = (Integer) bestPriceObj;
		}
		Cabin cabin = oriAirData.getCabins().get(0);
		if (isFirst) {
			oriAirData.setFlightNos(flightNo);
			cabin.setCabinCode(cabinCode);
			cabin.setCabinQuantity("A");
			cabin.setPrice(price);
		} else {
			StringBuilder sb = new StringBuilder(oriAirData.getFlightNos());
			sb.append("-").append(flightNo);
			oriAirData.setFlightNos(sb.toString());
			cabin.setPrice(price + cabin.getPrice());
		}

	}

}
