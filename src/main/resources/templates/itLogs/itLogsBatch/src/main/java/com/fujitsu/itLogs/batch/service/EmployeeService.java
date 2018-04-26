/*
 * Copyright (C) 2015 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.batch.service;

import java.util.List;
import java.util.Map;

import com.fujitsu.itLogs.batch.model.Employee;



/**
 * @author r.monte
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
    
}
