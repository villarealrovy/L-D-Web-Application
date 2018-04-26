/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.service.impl;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fujitsu.itLogs.online.model.DoorLog;
import com.fujitsu.itLogs.online.repository.DoorLogRepository;
import com.fujitsu.itLogs.online.service.DoorLogService;

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
@Service
public class DoorLogServiceImpl implements DoorLogService {

	@Autowired
	private DoorLogRepository doorLogRepository;

	@Override
	public List<DoorLog> findAll() {
		Iterable<DoorLog> doorLogs = doorLogRepository.findAll();
		List<DoorLog> doorLogList = new ArrayList<>();
		for (DoorLog doorLog : doorLogs) {
			doorLogList.add(doorLog);
		}

		return doorLogList;
	}

	@Override
	public DoorLog save(DoorLog doorLog) {

		return doorLogRepository.save(doorLog);
	}

	@Override
	public DoorLog find(Long id) {

		return doorLogRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {

		doorLogRepository.delete(id);
	}

	@Override
	public List<DoorLog> findByEmpId(Long id) {

		Iterable<DoorLog> doorLogs = doorLogRepository.findByEmpId(id);
		List<DoorLog> doorLogList = new ArrayList<>();
		for (DoorLog doorLog : doorLogs) {
			doorLogList.add(doorLog);
		}

		return doorLogList;
	}

	@Override
	public List<DoorLog> findByEmpNo(String empNo) {

		Iterable<DoorLog> doorLogs = doorLogRepository.findByEmpNo(empNo);
		List<DoorLog> doorLogList = new ArrayList<>();
		for (DoorLog doorLog : doorLogs) {
			doorLogList.add(doorLog);
		}

		return doorLogList;
	}

	@Override
	public List<DoorLog> findByDate(Date fromDate, Date toDate, String empNo) {

		Iterable<DoorLog> doorLogs = doorLogRepository.findByDate(fromDate, toDate, empNo);
		List<DoorLog> doorLogList = new ArrayList<>();

		for (DoorLog doorLog : doorLogs) {
			doorLogList.add(doorLog);
		}

		return doorLogList;
	}

	@Override
	public List<DoorLog> findByPairDate(Date fromDate, Date toDate, String empNo) {

		Iterable<DoorLog> doorLogs = doorLogRepository.findByPairDate(fromDate, toDate, empNo);
		List<DoorLog> doorLogList = new ArrayList<>();

		for (DoorLog doorLog : doorLogs) {
			doorLogList.add(doorLog);
		}

		return doorLogList;
	}

	@Override
	public List<DoorLog> findLateEntriesByEmployeeID(int employeeID, Date fromDate, Time fromTime, Date toDate) {

		Iterable<DoorLog> doorLogs = doorLogRepository.findLateEntriesByEmployeeID(employeeID, fromDate, fromTime,
				toDate);
		List<DoorLog> doorLogList = new ArrayList<>();
		for (DoorLog doorLog : doorLogs) {
			doorLogList.add(doorLog);
		}

		return doorLogList;
	}

	@Override
	public DoorLog findTimeSheetbyEmployeeID(int employeeID, Date date) {

		DoorLog doorLog = doorLogRepository.findTimeSheetbyEmployeeID(employeeID, date);

		return doorLog;
	}

	@Override
	public List<Date> findEntriesWithoutWeekendsByEmployeeID(int employeeID, Date fromDate, Date toDate) {

		Iterable<java.sql.Date> doorLogs = doorLogRepository.findEntriesWithoutWeekendsByEmployeeID(employeeID,
				fromDate, toDate);
		List<Date> doorLogList = new ArrayList<>();
		for (Date doorLog : doorLogs) {
			doorLogList.add(doorLog);
		}

		return doorLogList;
	}

	@Override
	public List<Date> findEntriesByEmployeeID(int employeeID, Date fromDate, Date toDate) {

		Iterable<java.sql.Date> doorLogs = doorLogRepository.findEntriesByEmployeeID(employeeID, fromDate, toDate);
		List<Date> doorLogList = new ArrayList<>();
		for (Date doorLog : doorLogs) {
			doorLogList.add(doorLog);
		}

		return doorLogList;
	}

	// @Override
	// public List<Date> findDistinctDateByEmpNo(int empNo, Date fromDate, Date
	// toDate) {
	//
	// Iterable<java.sql.Date> doorLogs =
	// doorLogRepository.findDistinctDateByEmpNo(empNo, fromDate, toDate);
	// List<Date> doorLogList = new ArrayList<>();
	// for (Date doorLog : doorLogs) {
	// doorLogList.add(doorLog);
	// }
	//
	// return doorLogList;
	// }
	//
	// @Override
	// public List<DoorLog> findPairEntry(int empNo, Date date) {
	//
	// List<DoorLog> pairEntry = new ArrayList<>();
	// DoorLog in = new DoorLog();
	// DoorLog out = new DoorLog();
	//
	// DoorLog timeIn = doorLogRepository.findTimeIn(empNo, date);
	// DoorLog timeOut = doorLogRepository.findTimeOut(empNo, date);
	//
	// in.setDate(date);
	// in.setTime(timeIn.getTime());
	// in.setArea(timeIn.getArea());
	// in.setFloor(timeIn.getFloor());
	// in.setDoor(timeIn.getDoor());
	// in.setReader(timeIn.getReader());
	// in.setCardNo(timeIn.getCardNo());
	// in.setCompany(timeIn.getCompany());
	//
	// out.setDate(date);
	// out.setTime(timeOut.getTime());
	// out.setArea(timeOut.getArea());
	// out.setFloor(timeOut.getFloor());
	// out.setDoor(timeOut.getDoor());
	// out.setReader(timeOut.getReader());
	// out.setCardNo(timeOut.getCardNo());
	// out.setCompany(timeOut.getCompany());
	//
	// pairEntry.add(in);
	// pairEntry.add(out);
	//
	// return pairEntry;
	// }
}
