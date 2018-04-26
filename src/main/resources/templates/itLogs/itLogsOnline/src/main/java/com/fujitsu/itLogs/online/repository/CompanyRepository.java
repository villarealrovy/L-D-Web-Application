/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */

package com.fujitsu.itLogs.online.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fujitsu.itLogs.online.model.Company;

//Enter import model here.

/**
 * @author r.monte
 *
 */

public interface CompanyRepository extends JpaRepository<Company, Long> {

	@Query("SELECT c FROM Company c WHERE c.id = :id")
	Company findByCompanyId(@Param("id") Long id);

	@Query("SELECT c FROM Company c WHERE c.gdcName = :gdcName and "
			+ "c.department = :department and c.division = :division and "
			+ "c.los = :los")
	Company findByGdcDeptDivLos(@Param("gdcName") String gdcName, @Param("department") String department,
					@Param("division") String division, @Param("los") String los);

}
