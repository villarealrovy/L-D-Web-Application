package com.example.demo.modal;

import java.io.Serializable;

public class employee implements Serializable {

	
	private String username;
	private String system_role;
	private String full_name;
	private String gender;
	private String email_account;
	private String gdc_name;
	private String department;
	private String division;
	private String LOS;
	
	public employee() {
		
	}
	
	public employee(String username, String system_role, String full_name, String gender, String email_account,
			String gdc_name, String department, String division, String lOS) {
		super();
		this.username = username;
		this.system_role = system_role;
		this.full_name = full_name;
		this.gender = gender;
		this.email_account = email_account;
		this.gdc_name = gdc_name;
		this.department = department;
		this.division = division;
		LOS = lOS;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSystem_role() {
		return system_role;
	}

	public void setSystem_role(String system_role) {
		this.system_role = system_role;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail_account() {
		return email_account;
	}

	public void setEmail_account(String email_account) {
		this.email_account = email_account;
	}

	public String getGdc_name() {
		return gdc_name;
	}

	public void setGdc_name(String gdc_name) {
		this.gdc_name = gdc_name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getLOS() {
		return LOS;
	}

	public void setLOS(String lOS) {
		LOS = lOS;
	}

	@Override
	public String toString() {
		return "employee [username=" + username + ", system_role=" + system_role + ", full_name=" + full_name
				+ ", gender=" + gender + ", email_account=" + email_account + ", gdc_name=" + gdc_name + ", department="
				+ department + ", division=" + division + ", LOS=" + LOS + "]";
	}
	
	
	
	
	
}
