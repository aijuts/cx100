package com.aijuts.cx100.entity;

import java.util.List;

public class SellerDetailData {
	
	private String userid;
	
	private String name;
	
	private List<String> image;
	
	private int taste;
	
	private int environment;
	
	private int service;
	
	private String perCapita;
	
	private List<SellerCuisines> cuisines;
	
	private String businesshours;
	
	private List<PurposeData> purpose;
	
	private String tel;
	
	private String addr;
	
	private String info;
	
	private String point;
	
	private List<Cateclassify> cateclassify;
	
	private List<Notice> notice;
	
	private List<Order> order;
	
	private List<Reviews> reviews;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
		this.image = image;
	}

	public int getTaste() {
		return taste;
	}

	public void setTaste(int taste) {
		this.taste = taste;
	}

	public int getEnvironment() {
		return environment;
	}

	public void setEnvironment(int environment) {
		this.environment = environment;
	}

	public int getService() {
		return service;
	}

	public void setService(int service) {
		this.service = service;
	}

	public String getPerCapita() {
		return perCapita;
	}

	public void setPerCapita(String perCapita) {
		this.perCapita = perCapita;
	}

	public List<SellerCuisines> getCuisines() {
		return cuisines;
	}

	public void setCuisines(List<SellerCuisines> cuisines) {
		this.cuisines = cuisines;
	}

	public String getBusinesshours() {
		return businesshours;
	}

	public void setBusinesshours(String businesshours) {
		this.businesshours = businesshours;
	}

	public List<PurposeData> getPurpose() {
		return purpose;
	}

	public void setPurpose(List<PurposeData> purpose) {
		this.purpose = purpose;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public List<Cateclassify> getCateclassify() {
		return cateclassify;
	}

	public void setCateclassify(List<Cateclassify> cateclassify) {
		this.cateclassify = cateclassify;
	}

	public List<Notice> getNotice() {
		return notice;
	}

	public void setNotice(List<Notice> notice) {
		this.notice = notice;
	}

	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

	public List<Reviews> getReviews() {
		return reviews;
	}

	public void setReviews(List<Reviews> reviews) {
		this.reviews = reviews;
	}

}
