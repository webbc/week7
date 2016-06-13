package com.example.week7.domain;

/**
 * 新闻实体类
 * 
 * @author Administrator
 * 
 */
public class News {
	private String title;// 新闻标题
	private String url;// 新闻链接
	private String number;// 新闻点击量
	private String date;// 新闻发表日期

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public News(String title, String url, String number, String date) {
		super();
		this.title = title;
		this.url = url;
		this.number = number;
		this.date = date;
	}

}
