package com.aijuts.cx100.data.entity;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="AppSetting")
public class AppSetting {
	
	private int id;
	
	private int userCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

}
