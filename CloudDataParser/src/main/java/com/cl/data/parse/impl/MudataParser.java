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
 * MU机票(国内/国际通用)解析器
 * 
 * @author chenlei
 *
 */
public class MudataParser extends DBdataParser {

	private final static Log logger = LogFactory.getLog(MudataParser.class);

	public MudataParser() {
		super();
	}

	/**
	 * 以树模式解析
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<OriAirData> parse() {
		// 通用原始机票数据list集合
		List<OriAirData> oriAirDatas = super.parse();
		// 获取airResultDto节点的map
		Map<String, Object> data = (Map<String, Object>) rootMap
				.get("airResultDto");
		if (data == null || data.isEmpty()) {
			return Collections.EMPTY_LIST;
		}		
		// 获取productUnits数组节点(机票数据)
		List<Map<String, Object>> productUnits = (List<Map<String, Object>>) data
				.get("productUnits");
		OriAirData oriAirData = new OriAirData();
		Cabin cabin = new Cabin();
		// 遍历productUnits提取数据
		Iterator<Map<String, Object>> ite = productUnits.iterator();
		try {
			while (ite.hasNext()) {
				// 原始机票对象
				OriAirData oriAirDataCopy = oriAirData.clone();
				Map<String, Object> map = ite.next();
				// 提取航班号数组
				String flightNoGroup = (String) map.get("flightNoGroup");
				oriAirDataCopy.setFlightNos(flightNoGroup);
				// 获取cabinInfo节点
				Map<String, Object> cabinInfo = (Map<String, Object>) map
						.get("cabinInfo");
				List<Cabin> cabins = new ArrayList<Cabin>();
				Cabin cabinCopy = cabin.clone();
				cabinCopy.setCabinCode((String) cabinInfo.get("cabinCode"));
				cabinCopy.setCabinName((String) cabinInfo.get("baseCabinCode"));
				cabinCopy.setCabinQuantity((String) cabinInfo
						.get("cabinStatusCode"));
				// 提取票价数组fareInfoView信息,数组起始位为成人票价
				List<Map<String, Object>> fareInfoView = (List<Map<String, Object>>) map
						.get("fareInfoView");
				Map<String, Object> fareInfoMap = fareInfoView.get(0);
				Map<String, Object> fareMap = (Map<String, Object>) fareInfoMap
						.get("fare");
				String priceStr = (String) fareMap.get("salePrice");
				cabinCopy.setPrice(Float.valueOf(priceStr).intValue());
				cabinCopy.setTax(0);
				cabins.add(cabinCopy);
				oriAirDataCopy.setCabins(cabins);
				// 提取oriDestOption数组节点,此节点包含出发地、到达地、出发时间信息
				List<Map<String, Object>> oriDestOption = (List<Map<String, Object>>) map
						.get("oriDestOption");
				Map<String, Object> oriMap = oriDestOption.get(0);
				// 提取flights数组节点
				List<Map<String, Object>> flights = (List<Map<String, Object>>) oriMap
						.get("flights");
				// 遍历flights数组
				for (int index = 0; index < flights.size(); index++) {
					Map<String, Object> flightMap = flights.get(index);
					// 提取departureDateTime节点
					String departureDateTime = (String) flightMap
							.get("departureDateTime");
					// 提取departureAirport节点
					Map<String, Object> departureAirport = (Map<String, Object>) flightMap
							.get("departureAirport");
					// 提取arrivalAirport节点
					Map<String, Object> arrivalAirport = (Map<String, Object>) flightMap
							.get("arrivalAirport");
					if (index == 0) {
						oriAirDataCopy.setDepDate(departureDateTime);
						String cityCode = (String) departureAirport
								.get("cityCode");
						oriAirDataCopy.setDepCode(cityCode);
					}
					String cityCode = (String) arrivalAirport.get("cityCode");
					oriAirDataCopy.setArrCode(cityCode);
				}

				oriAirDatas.add(oriAirDataCopy);

			}
		} catch (NumberFormatException e) {
			logger.error("", e);
		} catch (CloneNotSupportedException e) {
			logger.error("", e);
		}

		// 合并同一航班号下的舱位数据
		for (int i = 0; i < oriAirDatas.size() - 1; i++) {
			OriAirData orii = oriAirDatas.get(i);
			for (int j = oriAirDatas.size() - 1; j > i; j--) {
				OriAirData orij = oriAirDatas.get(j);
				if (orij.getFlightNos().equals(orii.getFlightNos())) {
					orii.getCabins().addAll(orij.getCabins());
					oriAirDatas.remove(j);
				}
			}

		}

		return oriAirDatas;

	}

}
