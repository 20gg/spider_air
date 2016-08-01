package com.cl.data.check;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cl.data.algorithm.CommonJsonAlgorithm;
import com.cl.data.entity.DataCode;
import com.cl.data.entity.IdataCode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 机票json数据检测
 * 
 * @author chenlei
 *
 */
public class JsonAirDataCheck implements IdataCheck {

	private static Log logger = LogFactory.getLog(JsonAirDataCheck.class);

	/** jackson 解析器对象 */
	private ObjectMapper objectMapper;
	/** 目标响应码－－键值映射(类型,键值封装) */
	private Map<String, DataCode> targetCodeMap;
	/** */
	private CommonJsonAlgorithm algorithm;
	/** json转换后的map集 */
	private Map<String, Object> rootMap;

	public JsonAirDataCheck() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int checkData(String datastr) {
		int statusCode = IdataCode.JOSN_201;
		try {
			rootMap = objectMapper.readValue(datastr, Map.class);
		} catch (JsonParseException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (rootMap == null) {
				statusCode = IdataCode.JSON_501;
			} else if (targetCodeMap == null || targetCodeMap.isEmpty()) {
				statusCode = IdataCode.JOSN_201;
			} else {
				Set<Entry<String, DataCode>> set = targetCodeMap.entrySet();
				Iterator<Entry<String, DataCode>> ite = set.iterator();
				OK: 
				while (ite.hasNext()) {
					Entry<String, DataCode> entry = ite.next();
					DataCode dataCode = entry.getValue();
					Object obj = algorithm.computeTargetNode(rootMap,
							dataCode.getKey());
					if (obj == null) {
						continue;
					}
					String objCode = obj.toString().replaceAll("\"", "");
					String value = dataCode.getValue();
					String[] values = value.split(",");
					for (String codeValue : values) {
						if (objCode.equals(codeValue)) {
							switch (entry.getKey()) {
							case "SUCCESS":
								statusCode = IdataCode.JOSN_201;
								break OK;
							case "FAIL":
								statusCode = IdataCode.JSON_401;
								break OK;
							case "LOGON":
								statusCode = IdataCode.JSON_301;
								break OK;
							case "VERICODE":
								statusCode = IdataCode.JSON_302;
								break OK;
							default:
								statusCode = IdataCode.JOSN_201;
								break OK;
							}
						}
					}
				}
			}
		}

		return statusCode;

	}

	/**
	 * 注入
	 * 
	 * @param objectMapper
	 */
	@Override
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * 注入
	 * 
	 * @param targetCodeMap
	 */
	@Override
	public void setTargetCodeMap(Map<String, DataCode> targetCodeMap) {
		this.targetCodeMap = targetCodeMap;
	}

	/**
	 * 注入
	 * 
	 * @param algorithm
	 */
	@Override
	public void setAlgorithm(CommonJsonAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * 获取json转换后的map集
	 * 
	 * @return
	 */
	public Map<String, Object> getRootMap() {
		return rootMap;
	}

}
