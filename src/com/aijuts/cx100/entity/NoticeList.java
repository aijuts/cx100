package com.aijuts.cx100.entity;

import java.util.List;

public class NoticeList {
	
	private int total;
	
	private String success;
	
	private List<Notice> data;

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

	public List<Notice> getData() {
		return data;
	}

	public void setData(List<Notice> data) {
		this.data = data;
	}

}
