package com.fujitsu.itLogs.batch.tasklet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fujitsu.itLogs.batch.model.Company;
import com.fujitsu.itLogs.batch.model.Employee;
import com.fujitsu.itLogs.batch.service.CompanyService;
import com.fujitsu.itLogs.batch.service.EmployeeService;
import com.fujitsu.ph.excel.util.Data;
import com.fujitsu.ph.excel.util.ExcelTemplating;

/**
 * class to read employee excel, and write to employee table.
 * 
 * @author m.cahinod
 *
 */

public class LoadEmployeeTasklet implements Tasklet {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CompanyService companyService;

	private static Logger logger = LoggerFactory.getLogger(LoadEmployeeTasklet.class);

	@Value("${excelPath}") // get from application.properties
	private String employeeFilePath;

	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {

		logger.info("----- START Running LoadEmployeeTasklet -------------------");

		List<Employee> employees = new ArrayList<Employee>();

		logger.info("Loading File[{}]", employeeFilePath);

		try (ExcelTemplating template = ExcelTemplating.newInstance(employeeFilePath)) {

			int lastRowIndex = template.getLastRowIndex();
			for (int rowIndex = 0; rowIndex <= lastRowIndex; rowIndex++) {

				Map<Integer, Data> rowValue = template.getRowValue(rowIndex);

				if (rowValue.get(0) != null && rowValue.get(0).getValue().toString().trim().replace(".0", "").matches(".*\\d+.*")
						&& rowValue.get(1) != null && !rowValue.get(1).getValue().toString().trim().isEmpty()
						&& rowValue.get(2) != null && !rowValue.get(2).getValue().toString().trim().isEmpty()
						&& rowValue.get(5) != null && !rowValue.get(5).getValue().toString().trim().isEmpty()
								
						&& rowValue.get(6) != null && !rowValue.get(6).getValue().toString().trim().isEmpty()
						&& rowValue.get(7) != null && !rowValue.get(7).getValue().toString().trim().isEmpty()
						&& rowValue.get(8) != null && !rowValue.get(8).getValue().toString().trim().isEmpty()
						&& rowValue.get(9) != null && !rowValue.get(9).getValue().toString().trim().isEmpty()
						
						) {
						//required column0 - empNo contains number
						//required column1 - username not null
						//required column2 - sys_role not null
						//not required column3 - full name - null
						//not required column4 - gender - null
						//required column5 - emailAcct not null
						//required column6 - gdc_name not null
						//required column7 - dept not null
						//required column8 - div not null
						//required column9 - LOS not null
					
				
					Employee employeeEntry = new Employee();

					String employeeNum = rowValue.get(0).getValue().toString().trim().replace(".0", "");
		
					employeeEntry.setEmpNo(employeeNum);
					
					employeeEntry.setUsername(rowValue.get(1).getValue().toString().trim());
					employeeEntry.setSys_role(rowValue.get(2).getValue().toString().trim());
					
					
					if (rowValue.get(3) != null && !rowValue.get(3).getValue().toString().trim().isEmpty()) {
						employeeEntry.setFullname(rowValue.get(3).getValue().toString().trim());
					}
					
					if (rowValue.get(4) != null && !rowValue.get(4).getValue().toString().trim().isEmpty()) {
						employeeEntry.setGender(rowValue.get(4).getValue().toString().trim());
					}
					
					employeeEntry.setEmailAccount(rowValue.get(5).getValue().toString().trim());
					
					
					Company companyEntry = new Company();
					companyEntry.setGdcName(rowValue.get(6).getValue().toString().trim());
					companyEntry.setDepartment(rowValue.get(7).getValue().toString().trim());
					companyEntry.setDivision(rowValue.get(8).getValue().toString().trim());
					companyEntry.setLos(rowValue.get(9).getValue().toString().trim());
					
					Company existingCompany = companyService.save(companyEntry);		
					employeeEntry.setCompany(existingCompany);

					employees.add(employeeEntry);
				}
			}

		} catch (IOException e) {
			logger.error("Reading excel error occurred!", e);
			throw e;
		}

		for (Employee emp : employees) {

			logger.info("saving: " + emp.getEmpNo() + "-" + emp.getFullname());
			employeeService.save(emp);
			
		}

		logger.info("----- END Running LoadEmployeeTasklet -------------------");
		return RepeatStatus.FINISHED;
	}
}
