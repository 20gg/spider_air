package com.cl.data.parse.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cl.data.entity.ori.OriAirData;
import com.cl.data.entity.ori.OriAirData.Cabin;
import com.cl.data.parse.DBdataParser;
/**
 * CZ机票(国际)解析器
 * 
 * @author chenlei
 *
 */
public class CzInterdataParser extends DBdataParser {

	private final static Log logger = LogFactory
			.getLog(CzInterdataParser.class);

	public CzInterdataParser() {
		super();
	}

	/**
	 * 以树模式解析CZ国际机票数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<OriAirData> parse() {
		// 通用原始机票数据list集合
		List<OriAirData> oriAirDatas = super.parse();
		// 获取data节点的map
		List<Map<String, Object>> segment = (List<Map<String, Object>>) rootMap
				.get("segment");
		if (segment == null || segment.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		Map<String, Object> data = segment.get(0);
		// 获取出发/到达地
		String depcity = (String) data.get("depcity");
		String arrcity = (String) data.get("arrcity");
		// 获取dateflight节点数组
		List<Map<String, Object>> dateflight = (List<Map<String, Object>>) data
				.get("dateflight");
		OriAirData oriAirData = new OriAirData();
		Cabin cabin = new Cabin();
		// 遍历dateflight数组获取机票数据
		Iterator<Map<String, Object>> dateflightite = dateflight.iterator();
		try {
			while (dateflightite.hasNext()) {
				OriAirData oriAirDataCopy = oriAirData.clone();
				oriAirDataCopy.setDepCode(depcity);
				oriAirDataCopy.setArrCode(arrcity);
				Map<String, Object> map = dateflightite.next();
				// 获取flight节点数组
				List<Map<String, Object>> flight = (List<Map<String, Object>>) map
						.get("flight");
				// 提取航班号组信息和出发时间,舱位数信息
				StringBuilder flightNoSb = new StringBuilder(); // 航班号组
				Map<String, Map<String, String>> cityCabinMap = new HashMap<String, Map<String, String>>(); // 出发城市-舱位等级码与剩余舱位数映射map
				for (int index = 0; index < flight.size(); index++) {
					String depport = (String) flight.get(index).get("depport");
					Map<String, String> cabinMap = new HashMap<String, String>(); // 舱位等级码与剩余舱位数映射map
					String bookingclassavails = (String) flight.get(index).get(
							"bookingclassavails");
					String[] classavails = bookingclassavails.split(",");
					for (String classavail : classavails) {
						String[] mapArr = classavail.split(":");
						cabinMap.put(mapArr[0], mapArr[1]);
					}
					cityCabinMap.put(depport, cabinMap);

					if (index == 0) {
						String deptime = (String) flight.get(index).get(
								"deptime");
						oriAirDataCopy.setDepDate(deptime.replace("T", " "));
						flightNoSb.append((String) flight.get(index).get(
								"flightNo"));
					} else {
						flightNoSb.append("-").append(
								(String) flight.get(index).get("flightNo"));
					}
				}
				oriAirDataCopy.setFlightNos(flightNoSb.toString());
				// 获取prices节点数组
				List<Map<String, Object>> prices = (List<Map<String, Object>>) map
						.get("prices");
				// 遍历prices提取票价信息
				List<Cabin> cabins = new ArrayList<Cabin>();
				for (int index = 0; index < prices.size(); index++) {
					Cabin cabinCopy = cabin.clone();
					Map<String, Object> priceMap = prices.get(index);
					String adultcn = (String) priceMap.get("adultcn");
					String adultprice = (String) priceMap.get("adultprice");
					String adultxt = (String) priceMap.get("adultxt");
					String adultyq = (String) priceMap.get("adultyq");
					cabinCopy.setPrice(Integer.valueOf(adultprice));
					cabinCopy.setTax(Integer.valueOf(adultcn) + Integer.valueOf(adultxt) + Integer.valueOf(adultyq));
					// 原机票舱位匹配
					List<Map<String, Object>> adultcabins = (List<Map<String, Object>>) priceMap
							.get("adultcabins");
					for (int i = 0; i < adultcabins.size(); i++) {
						Map<String, Object> adultcabin = adultcabins.get(i);
						String dep = (String) adultcabin.get("dep");
						String name = (String) adultcabin.get("name");
						String type = (String) adultcabin.get("type");
						String countstr = cityCabinMap.get(dep).get(name);
						if (i == 0) {
							cabinCopy.setCabinCode(name);
							cabinCopy.setCabinName(type);
							cabinCopy.setCabinQuantity(countstr);
						} else if (Integer.valueOf(countstr) < Integer
								.valueOf(cabinCopy.getCabinQuantity())) {
							cabinCopy.setCabinCode(name);
							cabinCopy.setCabinName(type);
							cabinCopy.setCabinQuantity(countstr);
						}
					}
					cabins.add(cabinCopy);
				}
				oriAirDataCopy.setCabins(cabins);
				oriAirDatas.add(oriAirDataCopy);
			}
		} catch (NumberFormatException e) {
			logger.error("", e);
		} catch (CloneNotSupportedException e) {
			logger.error("", e);
		}
		return oriAirDatas;

	}

}
