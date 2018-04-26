/*
 * Copyright (C) 2015 FUJITSU All rights reserved.
 */

package com.fujitsu.itLogs.batch.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fujitsu.itLogs.batch.model.Employee;


/**
 * @author r.monte
 *
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.empNo = :empNo")
    Employee findByEmpNo(@Param("empNo") String empNo);
    
    @Query("SELECT e FROM Employee e WHERE e.username = :username")
    public Employee findByUsername(@Param("username") String username);
    
}
