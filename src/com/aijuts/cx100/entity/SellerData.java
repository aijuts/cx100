package com.aijuts.cx100.entity;

import java.util.List;

public class SellerData {
	
	private String userid;
	
	private String image;
	
	private String name;
	
	private String evaluate;
	
	private String percapita;
	
	private String addr;
	
	private List<SellerCuisines> cuisines;
	
	private String latitude;
	
	private String longitude;
	
	private String distance;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	public String getPercapita() {
		return percapita;
	}

	public void setPercapita(String percapita) {
		this.percapita = percapita;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public List<SellerCuisines> getCuisines() {
		return cuisines;
	}

	public void setCuisines(List<SellerCuisines> cuisines) {
		this.cuisines = cuisines;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

}
