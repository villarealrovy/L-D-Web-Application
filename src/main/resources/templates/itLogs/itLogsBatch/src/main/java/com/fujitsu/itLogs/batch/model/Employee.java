package com.fujitsu.itLogs.batch.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the employee database table.
 *
 * @author r.monte
 */
@Entity
@NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e")
public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "emp_no")
	private String empNo;

	private String username;

	private String fullname;

	private String sys_role;
	
	private String gender;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	
	@Column(name = "email_account")
	private String emailAccount;

	public Employee() {
	}

	public Employee(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSys_role() {
		return sys_role;
	}

	public void setSys_role(String role) {
		this.sys_role = role;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	/**
	 * @return the company
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
	}

	/**
	 * @return the emailAccount
	 */
	public String getEmailAccount() {
		return emailAccount;
	}

	/**
	 * @param emailAccount the emailAccount to set
	 */
	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Employee [id=" + id + ", empNo=" + empNo + ", username=" + username + ", fullname=" + fullname
				+ ", sys_role=" + sys_role + ", company=" + company + "]";
	}


}
