package com.example.week7.domain;

/**
 * 焦点图的实体类
 * 
 * @author Administrator
 * 
 */
public class Focus {
	private int id;
	private String title;
	private String img;
	private String addtime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public Focus(int id, String title, String img, String addtime) {
		super();
		this.id = id;
		this.title = title;
		this.img = img;
		this.addtime = addtime;
	}

}
