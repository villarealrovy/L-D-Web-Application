/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author y.umayam@ph.fujitsu.com
 *
 * @version 1.0.0
 * 
 */
public class DoorLogEntriesDetail {

	private String dayofTheWeek;
	private String dayWithNoEntry;

	// key = date; value = time in and time out
	private Map<String, List<String>> daysWithLateEntries = new HashMap<>();

	public String getDayofTheWeek() {
		return dayofTheWeek;
	}

	public void setDayofTheWeek(String dayofTheWeek) {
		this.dayofTheWeek = dayofTheWeek;
	}

	public String getDayWithNoEntry() {
		return dayWithNoEntry;
	}

	public void setDayWithNoEntry(String dayWithNoEntry) {
		this.dayWithNoEntry = dayWithNoEntry;
	}

	public Map<String, List<String>> getDaysWithLateEntries() {
		return daysWithLateEntries;
	}

	public void setDaysWithLateEntries(Map<String, List<String>> daysWithLateEntries) {
		this.daysWithLateEntries = daysWithLateEntries;
	}

}
