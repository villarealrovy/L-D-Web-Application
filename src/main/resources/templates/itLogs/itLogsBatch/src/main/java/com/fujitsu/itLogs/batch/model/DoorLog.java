package com.fujitsu.itLogs.batch.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The persistent class for the door_log database table.
 *
 * @author r.monte
 */
@Entity
@Table(name = "door_log")
@NamedQuery(name = "DoorLog.findAll", query = "SELECT d FROM DoorLog d")
public class DoorLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Temporal(TemporalType.TIME)
	private Date time;

	private String area;

	private String floor;

	private String door;

	private String reader;

	@Column(name = "card_no")
	private String cardNo;

	private String company;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public DoorLog() {
	}

	public DoorLog(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getDoor() {
		return door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DoorLog [id=" + id + ", date=" + date + ", time=" + time + ", area=" + area + ", floor=" + floor
				+ ", door=" + door + ", reader=" + reader + ", cardNo=" + cardNo + ", company=" + company
				+ ", employee=" + employee + "]";
	}

}