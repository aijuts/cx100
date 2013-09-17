package com.aijuts.cx100.entity;

import java.util.List;

public class Logeinfo {
	
	private int total;
	
	private String success;
	
	private List<LogeinfoData> data;

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

	public List<LogeinfoData> getData() {
		return data;
	}

	public void setData(List<LogeinfoData> data) {
		this.data = data;
	}

}
