package com.cl.data.entity.ori;

import java.io.Serializable;
import java.util.List;

/**
 * 通用原始机票数据
 * 
 * @author chenlei
 *
 */
public class OriAirData implements Cloneable, Serializable {

	private static final long serialVersionUID = -3829636039061155698L;

	// id
	private String id;
	// 航班号组
	private String flightNos;
	// 出发地
	private String depCode;
	// 到达地
	private String arrCode;
	// 出发时间
	private String depDate;
	// 中转次数
	private Integer transCount;
	// 舱位信息
	private List<Cabin> cabins;

	public OriAirData() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlightNos() {
		return flightNos;
	}

	public void setFlightNos(String flightNos) {
		this.flightNos = flightNos;
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

	public Integer getTransCount() {
		return transCount;
	}

	public void setTransCount(Integer transCount) {
		this.transCount = transCount;
	}

	public List<Cabin> getCabins() {
		return cabins;
	}

	public void setCabins(List<Cabin> cabins) {
		this.cabins = cabins;
	}

	@Override
	public OriAirData clone() throws CloneNotSupportedException {
		return (OriAirData) super.clone();
	}

	@Override
	public String toString() {
		return "OriAirData [id=" + id + ", flightNos=" + flightNos
				+ ", depCode=" + depCode + ", arrCode=" + arrCode
				+ ", depDate=" + depDate + ", transCount=" + transCount
				+ ", cabins=" + cabins + "]";
	}

	/**
	 * 舱位信息
	 * 
	 * @author chenlei
	 *
	 */
	public static class Cabin implements Cloneable, Serializable {

		private static final long serialVersionUID = 4400287872642981962L;

		// 舱位描述
		private String cabinName;
		// 舱位等级代码
		private String cabinCode;
		// 舱位剩余数
		private String cabinQuantity;
		// 舱位价格
		private Integer price;
		// 税费
		private Integer tax;

		public Cabin() {
			super();
		}

		public String getCabinName() {
			return cabinName;
		}

		public void setCabinName(String cabinName) {
			this.cabinName = cabinName;
		}

		public String getCabinCode() {
			return cabinCode;
		}

		public void setCabinCode(String cabinCode) {
			this.cabinCode = cabinCode;
		}

		public String getCabinQuantity() {
			return cabinQuantity;
		}

		public void setCabinQuantity(String cabinQuantity) {
			this.cabinQuantity = cabinQuantity;
		}

		public Integer getPrice() {
			return price;
		}

		public void setPrice(Integer price) {
			this.price = price;
		}

		public Integer getTax() {
			return tax;
		}

		public void setTax(Integer tax) {
			this.tax = tax;
		}

		@Override
		public Cabin clone() throws CloneNotSupportedException {
			return (Cabin) super.clone();
		}

		@Override
		public String toString() {
			return "Cabin [cabinName=" + cabinName + ", cabinCode=" + cabinCode
					+ ", cabinQuantity=" + cabinQuantity + ", price=" + price
					+ ", tax=" + tax + "]";
		}

	}

}
