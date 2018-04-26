/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.batch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fujitsu.itLogs.batch.model.Company;
import com.fujitsu.itLogs.batch.repository.CompanyRepository;
import com.fujitsu.itLogs.batch.service.CompanyService;

/**
 * @author r.monte
 *
 */
@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	CompanyRepository companyRepository;

	@Override
	public Company save(Company company) {
		
		Company companyEntry = new Company();
		companyEntry.setGdcName(company.getGdcName().trim());
		companyEntry.setDepartment(company.getDepartment().trim());
		companyEntry.setDivision(company.getDivision().trim());
		companyEntry.setLos(company.getLos().trim());

		// find first existing
		Company companyExisting = companyRepository.findByGdcDeptDivLos(companyEntry.getGdcName(), 
				companyEntry.getDepartment(), companyEntry.getDivision(), companyEntry.getLos());

		if (companyExisting != null) {
			return companyExisting;
			
		}

		return companyRepository.save(company);
	}

	@Override
	public List<Company> findAll() {
		Iterable<Company> companyListing = companyRepository.findAll();
		List<Company> companyListAll = new ArrayList<>();
		for (Company company : companyListing) {
			companyListAll.add(company);
		}

		return companyListAll;
	}

	@Override
	public Company find(Long id) {
		return companyRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		companyRepository.delete(id);
	}

	@Override
	public Company findByCompanyId(Long id) {
		Company company = companyRepository.findByCompanyId(id);

		return company;
	}

	@Override
	public Company findByGdcDeptDivLos(String gdcName, String department, String division, String los) {
		Company company = companyRepository.findByGdcDeptDivLos(gdcName, department, division, los);

		return company;
	}

}
