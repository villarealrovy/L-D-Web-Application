/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */

package com.fujitsu.itLogs.online.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fujitsu.itLogs.online.model.Employee;

/**
 * @author r.monte@ph.fujitsu.com
 *
 * @version 1.0.0
 *
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.empNo = :empNo")
    Employee findByEmpNo(@Param("empNo") String empNo);
    
    @Query("SELECT e FROM Employee e WHERE e.username = :username")
    public Employee findByUsername(@Param("username") String username);
    
    @Query("SELECT e FROM Employee e WHERE e.company.id = :companyId")
    public List<Employee> findByCompanyId(@Param("companyId") Long companyId);
    
}
