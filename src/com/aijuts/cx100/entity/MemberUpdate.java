package com.aijuts.cx100.entity;

public class MemberUpdate {
	
	private int total;
	
	private String success;
	
	private MemberUpdateData data;

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

	public MemberUpdateData getData() {
		return data;
	}

	public void setData(MemberUpdateData data) {
		this.data = data;
	}

}
