package com.example.week7.domain;

/**
 * 商品的实体类
 * 
 * @author Administrator
 * 
 */
public class Shop {
	private String id;
	private String logo;
	private String desc;
	private String price;
	private String hot;
	private String name;
	private String tel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getHot() {
		return hot;
	}

	public void setHot(String hot) {
		this.hot = hot;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Shop(String id, String logo, String desc, String price, String hot,
			String name, String tel) {
		super();
		this.id = id;
		this.logo = logo;
		this.desc = desc;
		this.price = price;
		this.hot = hot;
		this.name = name;
		this.tel = tel;
	}

}
