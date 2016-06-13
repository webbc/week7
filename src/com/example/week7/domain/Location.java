package com.example.week7.domain;

import java.io.Serializable;

/**
 * 区域的实体类
 * 
 * @author Administrator
 * 
 */
public class Location implements Serializable {
	private String province;
	private String city;
	private String address;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Location(String province, String city, String address) {
		super();
		this.province = province;
		this.city = city;
		this.address = address;
	}

}
