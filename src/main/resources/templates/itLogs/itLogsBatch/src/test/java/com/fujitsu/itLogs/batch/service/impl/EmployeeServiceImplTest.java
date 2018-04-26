package com.fujitsu.itLogs.batch.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fujitsu.itLogs.batch.model.Company;
import com.fujitsu.itLogs.batch.model.Employee;
import com.fujitsu.itLogs.batch.service.CompanyService;
import com.fujitsu.itLogs.batch.service.EmployeeService;

@ActiveProfiles("mysql")
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EmployeeServiceImplTest {

	private static final String NAME_TEST = "rixon";
	private static final String EMPLOYEENO_TEST = "888666";

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CompanyService companyService;

	@Before
	public void beforeEachTestExecution() {
		Employee employeeName = new Employee();
		employeeName.setEmpNo(EMPLOYEENO_TEST);
		employeeName.setUsername(NAME_TEST);
		employeeName.setSys_role("manager");
		employeeName.setFullname("abella");
		
		employeeName.setEmailAccount("test.ph@fujitsu.com");
		
		Company companyEntry = new Company();
		companyEntry.setGdcName("Philippines");
		companyEntry.setDepartment("FDC");
		companyEntry.setDivision("Apps");
		companyEntry.setLos("BAS");
		
		Company existingCompany = companyService.save(companyEntry);		
		employeeName.setCompany(existingCompany);
		
		

		employeeService.save(employeeName);
		if (employeeName == employeeService) {
			System.out.println("Existing" + employeeName);
		}

	}

	@Test
	public void EmployeeSaveTest() {

		String empNo = EMPLOYEENO_TEST;

		Employee employeeEntry = createEmployee(empNo);

		employeeService.save(employeeEntry);

		// check if already saved
		Employee employeeActual = employeeService.findByEmpNo(empNo);

		assertEquals(employeeEntry.getEmpNo(), employeeActual.getEmpNo());

		assertEquals(employeeEntry.getFullname(), employeeActual.getFullname());

		assertEquals(employeeEntry.getId(), employeeActual.getId());

		assertEquals(employeeEntry.getSys_role(), employeeActual.getSys_role());

		assertEquals(employeeEntry.getUsername(), employeeActual.getUsername());

		// get all existing entry

		Employee employeeActualFindByUsername = employeeService.findByUsername(NAME_TEST);
		assertEquals(employeeActualFindByUsername.getUsername(), employeeEntry.getUsername());

		Map<String, Employee> employeeActualFindAllEmployeeNumbers = employeeService.findAllEmployeeNumbers();
		assertTrue(employeeActualFindAllEmployeeNumbers.containsKey(employeeEntry.getEmpNo()));

		List<Employee> EmployeeActualAllList = employeeService.findAll();
		assertTrue(EmployeeActualAllList != null);

		Employee employeeActualFind = employeeService.find(employeeActual.getId());
		assertTrue(employeeActualFind.getEmpNo() == employeeActual.getEmpNo());

		Employee findByEmployeeUserId = employeeService.findByEmployeeUserId(employeeActual.getId());
		assertTrue(findByEmployeeUserId.getEmpNo() == employeeActual.getEmpNo());

		employeeService.delete(employeeActual.getId());
		Employee findByEmployeeDeleted = employeeService.find(employeeActual.getId());
		assertTrue(findByEmployeeDeleted == null);

	}

	/**
	 * 
	 *
	 * @param employeeEntry
	 * @param empNo
	 */
	private Employee createEmployee(String empNo) {
		Employee employeeEntry = new Employee();

		employeeEntry.setEmpNo(empNo);
		employeeEntry.setUsername(NAME_TEST);
		employeeEntry.setSys_role("manager");
		employeeEntry.setFullname("abella");
		
		employeeEntry.setEmailAccount("test.ph@fujitsu.com");
		
		Company companyEntry = new Company();
		companyEntry.setGdcName("Philippines");
		companyEntry.setDepartment("FDC");
		companyEntry.setDivision("Apps");
		companyEntry.setLos("BAS");
		
		Company existingCompany = companyService.save(companyEntry);		
		employeeEntry.setCompany(existingCompany);

		return employeeEntry;
	}
}