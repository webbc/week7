package com.example.week7.domain;

/**
 * 兼职信息
 * 
 * @author Administrator
 * 
 */
public class Parttime {
	private int id;// 主键
	private String title;// 标题
	private int column;// 兼职分类
	private String img;// 兼职图片
	private int istop;// 是否置顶
	private String type;// 兼职分类
	private String content;// 兼职内容
	private int state;// 状态
	private String area;// 兼职区域
	private String company;// 招聘公司
	private String gender;// 性别要求
	private String treatment;// 工资待遇
	private int person_count;// 可报名人数
	private String stime;// 开始时间
	private String etime;// 截止时间
	private String worktime;// 工作时间
	private String addtime;// 文章添加时间
	private String phone;// 联系电话
	private String qq;// 练习电话
	private String remarks;// 备注
	private String comment_count;// 评论数量
	private int apply_count;// 已报名人数

	public Parttime(int id, String title, int column, String img, int istop,
			String type, String content, int state, String area,
			String company, String gender, String treatment, int person_count,
			String stime, String etime, String worktime, String addtime,
			String phone, String qq, String remarks, String comment_count,
			int apply_count) {
		super();
		this.id = id;
		this.title = title;
		this.column = column;
		this.img = img;
		this.istop = istop;
		this.type = type;
		this.content = content;
		this.state = state;
		this.area = area;
		this.company = company;
		this.gender = gender;
		this.treatment = treatment;
		this.person_count = person_count;
		this.stime = stime;
		this.etime = etime;
		this.worktime = worktime;
		this.addtime = addtime;
		this.phone = phone;
		this.qq = qq;
		this.remarks = remarks;
		this.comment_count = comment_count;
		this.apply_count = apply_count;
	}

	public int getApply_count() {
		return apply_count;
	}

	public void setApply_count(int apply_count) {
		this.apply_count = apply_count;
	}

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

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getIstop() {
		return istop;
	}

	public void setIstop(int istop) {
		this.istop = istop;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public int getPerson_count() {
		return person_count;
	}

	public void setPerson_count(int person_count) {
		this.person_count = person_count;
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

	public String getWorktime() {
		return worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

}
