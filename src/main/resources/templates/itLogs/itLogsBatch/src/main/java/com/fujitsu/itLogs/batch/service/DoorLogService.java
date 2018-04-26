package com.fujitsu.itLogs.batch.service;

import java.util.Date;
import java.util.List;

import com.fujitsu.itLogs.batch.model.DoorLog;

/**
 * @author r.monte
 *
 */
public interface DoorLogService {

	DoorLog save(DoorLog doorLog);

	List<DoorLog> findAll();

	DoorLog find(Long id);

	void delete(Long id);

	List<DoorLog> findByEmpId(Long id);

	List<DoorLog> findByEmpNo(String empNo);

	void deleteByDate(Date date);

	void deleteOld(String retentionDate);

}
