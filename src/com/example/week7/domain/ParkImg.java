package com.example.week7.domain;

/**
 * Î¢²©Í¼Æ¬
 * 
 * @author Administrator
 * 
 */
public class ParkImg {
	private String id;
	private String park_id;
	private String img_url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPark_id() {
		return park_id;
	}

	public void setPark_id(String park_id) {
		this.park_id = park_id;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public ParkImg(String id, String park_id, String img_url) {
		super();
		this.id = id;
		this.park_id = park_id;
		this.img_url = img_url;
	}

}