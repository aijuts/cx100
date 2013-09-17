package com.aijuts.cx100.data.entity;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="UserLogin")
public class UserLogin {
	
	private int id;
	
	private String userid;
	
	private String siteid;
	
	private String account;
	
	private String mailaccount;
	
	private String mobileaccount;
	
	private String nameaccount;
	
	private String pwd;
	
	private String v_pwd;
	
	private String type;
	
	private String regtime;
	
	private String name;
	
	private String state;
	
	private String isdelete;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMailaccount() {
		return mailaccount;
	}

	public void setMailaccount(String mailaccount) {
		this.mailaccount = mailaccount;
	}

	public String getMobileaccount() {
		return mobileaccount;
	}

	public void setMobileaccount(String mobileaccount) {
		this.mobileaccount = mobileaccount;
	}

	public String getNameaccount() {
		return nameaccount;
	}

	public void setNameaccount(String nameaccount) {
		this.nameaccount = nameaccount;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getV_pwd() {
		return v_pwd;
	}

	public void setV_pwd(String v_pwd) {
		this.v_pwd = v_pwd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRegtime() {
		return regtime;
	}

	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

}
