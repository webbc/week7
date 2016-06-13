package com.example.week7.domain;

/**
 * 原著微博内容
 * 
 * @author Administrator
 * 
 */
public class FromPark {

	public FromPark(String content, String userphone, String username) {
		super();
		this.content = content;
		this.userphone = userphone;
		this.username = username;
	}

	private String content;// 被转发的微博内容
	private String userphone;// 被转发的微博用户电话
	private String username;// 被转发的微博用户名称

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
