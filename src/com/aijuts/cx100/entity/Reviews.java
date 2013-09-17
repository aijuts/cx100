package com.aijuts.cx100.entity;

import java.util.List;

public class Reviews {
	
	private String revid;
	
	private String userid;
	
	private String name;
	
	private String dir;
	
	private String taste;
	
	private String environment;
	
	private String service;
	
	private String message;
	
	private String capita;
	
	private String special;
	
	private String likefood;
	
	private String spendingtime;
	
	private String addtime;
	
	private List<Replymsg> replymsg;

	public String getRevid() {
		return revid;
	}

	public void setRevid(String revid) {
		this.revid = revid;
	}

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

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getTaste() {
		return taste;
	}

	public void setTaste(String taste) {
		this.taste = taste;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCapita() {
		return capita;
	}

	public void setCapita(String capita) {
		this.capita = capita;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getLikefood() {
		return likefood;
	}

	public void setLikefood(String likefood) {
		this.likefood = likefood;
	}

	public String getSpendingtime() {
		return spendingtime;
	}

	public void setSpendingtime(String spendingtime) {
		this.spendingtime = spendingtime;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public List<Replymsg> getReplymsg() {
		return replymsg;
	}

	public void setReplymsg(List<Replymsg> replymsg) {
		this.replymsg = replymsg;
	}

}
