package com.fujitsu.itLogs.batch.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fujitsu.itLogs.batch.model.Company;
import com.fujitsu.itLogs.batch.model.DoorLog;
import com.fujitsu.itLogs.batch.model.Employee;
import com.fujitsu.itLogs.batch.service.CompanyService;
import com.fujitsu.itLogs.batch.service.DoorLogService;
import com.fujitsu.itLogs.batch.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("mysql")
public class DoorLogServiceImplTest {

	@Autowired
	private DoorLogService doorLogService;

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CompanyService companyService;

	private void saveTestEmployee(String empNo) {
		Employee employeeEntry = new Employee();

		employeeEntry.setEmpNo(empNo);
		employeeEntry.setUsername("test");
		employeeEntry.setSys_role("manager");
		employeeEntry.setFullname("testuser");
		employeeEntry.setEmailAccount("test.ph@fujitsu.com");
		
		
		Company companyEntry = new Company();
		companyEntry.setGdcName("Philippines");
		companyEntry.setDepartment("FDC");
		companyEntry.setDivision("Apps");
		companyEntry.setLos("BAS");
		
		Company existingCompany = companyService.save(companyEntry);
		
		employeeEntry.setCompany(existingCompany);

		employeeService.save(employeeEntry);

	}

	@SuppressWarnings("deprecation")
	@Test
	public void doorLogSaveTest() throws ParseException {

		// save test employee first and get employee no.
		String employeeNum = "999999";
		int cardNo = 123456;

		saveTestEmployee(employeeNum);

		DoorLog doorLog = createDoorLog(employeeNum, cardNo);

		System.out.println("check expected: " + doorLog.toString());

		doorLogService.save(doorLog);

		// check if already saved
		List<DoorLog> doorLogActualList = doorLogService.findByEmpNo(employeeNum);

		System.out.println("check expected: " + doorLog.toString());
		System.out.println("check actual: " + doorLogActualList.get(0).toString());

		assertEquals(doorLog.getDate().getDate(), doorLogActualList.get(0).getDate().getDate());
		assertEquals(doorLog.getTime(), doorLogActualList.get(0).getTime());

		assertEquals(doorLog.getArea(), doorLogActualList.get(0).getArea());

		assertEquals(doorLog.getFloor(), doorLogActualList.get(0).getFloor());

		assertEquals(doorLog.getDoor(), doorLogActualList.get(0).getDoor());

		assertEquals(doorLog.getReader(), doorLogActualList.get(0).getReader());

		assertEquals(doorLog.getEmployee().getEmpNo(), doorLogActualList.get(0).getEmployee().getEmpNo());

		assertEquals(doorLog.getEmployee().getFullname(), doorLogActualList.get(0).getEmployee().getFullname());

		assertEquals(doorLog.getEmployee().getId(), doorLogActualList.get(0).getEmployee().getId());

		assertEquals(doorLog.getEmployee().getSys_role(), doorLogActualList.get(0).getEmployee().getSys_role());

		assertEquals(doorLog.getEmployee().getUsername(), doorLogActualList.get(0).getEmployee().getUsername());

		assertEquals(doorLog.getCardNo(), doorLogActualList.get(0).getCardNo());

		assertEquals(doorLog.getCompany(), doorLogActualList.get(0).getCompany());

		// get all existing entry
		List<DoorLog> doorLogActualAllList = doorLogService.findAll();
		assertTrue(doorLogActualAllList != null);

		DoorLog doorLogActualFindId = doorLogService.find(doorLogActualList.get(0).getId());
		assertEquals(doorLogActualFindId.getId(), doorLogActualList.get(0).getId());

		List<DoorLog> doorLogActualFindByEmpId = doorLogService
				.findByEmpId(doorLogActualList.get(0).getEmployee().getId());
		assertEquals(doorLogActualFindByEmpId.get(0).getEmployee().getId(),
				doorLogActualList.get(0).getEmployee().getId());

		doorLogService.delete(doorLogActualList.get(0).getId());
		DoorLog doorLogActualAfterDelete = doorLogService.find(doorLogActualList.get(0).getId());
		assertTrue(doorLogActualAfterDelete == null);

	}


	/**
	 * TODO: Describe this method
	 * 
	 * @author r.abella
	 * @param employeeNum
	 * @param cardNo
	 * @return
	 * @throws ParseException
	 */
	private DoorLog createDoorLog(String employeeNum, int cardNo) throws ParseException {
		DoorLog doorLog = new DoorLog();

		// set date
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date date = new Date(timestamp.getTime());
		doorLog.setDate(date);

		// set time
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date timeLog = null;

		timeLog = timeFormat.parse("5:17:00");
		doorLog.setTime(timeLog);

		// set area
		doorLog.setArea("Eco tower");

		doorLog.setFloor("12");

		doorLog.setDoor("Left door");

		doorLog.setReader("In");

		// get Employee object using saved test employee

		Employee employee = employeeService.findByEmpNo(employeeNum);
		doorLog.setEmployee(employee);

		doorLog.setCardNo(String.valueOf(cardNo));

		doorLog.setCompany("Accenture");
		return doorLog;
	}

}
