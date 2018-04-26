package com.example.demo.modal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class Rights {

	@Id
	@GeneratedValue
	private int employeeID;
	private String email;
	private int user_rights;
	
	public Rights() {
		
	}

	public Rights(int employeeID, String email, int user_rights) {
		super();
		this.employeeID = employeeID;
		this.email = email;
		this.user_rights = user_rights;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUser_rights() {
		return user_rights;
	}

	public void setUser_rights(int user_rights) {
		this.user_rights = user_rights;
	}


	@Override
	public String toString() {
		return "Rights [email=" + email + ", employeeID=" + employeeID + ", rights=" + user_rights + "]";
	}
	
	
	
	
	
}
