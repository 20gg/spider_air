package com.cl.crawler.entity;

/**
 * 简单的机票参数实体
 * 
 * @author chenlei
 *
 */
public class AirPoJo implements Iair, Cloneable {
	// 境内/境外
	private String inter;
	// 出发地三字码
	private String depCode;
	// 到达地三字码
	private String arrCode;
	// 出发日期
	private String depDate;

	public AirPoJo() {
		super();
	}

	public AirPoJo(String inter, String depCode, String arrCode, String depDate) {
		super();
		this.inter = inter;
		this.depCode = depCode;
		this.arrCode = arrCode;
		this.depDate = depDate;
	}

	public String getInter() {
		return inter;
	}

	public void setInter(String inter) {
		this.inter = inter;
	}

	public String getDepCode() {
		return depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	public String getArrCode() {
		return arrCode;
	}

	public void setArrCode(String arrCode) {
		this.arrCode = arrCode;
	}

	public String getDepDate() {
		return depDate;
	}

	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}

	@Override
	public AirPoJo clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (AirPoJo) super.clone();
	}

}
