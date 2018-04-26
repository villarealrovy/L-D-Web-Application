/**
 * Copyright (C) 2015 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.batch.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fujitsu.itLogs.batch.model.Employee;
import com.fujitsu.itLogs.batch.repository.EmployeeRepository;
import com.fujitsu.itLogs.batch.service.EmployeeService;

/**
 * @author r.monte
 *
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public List<Employee> findAll() {
		Iterable<Employee> employees = employeeRepository.findAll();
		List<Employee> employeeList = new ArrayList<>();
		for (Employee employee : employees) {
			employeeList.add(employee);
		}

		return employeeList;
	}

	@Override
	public Employee save(Employee employee) {

		Employee employeeNoFromDb = employeeRepository.findByEmpNo(employee.getEmpNo());

		//to save time name will not be searched for update, using emp no. only for now
		//Employee employeeNameFromDb = employeeRepository.findByUsername(employee.getUsername());

		// search existing item id to point to for revision
//		if (employeeNameFromDb != null) {
//			// System.out.println(" employee by name from DB: " +
//			// employeeNameFromDb.toString());
//			employee.setId(employeeNameFromDb.getId());
//			
//			// employee number or others might be replaced
//
//		}

		// for existing employee no. to be updated - priority
		if (employeeNoFromDb != null) {			
			// System.out.println(" employee by EMP no from DB: " +
			// employeeNoFromDb.toString());
			employee.setId(employeeNoFromDb.getId());
			
			// employee user_name or others might be replaced
		}

		return employeeRepository.save(employee);
	}

	@Override
	public Employee find(Long id) {

		return employeeRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {

		employeeRepository.delete(id);
	}

	@Override
	public Employee findByEmployeeUserId(Long id) {

		return employeeRepository.findOne(id);
	}

	@Override
	public Employee findByEmpNo(String empNo) {

		return employeeRepository.findByEmpNo(empNo);
	}

	@Override
	public Employee findByUsername(String username) {

		return employeeRepository.findByUsername(username);
	}

	@Override
	public Map<String, Employee> findAllEmployeeNumbers() {
		
		Map<String, Employee> employeeMap = new HashMap<String, Employee>();

		Iterable<Employee> employees = employeeRepository.findAll();

		for (Employee employee : employees) {
			employeeMap.put(employee.getEmpNo(), employee);
		}

		return employeeMap;

	}

}
