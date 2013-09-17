package com.aijuts.cx100.entity;

import java.util.List;

public class Purpose {
	
	private int total;
	
	private String success;
	
	private List<PurposeData> data;

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

	public List<PurposeData> getData() {
		return data;
	}

	public void setData(List<PurposeData> data) {
		this.data = data;
	}

}
