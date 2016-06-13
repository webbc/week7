package com.example.week7.domain;

/**
 * 学校实体类
 * 
 * @author Administrator
 * 
 */
public class School {
	private int id;// id
	private String name;// 学校名称
	private String code;// 学校编码
	private int sid;// 学校内区域所属的学校代码

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public School(int id, String name, String code, int sid) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.sid = sid;
	}

}
