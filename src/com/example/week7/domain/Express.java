package com.example.week7.domain;

import java.io.Serializable;

/**
 * 快递实体类
 * 
 * @author Administrator
 * 
 */
public class Express implements Serializable {
	private int id;// id
	private int uid;// 用户id
	private int did;// 配送员id
	private String order_code;// 订单编号
	private String name;// 下单人姓名
	private String phone;// 下单人电话
	private String address;// 下单人详细地址
	private int aid;// 下单人所在区域
	private double money;// 金额
	private String stime;// 订单提交时间
	private String etime;// 订单完成时间
	private int state;// 订单状态
	private String company;// 快递公司
	private String getnum;// 取货号
	private String arrivetime;// 到件时间
	private int paystyle;// 支付方式
	private int sid;// 学校id
	private String desc;// 备注
	private String esttime;// 预计送达时间
	private int large;// 快件大小
	private Deliveryman deliveryman;// 配送员

	public Deliveryman getDeliveryman() {
		return deliveryman;
	}

	public void setDeliveryman(Deliveryman deliveryman) {
		this.deliveryman = deliveryman;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getGetnum() {
		return getnum;
	}

	public void setGetnum(String getnum) {
		this.getnum = getnum;
	}

	public String getArrivetime() {
		return arrivetime;
	}

	public void setArrivetime(String arrivetime) {
		this.arrivetime = arrivetime;
	}

	public int getPaystyle() {
		return paystyle;
	}

	public void setPaystyle(int paystyle) {
		this.paystyle = paystyle;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getEsttime() {
		return esttime;
	}

	public void setEsttime(String esttime) {
		this.esttime = esttime;
	}

	public int getLarge() {
		return large;
	}

	public void setLarge(int large) {
		this.large = large;
	}

}
