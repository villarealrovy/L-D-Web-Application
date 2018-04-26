package com.fujitsu.itLogs.batch.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fujitsu.itLogs.batch.model.DoorLog;
import com.fujitsu.itLogs.batch.repository.DoorLogRepository;
import com.fujitsu.itLogs.batch.service.DoorLogService;

/**
 * @author r.abella
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
	public void deleteByDate(Date date) {

		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		doorLogRepository.deleteByDate(sqlDate);
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
	public void deleteOld(String retentionDate) {

		doorLogRepository.deleteOld(retentionDate);

	}
}
