package com.cl.data.entity;

/**
 * json字符串的响应状态的kye - value封装
 * 
 * @author chenlei
 *
 */
public class DataCode {
	/** 响应状态名 */
	private String key;
	/** 响应状态值 */
	private String value;

	public DataCode() {
		super();
	}

	public DataCode(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "DataCode [key=" + key + ", value=" + value + "]";
	}

}
