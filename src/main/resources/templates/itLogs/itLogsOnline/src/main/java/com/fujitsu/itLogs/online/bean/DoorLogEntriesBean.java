/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author y.umayam@ph.fujitsu.com
 *
 * @version 1.0.0
 * 
 */
public class DoorLogEntriesBean {

	private String monthYear;
	private int daysCount;
	private long totalMinutes;
	private List<DoorLogEntriesDetail> doorLogEntriesDetails = new ArrayList<>();

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public int getDaysCount() {
		return daysCount;
	}

	public void setDaysCount(int daysCount) {
		this.daysCount = daysCount;
	}

	public long getTotalMinutes() {
		return totalMinutes;
	}

	public void setTotalMinutes(long totalMinutes) {
		this.totalMinutes = totalMinutes;
	}

	public List<DoorLogEntriesDetail> getDoorLogEntriesDetails() {
		return doorLogEntriesDetails;
	}

	public void setDoorLogEntriesDetails(List<DoorLogEntriesDetail> doorLogEntriesDetails) {
		this.doorLogEntriesDetails = doorLogEntriesDetails;
	}

}
