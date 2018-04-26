/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.repository;

import java.sql.Date;
import java.sql.Time;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
public interface DoorLogRepository extends JpaRepository<DoorLog, Long> {

	@Query("SELECT d FROM DoorLog d WHERE d.employee.id = :employee_id")
	Iterable<DoorLog> findByEmpId(@Param("employee_id") Long empId);

	@Query("SELECT d FROM DoorLog d WHERE d.employee.empNo = :empNo")
	Iterable<DoorLog> findByEmpNo(@Param("empNo") String empNo);

	@Query(value = "SELECT * FROM door_log d " + "JOIN( SELECT id, MIN(time) as time FROM door_log "
			+ "WHERE employee_id= :employeeID " + "AND date >= :fromDate AND date <= :toDate "
			+ "AND UPPER(reader) = UPPER('IN') " + "GROUP BY date ORDER BY date DESC) d2 " + "ON  d.id = d2.id "
			+ "WHERE d.time > :fromTime", nativeQuery = true)
	Iterable<DoorLog> findLateEntriesByEmployeeID(@Param("employeeID") int employeeID, @Param("fromDate") Date fromDate,
			@Param("fromTime") Time fromTime, @Param("toDate") Date toDate);

	@Query(value = "SELECT d.id, d.date, MAX(d.time) as time, d.area, d.floor, "
			+ "d.door, d.reader, d.card_no, d.company, d.employee_id " + "FROM door_log d "
			+ "WHERE d.employee_id = :employeeID " + "AND d.date = :date "
			+ "AND UPPER(d.reader) = UPPER('OUT')", nativeQuery = true)
	DoorLog findTimeSheetbyEmployeeID(@Param("employeeID") int employeeID, @Param("date") Date date);

	@Query(value = "SELECT DISTINCT(d.date) FROM door_log d " + "WHERE d.employee_id = :employeeID "
			+ "AND d.date >= :fromDate AND d.date <= :toDate " + "AND DAYOFWEEK(d.date) <> 1 AND DAYOFWEEK(d.date)<>7 "
			+ "ORDER BY d.date DESC", nativeQuery = true)
	Iterable<Date> findEntriesWithoutWeekendsByEmployeeID(@Param("employeeID") int employeeID,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "SELECT DISTINCT(d.date) FROM door_log d " + "WHERE d.employee_id = :employeeID "
			+ "AND d.date >= :fromDate AND d.date <= :toDate " + "ORDER BY d.date DESC", nativeQuery = true)
	Iterable<Date> findEntriesByEmployeeID(@Param("employeeID") int employeeID, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate);

	@Query("SELECT d FROM DoorLog d WHERE d.employee.empNo = :empNo " + "AND d.date >= :fromDate AND d.date <= :toDate "
			+ "ORDER BY d.date, d.time")
	Iterable<DoorLog> findByDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("empNo") String empNo);

	// @Query("SELECT DISTINCT d.date FROM DoorLog d WHERE d.employee.empNo =
	// :empNo AND d.date >= :fromDate AND d.date <= :toDate")
	// Iterable<Date> findDistinctDateByEmpNo(@Param("empNo") int empNo,
	// @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	//
	// @Query(value="SELECT d.id, MIN(d.time) as time "
	// + "FROM door_log d "
	// + "WHERE d.employee_id = (SELECT e.id FROM employee e "
	// + "WHERE e.emp_no = :empNo) "
	// + "AND d.date = :date AND UPPER(d.reader) = UPPER('IN')",
	// nativeQuery=true)
	// DoorLog findTimeIn(@Param("empNo") int empNo, @Param("date") Date date);
	//
	// @Query(value="SELECT d.* "
	// + "FROM door_log d "
	// + "WHERE d.employee_id = (SELECT e.id FROM employee e "
	// + "WHERE e.emp_no = :empNo) "
	// + "AND UPPER(d.reader) = UPPER('OUT') AND d.date = :date ORDER BY d.id
	// DESC LIMIT 1", nativeQuery=true)
	// DoorLog findTimeOut(@Param("empNo") int empNo, @Param("date") Date date);

	@Query(value = "SELECT d.* FROM door_log d " + "    JOIN employee e " + "    ON d.employee_id = e.id  "
			+ "       AND e.emp_no = :empNo " + "    JOIN ( SELECT concat(date,min(time),reader) 'log' "
			+ "           FROM door_log door " + "            JOIN employee e "
			+ "            ON door.employee_id = e.id " + "            AND e.emp_no = :empNo "
			+ "            WHERE date >= :fromDate AND date <= :toDate "
			+ "            AND UPPER(reader) = UPPER('IN') " + "            GROUP BY date ) as timeIN "
			+ "    ON concat(d.date,d.time,d.reader) = timeIN.log " + " UNION " + "   SELECT d.* FROM door_log d "
			+ "    JOIN employee e " + "    ON d.employee_id = e.id  " + "       AND e.emp_no = :empNo "
			+ "    JOIN ( SELECT concat(date,max(time),reader) 'log' " + "           FROM door_log door "
			+ "            JOIN employee e " + "            ON door.employee_id = e.id "
			+ "            AND e.emp_no = :empNo " + "            WHERE date >= :fromDate AND date <= :toDate "
			+ "            AND UPPER(reader) = UPPER('OUT') " + "            GROUP BY date ) as timeOut "
			+ "    ON concat(d.date,d.time,d.reader) = timeOut.log " + " ORDER BY date, time", nativeQuery = true)
	Iterable<DoorLog> findByPairDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("empNo") String empNo);

}
