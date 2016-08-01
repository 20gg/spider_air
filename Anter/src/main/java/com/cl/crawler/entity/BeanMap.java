package com.cl.crawler.entity;

/**
 * springBean映射
 * 
 * @author chenlei
 *
 */
public class BeanMap implements Cloneable {
	// 域名
	private String domain;
	// 境内/境外
	private String inter;
	// SpringBeanName;
	private String bean;

	public BeanMap() {
		super();
	}

	public BeanMap(String domain, String inter, String bean) {
		super();
		this.domain = domain;
		this.inter = inter;
		this.bean = bean;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getInter() {
		return inter;
	}

	public void setInter(String inter) {
		this.inter = inter;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	@Override
	public String toString() {
		return "BeanMap [domain=" + domain + ", inter=" + inter + ", bean="
				+ bean + "]";
	}

	@Override
	public BeanMap clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BeanMap) super.clone();
		
	}

}
