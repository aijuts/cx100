package com.aijuts.cx100.entity;

import java.util.List;

public class Businesszone {
	
	private int total;
	
	private String success;
	
	private List<BusinesszoneData> data;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public List<BusinesszoneData> getData() {
		return data;
	}

	public void setData(List<BusinesszoneData> data) {
		this.data = data;
	}

}
