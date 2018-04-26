/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;

import com.fujitsu.itLogs.online.model.Employee;

/**
 * @author r.monte@ph.fujitsu.com
 *
 * @version 1.0.0
 *
 */
public interface EmployeeService {

	Employee save(Employee employee);

	List<Employee> findAll();
    Map<String, Employee> findAllEmployeeNumbers();
	Employee find(Long id);

	void delete(Long id);

	Employee findByEmployeeUserId(Long id);

    Employee findByEmpNo(String empNo);

	Employee findByUsername(String username);
	
	List<Employee> findByCompanyId(@Param("companyId") Long companyId);

}
