package com.example.week7.domain;

import java.io.Serializable;

/**
 * 配送员实体类
 * 
 * @author Administrator
 * 
 */
public class Deliveryman implements Serializable {
	private int id;
	private String name;
	private String phone;
	private int sid;
	private String photo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
