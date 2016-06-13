package com.example.week7.domain;

import java.io.Serializable;

/**
 * 失物招领实体类
 * 
 * @author Administrator
 * 
 */
public class Lostfound implements Serializable {
	private String id;// 主键
	private String uid;// 用户id
	private String content;// 内容
	private String time;// 发布时间
	private String img;// 图像
	private String tel;// 联系电话
	private String name;// 联系人姓名
	private String sid;// 学校id
	private String photo;// 发布人照片
	private String phone;// 发布人账号
	private String nickname;// 发布人昵称

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Lostfound(String id, String uid, String content, String time,
			String img, String tel, String name, String sid, String photo,
			String phone, String nickname) {
		super();
		this.id = id;
		this.uid = uid;
		this.content = content;
		this.time = time;
		this.img = img;
		this.tel = tel;
		this.name = name;
		this.sid = sid;
		this.photo = photo;
		this.phone = phone;
		this.nickname = nickname;
	}

}
