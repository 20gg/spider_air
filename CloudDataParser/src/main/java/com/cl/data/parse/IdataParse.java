package com.cl.data.parse;

/**
 * 数据抽取器接口
 * 
 * @author chenlei
 *
 */
public interface IdataParse {

	/**
	 * 数据解析方法
	 * 
	 * @param taskId
	 *            待解析数据所属的任务的任务id
	 * @return checkCode 状态码
	 */
	public int parseData(String taskId);

	/**
	 * 注入要被解析的原始数据
	 * 
	 * @param dataStr
	 */
	public void setDataStr(String dataStr);

}
