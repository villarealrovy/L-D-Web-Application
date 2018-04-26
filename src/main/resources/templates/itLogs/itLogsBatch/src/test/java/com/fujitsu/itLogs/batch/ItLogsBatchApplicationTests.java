package com.fujitsu.itLogs.batch;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fujitsu.itLogs.batch.model.DoorLog;
import com.fujitsu.itLogs.batch.model.Employee;
import com.fujitsu.itLogs.batch.model.UploadStatus;
import com.fujitsu.itLogs.batch.service.DoorLogService;
import com.fujitsu.itLogs.batch.service.EmployeeService;
import com.fujitsu.itLogs.batch.service.UploadStatusService;

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ItLogsBatchApplication.class)
@ActiveProfiles("mysql")
public class ItLogsBatchApplicationTests {

	@Autowired
	private DoorLogService doorLogService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	UploadStatusService uploadStatusService;

	private void cleanUp() {
		// cleanup uploadStatus repo
		List<UploadStatus> uploadList = uploadStatusService.findAll();
		for (UploadStatus item : uploadList) {
			uploadStatusService.delete(item.getId());
		}

		// cleanup doorlog repo
		List<DoorLog> doorLogList = doorLogService.findAll();
		for (DoorLog item : doorLogList) {
			doorLogService.delete(item.getId());
		}

		// cleanup employee repo
		List<Employee> employeeList = employeeService.findAll();
		for (Employee item : employeeList) {
			employeeService.delete(item.getId());
		}

	}

	@Before
	public void beforeEachTestExecution() {
		cleanUp();
	}

	@After
	public void afterEachTestExecution() {
		cleanUp();
	}

	@Test
	public void contextLoads() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, IOException {
		boolean isNormalRun = false;
		String[] args = { "test" };

		ItLogsBatchApplication.main(args);

		isNormalRun = true;
		assertTrue(isNormalRun);

	}

}
