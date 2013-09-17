package com.aijuts.cx100.entity;

public class OrderCommit {
	
	private int total;
	
	private String success;
	
	private Order data;

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

	public Order getData() {
		return data;
	}

	public void setData(Order data) {
		this.data = data;
	}

}
