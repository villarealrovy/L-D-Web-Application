/*
 * Copyright (C) 2015 FUJITSU All rights reserved.
 */

package com.fujitsu.itLogs.batch.repository;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fujitsu.itLogs.batch.model.DoorLog;

/**
 * @author r.abella
 *
 */
public interface DoorLogRepository extends JpaRepository<DoorLog, Long> {

	@Query("SELECT d FROM DoorLog d WHERE d.employee.id = :employee_id")
	Iterable<DoorLog> findByEmpId(@Param("employee_id") Long empId);

	@Query("SELECT d FROM DoorLog d WHERE d.employee.empNo = :empNo")
	Iterable<DoorLog> findByEmpNo(@Param("empNo") String empNo);

	@Modifying
	@Query("DELETE FROM DoorLog d WHERE d.date = :inputDate")
	void deleteByDate(@Param("inputDate") Date date);

	@Modifying
	@Query(value = "DELETE FROM door_log WHERE "
			+ " date < DATE_SUB(curdate(), INTERVAL :retentionDate YEAR_MONTH)", nativeQuery = true)
	void deleteOld(@Param("retentionDate") String retentionDate);

	// @Query("DELETE FROM DoorLog WHERE DATEADD(year, 2, CreateDate) <
	// getdate()")

}
