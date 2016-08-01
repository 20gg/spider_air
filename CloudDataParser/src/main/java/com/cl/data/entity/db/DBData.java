package com.cl.data.entity.db;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 机票最低价数据(入库)
 * 
 * @author chenlei
 *
 */
@XmlRootElement(name = "DBData")
@XmlAccessorType(XmlAccessType.FIELD)
public class DBData implements Cloneable, Serializable {
	private static final long serialVersionUID = 63327206710735369L;

	/** id数据库维护 */
	private Integer id;
	/** 机票数据来源 */
	private String source;
	// 航班号组
	private String flightNos;
	// 出发日期
	private String dt;
	// 出发地
	private String fromAirport;
	// 到达地
	private String toAirport;
	// 最低票价
	private Integer loweastPrice;
	// 税费
	private Integer tax;
	// 剩余票数
	private String quanty;
	// 生成时间
	private long birthtime;

	public DBData() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getFlightNos() {
		return flightNos;
	}

	public void setFlightNos(String flightNos) {
		this.flightNos = flightNos;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public String getFromAirport() {
		return fromAirport;
	}

	public void setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
	}

	public String getToAirport() {
		return toAirport;
	}

	public void setToAirport(String toAirport) {
		this.toAirport = toAirport;
	}

	public Integer getLoweastPrice() {
		return loweastPrice;
	}

	public void setLoweastPrice(Integer loweastPrice) {
		this.loweastPrice = loweastPrice;
	}

	public Integer getTax() {
		return tax;
	}

	public void setTax(Integer tax) {
		this.tax = tax;
	}

	public String getQuanty() {
		return quanty;
	}

	public void setQuanty(String quanty) {
		this.quanty = quanty;
	}

	public long getBirthtime() {
		return birthtime;
	}

	public void setBirthtime(long birthtime) {
		this.birthtime = birthtime;
	}

	@Override
	public DBData clone() throws CloneNotSupportedException {
		return (DBData) super.clone();
	}

}
