/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import com.fujitsu.itLogs.online.model.DoorLog;

/**
 * @author r.monte@ph.fujitsu.com
 * 
 * @version 1.0.0
 * 
 *         History: Date(YYYY.MM.DD)|author|revision.
 * 
 *         2017.03.09 | y.umayam@ph.fujitsu.com | added named query for advance
 *         search.
 * 
 *         2017.03.16 | t.alba@ph.fujitsu.com | added named query for normal
 *         search.
 * 
 */
public interface DoorLogService {

	DoorLog save(DoorLog doorLog);

	List<DoorLog> findAll();

	DoorLog find(Long id);

	void delete(Long id);

	List<DoorLog> findByEmpId(Long id);

	List<DoorLog> findByEmpNo(String empNo);

	List<DoorLog> findByDate(Date fromDate, Date toDate, String empNo);

	List<DoorLog> findByPairDate(Date fromDate, Date toDate, String empNo);

	// DoorLog findByUsername(String username);
	List<DoorLog> findLateEntriesByEmployeeID(int employeeID, Date fromDate, Time fromTime, Date toDate);

	DoorLog findTimeSheetbyEmployeeID(int employeeID, Date date);

	List<Date> findEntriesWithoutWeekendsByEmployeeID(int employeeID, Date fromDate, Date toDate);

	List<Date> findEntriesByEmployeeID(int employeeID, Date fromDate, Date toDate);
	// List<Date> findDistinctDateByEmpNo(int empNo, Date fromDate, Date
	// toDate);
	// List<DoorLog> findPairEntry(int empId, Date date);
}
