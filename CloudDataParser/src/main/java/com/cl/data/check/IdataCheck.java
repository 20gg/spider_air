package com.cl.data.check;

import java.util.Map;

import com.cl.data.algorithm.CommonJsonAlgorithm;
import com.cl.data.entity.DataCode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 数据检测接口
 * 
 * @author chenlei
 *
 */
public interface IdataCheck {

	/**
	 * 检测数据有效性
	 * 
	 * @param datastr
	 * @return 机票数据状态码
	 */
	public int checkData(String datastr);

	/**
	 * 注入jackson解析器对象
	 * 
	 * @param objectMapper
	 */
	public void setObjectMapper(ObjectMapper objectMapper);

	/**
	 * 注入目标响应码map
	 * 
	 * @param targetCodeMap
	 */
	public void setTargetCodeMap(Map<String, DataCode> targetCodeMap);

	/**
	 * 注入算法
	 * 
	 * @param algorithm
	 */
	public void setAlgorithm(CommonJsonAlgorithm algorithm);

	/**
	 * 获取json转换后的map集
	 * 
	 * @return
	 */
	public Map<String, Object> getRootMap();

}
