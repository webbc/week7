package com.example.week7.domain;

/**
 * 评论实体类
 * 
 * @author Administrator
 * 
 */
public class ParkComment {
	private String user_id;// 用户id
	private String park_id;// 微博id
	private String content;// 评论内容
	private String addtime;// 评论时间
	private String photo;// 评论人头像
	private String nickname;// 评论人昵称

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPark_id() {
		return park_id;
	}

	public void setPark_id(String park_id) {
		this.park_id = park_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public ParkComment(String user_id, String park_id, String content,
			String addtime, String photo, String nickname) {
		super();
		this.user_id = user_id;
		this.park_id = park_id;
		this.content = content;
		this.addtime = addtime;
		this.photo = photo;
		this.nickname = nickname;
	}

}
