/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */

package com.fujitsu.itLogs.batch.tasklet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fujitsu.itLogs.batch.model.Company;
import com.fujitsu.itLogs.batch.model.DoorLog;
import com.fujitsu.itLogs.batch.model.Employee;
import com.fujitsu.itLogs.batch.model.UploadStatus;
import com.fujitsu.itLogs.batch.service.CompanyService;
import com.fujitsu.itLogs.batch.service.DoorLogService;
import com.fujitsu.itLogs.batch.service.EmployeeService;
import com.fujitsu.itLogs.batch.service.UploadStatusService;
import com.fujitsu.itLogs.batch.util.Constants;
import com.fujitsu.ph.excel.util.Data;
import com.fujitsu.ph.excel.util.ExcelTemplating;

/**
 * @author r.abella@ph.fujitsu.com
 * 
 * @version 1.0.0
 *
 *         History: Date(YYYY.MM.DD)|author|revision.
 * 
 *         2017.08.01 | r.monte@ph.fujitsu.com | change data type of employee no from integer to String.
 *         
 */
public class LoadDoorLogsTasklet implements Tasklet {
	private static Logger logger = LoggerFactory.getLogger(LoadDoorLogsTasklet.class);

	@Autowired
	private DoorLogService doorLogService;

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CompanyService companyService;

	@Autowired
	UploadStatusService uploadStatusService;
	
	@Value("${folderYearPartIndex}") // get from application.properties
	private int folderYearPartIndex;

	public RepeatStatus execute(StepContribution arg0, ChunkContext chunkContext) throws Exception {

		logger.info("----- START Running LoadDoorLogsTasklet -------------------");

		Map<String, Employee> employeeNumbers = employeeService.findAllEmployeeNumbers();

		Path filePath = Paths.get((String) chunkContext.getStepContext().getJobParameters().get("filePath"));
		logger.info("Loading filePath[{}]", filePath.toString());

		Path reUpload = Paths.get((String) chunkContext.getStepContext().getJobParameters().get("reUpload"));
		logger.info("Loading reUpload[{}]", reUpload.toString());

		boolean isReUpload = new Boolean(reUpload.toString());

		String yearPart = filePath.getName(filePath.getNameCount() - 2).toString();
		String filename = filePath.getFileName().toString();

		if (isReUpload || !uploadStatusService.isFileNameCompleted(filename, yearPart)) {
			readAndSave(employeeNumbers, filePath);
		} else {
			logger.info("Already Processed File[{}]", filePath.toString());
		}

		logger.info("----- END Running LoadDoorLogsTasklet -------------------");
		return RepeatStatus.FINISHED;

	}

	/**
	 * TODO: Describe this method
	 * 
	 * @author r.abella
	 * @param filePath
	 */
	private void updateFileStatus(Path filePath) throws IOException {

		UploadStatus uploadStatus = new UploadStatus();

		String yearPart = filePath.getName(filePath.getNameCount() - folderYearPartIndex).toString();
		String yearOnly = yearPart.replaceAll("\\D+","");
		
		
		uploadStatus.setYear(yearOnly);
		uploadStatus.setFileName(filePath.getFileName().toString());
	

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date date = new Date(timestamp.getTime());
		uploadStatus.setProcessDate(date);

		uploadStatus.setStatus(Constants.STATUS_COMPLETED);

		uploadStatusService.save(uploadStatus);

	}

