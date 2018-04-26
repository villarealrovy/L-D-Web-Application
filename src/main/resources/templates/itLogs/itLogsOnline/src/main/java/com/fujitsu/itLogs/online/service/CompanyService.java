/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.service;

import java.util.List;
import com.fujitsu.itLogs.online.model.Company;

/**
 * @author r.monte
 *
 */

public interface CompanyService {

	Company save(Company company);

	List<Company> findAll();

	Company find(Long id);

	void delete(Long id);

	Company findByCompanyId(Long id);

	Company findByGdcDeptDivLos(String gdcName, String department, String division, String los);

}
