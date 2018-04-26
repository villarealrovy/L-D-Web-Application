package com.fujitsu.itLogs.batch.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the company database table.
 *
 * @author r.monte
 */
@Entity
@NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c")
public class Company implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "gdc_name")
	private String gdcName;

	private String department;

	private String division;

	private String los;

	public Company() {
	}

	public Company(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	/**
	 * @return the gdcName
	 */
	public String getGdcName() {
		return gdcName;
	}

	/**
	 * @param gdcName the gdcName to set
	 */
	public void setGdcName(String gdcName) {
		this.gdcName = gdcName;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the division
	 */
	public String getDivision() {
		return division;
	}

	/**
	 * @param division the division to set
	 */
	public void setDivision(String division) {
		this.division = division;
	}

	/**
	 * @return the los
	 */
	public String getLos() {
		return los;
	}

	/**
	 * @param los the los to set
	 */
	public void setLos(String los) {
		this.los = los;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Company [id=" + id + ", gdcName=" + gdcName + ", department=" + department + ", division=" + division
				+ ", los=" + los + "]";
	}

}