	/**
	 * TODO: Describe this method
	 * 
	 * @author r.abella
	 * @param employeeNumbers
	 * @param filePath
	 * @param doorLogList
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	protected void readAndSave(Map<String, Employee> employeeNumbers, Path filePath) throws Exception {

		try (ExcelTemplating template = ExcelTemplating.newInstance(filePath.toString())) {

			boolean isDataInserted = false;
			int lastRowIndex = template.getLastRowIndex();
			Set<Date> dateDeletedMap = new HashSet<Date>();

			logger.info("Processing...");
			
			// read every row in a file
			for (int rowIndex = 0; rowIndex <= lastRowIndex; rowIndex++) {
				
				Map<Integer, Data> rowValue = template.getRowValue(rowIndex);

				// expecting rowValue.get(0) to have date value
				if (rowValue.get(0) != null) {

					try {

						// check row value
						// logger.info("Row[" + rowIndex + "][" +
						// template.getRowValue(rowIndex) + "]");
						logger.debug("Column 0 value[{}]", rowValue.get(0));

						// filter row with column date first
						// if value is already in Date format validate (isDate
						// is
						// true)
						// else isValidDate will validate String if in date
						// format
						if (!isDate(rowValue.get(0).getValue())
								&& !isValidDate(rowValue.get(0).getValue().toString())) {
							// if not date object && string is not in Date
							// format
							continue;
						}
						
						// filter if employee no. contains numbers
						if (!(rowValue.get(7) != null && rowValue.get(7).getValue().toString().trim()
								.replace(".0", "").matches(".*\\d+.*"))) {
							
							logger.debug("employee number does not contain integer");
							continue;
						}
						
						
						// All employees will be saved to door logs table
						String employeeNum = rowValue.get(7).getValue().toString().trim().replace(".0", "");
						
						Employee employee = employeeNumbers.get(employeeNum);
						
						
						// if employeeNum is not yet on DB just create first temporary employee constraint entry
						if (employee == null) {
							
							employee = new Employee();
														
							if (employeeNum.length() < 6) {
								//in case employeeNum is not in 6 digit or 6chars, pad with zeroes								
								employeeNum = String.format("%0" + (6 - employeeNum.length()) + "d%s", 0, employeeNum);							
							}							
							
							employee.setEmpNo(employeeNum);
							
							employee.setUsername(employeeNum);
							employee.setSys_role("STAFF");
							employee.setFullname(rowValue.get(8).getValue().toString().trim());
							
							employee.setEmailAccount("unregistered.ph@fujitsu.com");
							
							Company companyEntry = new Company();
							companyEntry.setGdcName("Unregistered");
							companyEntry.setDepartment("FDC");
							companyEntry.setDivision("Apps");
							companyEntry.setLos("BAS");
							
							Company existingCompany = companyService.save(companyEntry);		
							employee.setCompany(existingCompany);
							
							employeeService.save(employee);
							employeeNumbers.put(employeeNum, employee);
							
						}
						
						//logger.debug("employeeNum: " + employeeNum);

						// convert row to doorLog
						DoorLog doorLog = toDoorLog(rowValue);
						
						// set the employee in door log object
						doorLog.setEmployee(employee);
						
						logger.debug("check: " + doorLog.toString());

						// check doorLog.date existing deletedDates map
						if (!dateDeletedMap.contains(doorLog.getDate())) {
							// delete all doorLogs with this doorLog.getDate()

							doorLogService.deleteByDate(doorLog.getDate());

							dateDeletedMap.add(doorLog.getDate());
						}

						// delete all dates - add to deletedDates map

						// save to DB
						doorLogService.save(doorLog);

						isDataInserted = true;

					} catch (Exception ex) {
						logger.info("Error at Row[" + rowIndex + "][" + template.getRowValue(rowIndex) + "]");
						throw ex;
					}

				} else {
					// this is null row
					continue;
				}

			} // end of reading rows

			if (isDataInserted) {
				logger.info("Writing content of [{}] to DB ", filePath);
				updateFileStatus(filePath);
			}

		}
	}

	protected boolean isValidDate(String dateString) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			logger.debug("isValidDate?[{}] ", dateString);
			df.parse(dateString);
			return true;
		} catch (ParseException e) {
			// logger.error("Encountered Error : " + e);
			return false;
		}
	}

	private boolean isDate(Object data) {
		logger.debug("isDate?[{}]", data instanceof Date);
		return data instanceof Date;
	}

	protected DoorLog toDoorLog(Map<Integer, Data> rowValue) throws ParseException {
		DoorLog doorLog = new DoorLog();

		if (!isDate(rowValue.get(0).getValue())) {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date inputDate = dateFormat.parse(rowValue.get(0).getValue().toString());
			doorLog.setDate(inputDate);
		} else {
			doorLog.setDate((Date) rowValue.get(0).getValue());
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date timeLog = null;
		timeLog = timeFormat.parse(rowValue.get(1).getValue().toString());

		doorLog.setTime(timeLog);

		Object area = rowValue.get(3) != null? rowValue.get(3).getValue() : null;
		doorLog.setArea(area != null? area.toString().trim() : null);
		
		Object floor = rowValue.get(4) != null? rowValue.get(4).getValue() : null;
		doorLog.setFloor(floor != null? floor.toString().trim() : null);
		
		Object door = rowValue.get(5) != null? rowValue.get(5).getValue() : null;
		doorLog.setDoor(door != null? door.toString().trim() : null);
		
		// this is mandatory, if null file is on error
		doorLog.setReader(rowValue.get(6).getValue().toString().trim());
		
		Long cardNo = ((Double) rowValue.get(9).getValue()).longValue();
		doorLog.setCardNo(String.valueOf(cardNo));

		// doorlog.employee will be set outside

		Object company = rowValue.get(10) != null? rowValue.get(10).getValue() : null;
		doorLog.setCompany(company != null? company.toString().trim() : null);

		return doorLog;
	}
}
