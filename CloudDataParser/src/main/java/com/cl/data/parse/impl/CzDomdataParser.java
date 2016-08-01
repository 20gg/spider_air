package com.cl.data.parse.impl;

import java.text.ParseException;
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
 * CZ机票(国内－直达机票)解析器
 * <p>
 * 注：中转机票解析方式不一样,请求方式也不一样
 * </p>
 * 
 * @author chenlei
 *
 */
public class CzDomdataParser extends DBdataParser {

	private final static Log logger = LogFactory.getLog(CzDomdataParser.class);

	public CzDomdataParser() {
		super();
	}

	/**
	 * jackson树模式解析
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<OriAirData> parse() {
		// 通用原始机票数据list集合
		List<OriAirData> oriAirDatas = super.parse();
		// 获取data节点的map
		Map<String, Object> data = (Map<String, Object>) rootMap.get("segment");
		if (data == null || data.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		// 获取出发/到达地
		String depcity = (String) data.get("depcity");
		String arrcity = (String) data.get("arrcity");
		// 提取dateflight节点
		Map<String, Object> dateflight = (Map<String, Object>) data
				.get("dateflight");
		// 提取flight数组节点
		List<Map<String, Object>> flight = (List<Map<String, Object>>) dateflight
				.get("flight");
		OriAirData oriAirData = new OriAirData();
		Cabin cabin = new Cabin();
		// 遍历flight数组,提取机票数据
		Iterator<Map<String, Object>> ite = flight.iterator();
		try {
			while (ite.hasNext()) {
				Map<String, Object> flightMap = ite.next();
				OriAirData oriAirDataCopy = oriAirData.clone();
				// 提取出发日期和时间
				String depdate = (String) flightMap.get("depdate");
				String deptime = (String) flightMap.get("deptime");
				Date time = ParseUtil
						.getDate(depdate + deptime, "yyyyMMddHHmm");
				String timeStr = ParseUtil.getDateStr(time, "yyyy-MM-dd HH:mm");
				oriAirDataCopy.setDepDate(timeStr);
				// 提取航班号
				String flightno = (String) flightMap.get("flightno");
				oriAirDataCopy.setFlightNos(flightno);
				// 设置出发.到达地
				oriAirDataCopy.setDepCode(depcity);
				oriAirDataCopy.setArrCode(arrcity);
				// 提取cabin数组节点
				List<Map<String, Object>> cabinlist = (List<Map<String, Object>>) flightMap
						.get("cabin");
				// 遍历cabin数组
				List<Cabin> cabins = new ArrayList<Cabin>();
				for (Map<String, Object> cabinMap : cabinlist) {
					Cabin cabinCpoy = cabin.clone();
					String adultfarebasis = (String) cabinMap
							.get("adultfarebasis");
					String name = (String) cabinMap.get("adultfarebasis");
					String adultprice = (String) cabinMap.get("adultprice");
					String info = (String) cabinMap.get("info");
					cabinCpoy.setCabinCode(name);
					cabinCpoy.setCabinName(adultfarebasis);
					cabinCpoy.setCabinQuantity(info);
					cabinCpoy.setPrice(Integer.valueOf(adultprice));
					cabins.add(cabinCpoy);
				}
				oriAirDataCopy.setCabins(cabins);
				oriAirDatas.add(oriAirDataCopy);
			}
		} catch (NumberFormatException e) {
			logger.error("", e);
		} catch (CloneNotSupportedException e) {
			logger.error("", e);
		} catch (ParseException e) {
			logger.error("", e);
		}
		return oriAirDatas;
	}

}
