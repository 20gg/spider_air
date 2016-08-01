package com.cl.data.parse;

import java.util.List;
import java.util.Map;

import com.cl.data.algorithm.CommonJsonAlgorithm;
import com.cl.data.check.IdataCheck;
import com.cl.data.entity.DataCode;
import com.cl.data.entity.IdataCode;
import com.cl.data.entity.ori.OriAirData;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 机票数据抽取模板
 * 
 * @author chenlei
 *
 */
public abstract class DataParser implements IdataParse {
	
	/** 待解析的原始数据 */
	protected String dataStr;
	/** 数据指定输出目录 */
	protected String outputdir = "";
	/** 数据检测工具对象 */
	protected IdataCheck dataCheck;
	/** jackson 解析器对象 */
	protected ObjectMapper objectMapper;
	/** 目标响应码－－键值映射(类型,键值封装) */
	protected Map<String, DataCode> targetCodeMap;
	/** 算法 */
	protected CommonJsonAlgorithm algorithm;
	/** 该任务所属的机票采集任务的任务id */
	protected String taskId;

	public DataParser() {
		super();
	}

	@Override
	public int parseData(String taskId) {
		this.taskId = taskId;
		int checkCode = 100;
		// 数据有效性检测器对象的准备工作
		dataCheck.setObjectMapper(objectMapper);
		dataCheck.setTargetCodeMap(targetCodeMap);
		dataCheck.setAlgorithm(algorithm);
		// 数据有效性检测器对象开始检测,并返回结果码
		int dataCode = dataCheck.checkData(getDataStr());
		// 根据结果码做出相应的处理
		if (IdataCode.JOSN_201 == dataCode) {
			checkCode = 100;
			/**
			 * 进入有效数据提取环节
			 */
			// 抽取机票数组,输出自定义的原始机票数据
			List<OriAirData> oridatas = parse();
			// 处理原始机票数据
			daAfter(oridatas);
		} else {
			switch (dataCode) {
			case IdataCode.JSON_401:
				checkCode = 100;
				break;
			case IdataCode.JSON_301:
				checkCode = 200;
				break;
			case IdataCode.JSON_302:
				checkCode = 300;
				break;
			case IdataCode.JSON_501:
				checkCode = 500;
				break;
			default:
				checkCode = 100;
				break;
			}
			/**
			 * 进入异常数据截取缓存环节(考虑是否需要随任务信息一起输出)
			 */

		}

		return checkCode;

	}

	/**
	 * 从机票数组抽取机票详细信息数据,并生成自定义的原始机票数据
	 * 
	 * @return
	 */
	protected abstract List<OriAirData> parse();

	/**
	 * 数据解析后的处理方法
	 * 
	 * @param oridatalist
	 */
	protected abstract void daAfter(List<OriAirData> oridatalist);

	/**
	 * 获取当前被注入的待解析的原始数据
	 * 
	 * @return
	 */
	public String getDataStr() {
		return dataStr;
	}

	@Override
	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;

	}

	/**
	 * spring注入数据输出目录
	 * 
	 * @param outputdir
	 */
	public void setOutputdir(String outputdir) {
		this.outputdir = outputdir;
	}

	/**
	 * spring注入
	 * 
	 * @param dataCheck
	 */
	public void setDataCheck(IdataCheck dataCheck) {
		this.dataCheck = dataCheck;
	}

	/**
	 * spring注入
	 * 
	 * @param objectMapper
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * spring注入
	 * 
	 * @param targetCodeMap
	 */
	public void setTargetCodeMap(Map<String, DataCode> targetCodeMap) {
		this.targetCodeMap = targetCodeMap;
	}

	/**
	 * spring注入
	 * 
	 * @param algorithm
	 */
	public void setAlgorithm(CommonJsonAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

}
