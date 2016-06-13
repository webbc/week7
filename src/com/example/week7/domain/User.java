package com.example.week7.domain;

import java.io.Serializable;

public class User implements Serializable {
	private int id;// id
	private String phone;// 电话
	private String password;// 密码
	private String email;// 邮箱
	private String nickname;// 昵称
	private String photo;// 图片连接
	private String score;// 积分
	private String rtime;// 注册时间
	private String ltime;// 最后登录时间
	private boolean isUseful;// 是否可用
	private String sex;// 性别
	private String room_id;// 寝室号
	private String province;// 省份
	private String city;// 城市
	private String address;// 我的详细地址
	private boolean isQiandao;// 是否已签到
	private String version;// 版本号
	private int sid;// 学校id

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getRtime() {
		return rtime;
	}

	public void setRtime(String rtime) {
		this.rtime = rtime;
	}

	public String getLtime() {
		return ltime;
	}

	public void setLtime(String ltime) {
		this.ltime = ltime;
	}

	public boolean isUseful() {
		return isUseful;
	}

	public void setUseful(boolean isUseful) {
		this.isUseful = isUseful;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getRoom_id() {
		return room_id;
	}

	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}

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

	public boolean isQiandao() {
		return isQiandao;
	}

	public void setQiandao(boolean isQiandao) {
		this.isQiandao = isQiandao;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", phone=" + phone + ", password=" + password
				+ ", email=" + email + ", nickname=" + nickname + ", photo="
				+ photo + ", score=" + score + ", rtime=" + rtime + ", ltime="
				+ ltime + ", isUseful=" + isUseful + ", sex=" + sex
				+ ", room_id=" + room_id + ", province=" + province + ", city="
				+ city + ", address=" + address + ", isQiandao=" + isQiandao
				+ ", version=" + version + ", sid=" + sid + "]";
	}

}
