/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.controller.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fujitsu.itLogs.online.bean.DoorLogEntriesBean;
import com.fujitsu.itLogs.online.bean.DoorLogEntriesDetail;
import com.fujitsu.itLogs.online.bean.DoorLogErrorBean;
import com.fujitsu.itLogs.online.controller.CommonController;
import com.fujitsu.itLogs.online.model.DoorLog;
import com.fujitsu.itLogs.online.model.Employee;
import com.fujitsu.itLogs.online.service.DoorLogService;
import com.fujitsu.itLogs.online.web.utils.Constants;

/**
 * Call REST service for advance search
 * 
 * @author y.umayam@ph.fujitsu.com
 *
 * @version 1.0.0
 * 
 *         History: Date(YYYY.MM.DD)|author|revision.
 * 
 *         2017.05.09 | r.monte@ph.fujitsu.com | added call time schedule placeholder in word report.
 *
 */
@RestController
@RequestMapping("/itLogs/rest/advanceSearch")
public class RestControllerAdvanceSearch {
	private static Logger logger = LoggerFactory.getLogger(RestControllerAdvanceSearch.class);

	@Autowired
	private CommonController commonCtlr;

	@Autowired
	DoorLogService doorLogService;

	@Autowired
	private MessageSource messageSource;

	// get from application.properties
	@Value("${file.template.notice.tardiness}")
	private String sourceFile;

	// get from application.properties
	@Value("${file.template.path}")
	private String outputPath;

	@RequestMapping(value = "/listLateEntries/{strEmployeeID}/{fromDate}/{fromTime}/{toDate}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> retrieveLateEntriesByEmployeeID(@PathVariable String strEmployeeID,
			@PathVariable String fromDate, @PathVariable String fromTime, @PathVariable String toDate,
			HttpSession session) {

		@SuppressWarnings("unchecked")
		Map<String, String> userLoginPrincipal = (Map<String, String>) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Employee loginUser = commonCtlr.getLoginUserInfo(userLoginPrincipal.get(Constants.USER_INFO_USERNAME), session);

		if (loginUser.getSys_role().equals(Constants.ROLE_MANAGER)) {
			logger.info("User is MANAGER.");

		} else if (loginUser.getSys_role().equals(Constants.ROLE_STAFF)) {
			logger.info("User is STAFF.");

		} else {
			return new ResponseEntity<List<DoorLogEntriesBean>>(HttpStatus.UNAUTHORIZED);
		}

		// START: Input Validation
		List<DoorLogErrorBean> errorBeanList = new ArrayList<>();

		String dateFormat = Constants.DATE_FORMAT_DDMMYY;
		Date dateFrom = getStringToDate(fromDate, dateFormat);
		Date dateTo = getStringToDate(toDate, dateFormat);

		errorBeanList = validateInputs(strEmployeeID, dateFrom, dateTo);
		if (!errorBeanList.isEmpty()) {
			return new ResponseEntity<List<DoorLogErrorBean>>(errorBeanList, HttpStatus.BAD_REQUEST);
		}
		// END: Input Validation.

		int employeeID = Integer.parseInt(strEmployeeID);

		java.sql.Date sqlDateFrom = new java.sql.Date(dateFrom.getTime());
		java.sql.Date sqlDateTo = new java.sql.Date(dateTo.getTime());

		String timeFormat = Constants.TIME_FORMAT_HHMM;
		Date timeFrom = getStringToDate(fromTime, timeFormat);

		java.sql.Time sqlTimeFrom = new java.sql.Time(timeFrom.getTime());

		List<DoorLog> doorLogList = doorLogService.findLateEntriesByEmployeeID(employeeID, sqlDateFrom, sqlTimeFrom,
				sqlDateTo);

		SimpleDateFormat dateFormat2 = new SimpleDateFormat(Constants.DATE_FORMAT_DDMMYY_2);
		Calendar calendar = Calendar.getInstance();
		List<DoorLogEntriesBean> lateEntriesList = new ArrayList<>();
		List<String> monthYearList = new ArrayList<>();
		@SuppressWarnings("deprecation")
		int expectedTimeIn = (timeFrom.getHours() * 60) + timeFrom.getMinutes();
		Map<String, List<String>> daysWithLateEntries = new LinkedHashMap<>();

		for (DoorLog doorLog : doorLogList) {
			DoorLogEntriesBean tempObj = new DoorLogEntriesBean();
			DoorLogEntriesDetail tempObjDetail = new DoorLogEntriesDetail();

			calendar.setTime(doorLog.getDate());

			logger.debug(dateFormat2.format(doorLog.getDate()));

			// Set Month Year Column
			String month = getMonthName(calendar.get(Calendar.MONTH));
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String monthYear = month + Constants.SPACE + year;

			if (monthYearList.contains(monthYear)) {
				logger.debug("Month Year - " + monthYear + " already exists.");
				int index = monthYearList.indexOf(monthYear);

				// Set Days of Late Column per Month Year
				lateEntriesList.get(index).setDaysCount(lateEntriesList.get(index).getDaysCount() + 1);

				// Set Total Minutes of Late per Month Year
				@SuppressWarnings("deprecation")
				long timeIn = (doorLog.getTime().getHours() * 60) + doorLog.getTime().getMinutes();
				long minutesOfLate = timeIn - expectedTimeIn;
				lateEntriesList.get(index)
						.setTotalMinutes(lateEntriesList.get(index).getTotalMinutes() + minutesOfLate);

				// Set the Day of the Week
				tempObjDetail.setDayofTheWeek(getDayName(doorLog.getDate()));

				// Set the Days with Late Entries
				String dayLate = dateFormat2.format(doorLog.getDate());
				List<String> timeSheet = new ArrayList<>();
				DoorLog timeOut = new DoorLog();
				java.sql.Date sqlDate = new java.sql.Date(doorLog.getDate().getTime());
				timeOut = doorLogService.findTimeSheetbyEmployeeID(employeeID, sqlDate);
				timeSheet.add(getDateToString(doorLog.getTime(), timeFormat)); // Time
																				// In
				timeSheet.add(getDateToString(timeOut.getTime(), timeFormat)); // Time
																				// Out
				daysWithLateEntries.put(dayLate, timeSheet);
				tempObjDetail.setDaysWithLateEntries(daysWithLateEntries);

				lateEntriesList.get(index).getDoorLogEntriesDetails().add(tempObjDetail);

			} else {
				logger.debug("Month Year - " + monthYear + " doesn't exists.");
				// Add New Late Entries for Month Year if not existing in the
				// current Late Entries List
				monthYearList.add(monthYear);
				tempObj.setMonthYear(monthYear);

				// Set Days of Late Column per Month Year
				tempObj.setDaysCount(1);

				// Set Total Minutes of Late per Month Year
				@SuppressWarnings("deprecation")
				long timeIn = (doorLog.getTime().getHours() * 60) + doorLog.getTime().getMinutes();
				long minutesOfLate = timeIn - expectedTimeIn;
				logger.debug("Testing minutesOfLate : " + timeIn + " - " + expectedTimeIn + " = " + minutesOfLate);
				tempObj.setTotalMinutes(minutesOfLate);

				// Set the Day of the Week
				tempObjDetail.setDayofTheWeek(getDayName(doorLog.getDate()));

				// Set the Days with Late Entries
				String dayLate = dateFormat2.format(doorLog.getDate());
				List<String> timeSheet = new ArrayList<>();
				DoorLog timeOut = new DoorLog();
				java.sql.Date sqlDate = new java.sql.Date(doorLog.getDate().getTime());
				timeOut = doorLogService.findTimeSheetbyEmployeeID(employeeID, sqlDate);
				timeSheet.add(getDateToString(doorLog.getTime(), timeFormat)); // Time
																				// In
				timeSheet.add(getDateToString(timeOut.getTime(), timeFormat)); // Time
																				// Out
				daysWithLateEntries = new LinkedHashMap<>();
				daysWithLateEntries.put(dayLate, timeSheet);
				tempObjDetail.setDaysWithLateEntries(daysWithLateEntries);

				tempObj.getDoorLogEntriesDetails().add(tempObjDetail);
				lateEntriesList.add(tempObj);
			}

		}

		return new ResponseEntity<List<DoorLogEntriesBean>>(lateEntriesList, HttpStatus.OK);
	}

	@RequestMapping(value = "/listNoEntries/{strEmployeeID}/{fromDate}/{toDate}/{isWeekendsIncluded}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> retrieveNoEntriesByEmployeeID(@PathVariable String strEmployeeID,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable boolean isWeekendsIncluded,
			HttpSession session) {

		@SuppressWarnings("unchecked")
		Map<String, String> userLoginPrincipal = (Map<String, String>) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Employee loginUser = commonCtlr.getLoginUserInfo(userLoginPrincipal.get(Constants.USER_INFO_USERNAME), session);

		if (loginUser.getSys_role().equals(Constants.ROLE_MANAGER)) {
			logger.info("User is MANAGER.");

		} else if (loginUser.getSys_role().equals(Constants.ROLE_STAFF)) {
			logger.info("User is STAFF.");

		} else {
			return new ResponseEntity<List<DoorLogEntriesBean>>(HttpStatus.UNAUTHORIZED);
		}

		// START: Input Validation
		List<DoorLogErrorBean> errorBeanList = new ArrayList<>();

		String dateFormat = Constants.DATE_FORMAT_DDMMYY;
		Date dateFrom = getStringToDate(fromDate, dateFormat);
		Date dateTo = getStringToDate(toDate, dateFormat);

		errorBeanList = validateInputs(strEmployeeID, dateFrom, dateTo);
		if (!errorBeanList.isEmpty()) {
			logger.debug("Inputs have error.");
			return new ResponseEntity<List<DoorLogErrorBean>>(errorBeanList, HttpStatus.BAD_REQUEST);
		}
		// END: Input Validation.

		int employeeID = Integer.parseInt(strEmployeeID);

		java.sql.Date sqlDateFrom = new java.sql.Date(dateFrom.getTime());
		java.sql.Date sqlDateTo = new java.sql.Date(dateTo.getTime());

		// Get the list of date
		LocalDate start = dateFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate end = dateTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		List<java.sql.Date> dateList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();

		while (!end.isBefore(start)) {
			calendar.setTime(java.sql.Date.valueOf(end));
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

			if (isWeekendsIncluded) {
				dateList.add(java.sql.Date.valueOf(end));
			} else {
				if (dayOfWeek != 1 && dayOfWeek != 7) {
					dateList.add(java.sql.Date.valueOf(end));
				}
			}

			end = end.minusDays(1);
		}

		logger.debug("dateList : " + dateList.toString());

		List<java.sql.Date> doorLogList = new ArrayList<>();

		if (!isWeekendsIncluded) {
			doorLogList = doorLogService.findEntriesWithoutWeekendsByEmployeeID(employeeID, sqlDateFrom, sqlDateTo);
		} else {
			doorLogList = doorLogService.findEntriesByEmployeeID(employeeID, sqlDateFrom, sqlDateTo);
		}
		logger.debug("doorLogList : " + doorLogList.toString());

		List<java.sql.Date> noDoorLogList = new ArrayList<>();
		for (java.sql.Date dateObj : dateList) {
			if (!doorLogList.contains(dateObj)) {
				noDoorLogList.add(dateObj);
			}
		}
		logger.debug("noDoorLogList : " + noDoorLogList.toString());

		SimpleDateFormat dateFormat2 = new SimpleDateFormat(Constants.DATE_FORMAT_DDMMYY_2);
		List<DoorLogEntriesBean> noEntriesList = new ArrayList<>();
		List<String> monthYearList = new ArrayList<>();

		for (java.sql.Date doorLog : noDoorLogList) {
			DoorLogEntriesBean tempObj = new DoorLogEntriesBean();
			DoorLogEntriesDetail tempObjDetail = new DoorLogEntriesDetail();

			calendar.setTime(doorLog);

			logger.debug("dateFormat2.format(doorLog) : " + dateFormat2.format(doorLog));

			// Set Month Year Column
			String month = getMonthName(calendar.get(Calendar.MONTH));
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String monthYear = month + " " + year;

			if (monthYearList.contains(monthYear)) {
				logger.debug("Month Year - " + monthYear + " already exists.");
				int index = monthYearList.indexOf(monthYear);

				// Set the Day of the Week
				tempObjDetail.setDayofTheWeek(getDayName(doorLog));

				// Set the Days with Late Entries
				String dayNo = dateFormat2.format(doorLog);
				tempObjDetail.setDayWithNoEntry(dayNo);

				noEntriesList.get(index).getDoorLogEntriesDetails().add(tempObjDetail);
				logger.debug("noEntriesList.get(index).getMonthYear() : " + noEntriesList.get(index).getMonthYear());

			} else {
				logger.debug("Month Year - " + monthYear + " doesn't exists.");
				// Add New Late Entries for Month Year if not existing in the
				// current Late Entries List
				monthYearList.add(monthYear);
				tempObj.setMonthYear(monthYear);

				// Set the Day of the Week
				tempObjDetail.setDayofTheWeek(getDayName(doorLog));

				// Set the Days with Late Entries
				String dayLate = dateFormat2.format(doorLog);
				tempObjDetail.setDayWithNoEntry(dayLate);

				tempObj.getDoorLogEntriesDetails().add(tempObjDetail);
				noEntriesList.add(tempObj);
				logger.debug("noEntriesList.get(index).getMonthYear() : " + tempObj.getMonthYear());
			}
		}

		return new ResponseEntity<List<DoorLogEntriesBean>>(noEntriesList, HttpStatus.OK);
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/exportLateEntries/{expName}/{offenseDescription}/{daysOfLate}/{minutesOfLate}/{scheduledTime}", method = RequestMethod.GET, produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	@ResponseBody
	public ResponseEntity<byte[]> exportLateEntriesToDoc(@PathVariable String expName,
			@PathVariable String offenseDescription, @PathVariable String daysOfLate,
			@PathVariable String minutesOfLate, @PathVariable String scheduledTime, HttpSession session) {

		@SuppressWarnings("unchecked")
		Map<String, String> userLoginPrincipal = (Map<String, String>) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Employee loginUser = commonCtlr.getLoginUserInfo(userLoginPrincipal.get(Constants.USER_INFO_USERNAME), session);

		if (loginUser.getSys_role().equals(Constants.ROLE_MANAGER)) {
			logger.info("User is MANAGER.");

		} else if (loginUser.getSys_role().equals(Constants.ROLE_STAFF)) {
			logger.info("User is STAFF.");

		} else {
			return new ResponseEntity<byte[]>(HttpStatus.UNAUTHORIZED);
		}

		try {
			XWPFDocument doc = new XWPFDocument(OPCPackage.open(sourceFile));
			for (XWPFParagraph p : doc.getParagraphs()) {
				List<XWPFRun> runs = p.getRuns();
				if (runs != null) {
					for (XWPFRun r : runs) {
						String text = r.getText(0);
						if (text != null) {

							// Print Name
							if (text.contains(Constants.NAME_PLACEHOLDER)) {
								String formattedExpName = String.format(Constants.STRING_FORMAT_40_L, expName);
								text = text.replace(Constants.NAME_PLACEHOLDER, formattedExpName);
								r.setText(text, 0);
							}

							// Print Date
							if (text.contains(Constants.DATE_PLACEHOLDER)) {
								Date currentDate = new Date();
								String formattedCurrentDate = getDateToString(currentDate,
										Constants.DATE_FORMAT_DDMMYY_2);
								formattedCurrentDate = String.format(Constants.STRING_FORMAT_20_L, formattedCurrentDate)
										.replace(" ", "_");
								text = text.replace(Constants.DATE_PLACEHOLDER, formattedCurrentDate);
								r.setText(text, 0);
							}

							// Print Schedule
							if (text.contains(Constants.TIME_SCHEDULE_PLACEHOLDER)) {
								SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
								Date timeLog = timeFormat.parse(scheduledTime);
								
								//deduct minute for call time
								timeLog.setMinutes(timeLog.getMinutes() - 1);								
								SimpleDateFormat timeFormatAMPM = new SimpleDateFormat("hh:mm a");								
								
								text = text.replace(Constants.TIME_SCHEDULE_PLACEHOLDER, "("
										+ timeFormatAMPM.format(timeLog).toString() + ")");
								
								r.setText(text, 0);
							}							
							
							// Print Details Description of Offense
							if (text.contains(Constants.DETAIL_PLACEHOLDER)) {
								String formattedOffenseDesc = offenseDescription.replace(Constants.DASH,
										Constants.SLASH);
								formattedOffenseDesc = formattedOffenseDesc.replace(Constants.COMMA, Constants.NEWLINE);
								logger.debug(formattedOffenseDesc);
								text = text.replace(Constants.DETAIL_PLACEHOLDER, formattedOffenseDesc);
								r.setText(text, 0);
							}

							if (text.contains(Constants.NEWLINE)) {
								String[] stringsOnNewLines = text.split(Constants.NEWLINE);
								r.setText("", 0);
								for (int i = 0; i < stringsOnNewLines.length; i++) {
									String textForLine = String
											.format(Constants.STRING_FORMAT_80_L, stringsOnNewLines[i])
											.replace(" ", "_");
									r.setText(textForLine);
									r.addCarriageReturn();
								}
							}

							if (text.contains(Constants.MINUTES_PLACEHOLDER)) {
								text = text.replace(Constants.MINUTES_PLACEHOLDER, minutesOfLate);
								r.setText(text, 0);
							}

							if (text.contains(Constants.DAYS_PLACEHOLDER)) {
								text = text.replace(Constants.DAYS_PLACEHOLDER, daysOfLate);
								r.setText(text, 0);
							}

						}

					}
				}
			}

			String outputFileName = Constants.NOTICE_TARDINESS + Constants.OPEN_PARENTHESIS + expName
					+ Constants.CLOSE_PARENTHESIS + Constants.DOC_EXTENSION;
			doc.write(new FileOutputStream(outputPath + outputFileName));

			File outputFile = new File(outputPath + outputFileName);
			FileInputStream fileStream = new FileInputStream(outputFile);
			byte[] contents = IOUtils.toByteArray(fileStream);
			fileStream.close();

			if (outputFile.exists()) {
				if (outputFile.delete()) {
					logger.debug(outputFileName + " : Copy on resources has been deleted");
				}
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType
					.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
			headers.setContentDispositionFormData(outputFileName, outputFileName);
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
			return response;
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (InvalidFormatException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private List<DoorLogErrorBean> validateInputs(String strEmployeeID, Date dateFrom, Date dateTo) {

		List<DoorLogErrorBean> errorBeanList = new ArrayList<>();
		if (strEmployeeID.equalsIgnoreCase(Constants.STR_NULL)) {
			DoorLogErrorBean errorBean = new DoorLogErrorBean();
			errorBean.setErrorMessage(messageSource.getMessage(Constants.ERROR_MESSAGE_1002,
					new String[] { Constants.EMPLOYEE_NAME, Constants.FDC_LIST }, LocaleContextHolder.getLocale()));
			logger.debug(errorBean.getErrorMessage());
			errorBeanList.add(errorBean);
		}

		if (!dateTo.after(dateFrom) && !dateFrom.equals(dateTo)) {
			DoorLogErrorBean errorBean = new DoorLogErrorBean();
			errorBean.setErrorMessage(messageSource.getMessage(Constants.ERROR_MESSAGE_1004,
					new String[] { Constants.TO_DATE, Constants.FROM_DATE }, LocaleContextHolder.getLocale()));
			logger.debug(errorBean.getErrorMessage());
			errorBeanList.add(errorBean);
		} else {
			long dateDifference = getDateDifference(dateFrom, dateTo, TimeUnit.DAYS);
			if (dateDifference > 366) {
				DoorLogErrorBean errorBean = new DoorLogErrorBean();
				errorBean.setErrorMessage(messageSource.getMessage(Constants.ERROR_MESSAGE_1005,
						new String[] { Constants.FROM_DATE, Constants.TO_DATE, Constants.ONE_YEAR },
						LocaleContextHolder.getLocale()));
				logger.debug(errorBean.getErrorMessage());
				errorBeanList.add(errorBean);
			}
		}

		return errorBeanList;
	}

	private String getDayName(Date day) {

		return new SimpleDateFormat(Constants.DAY_FORMAT_EEEE).format(day);
	}

	private String getMonthName(int month) {

		return new DateFormatSymbols().getMonths()[month];
	}

	private Date getStringToDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return null;
	}

	private String getDateToString(Date time, String format) {

		return new SimpleDateFormat(format).format(time);
	}

	private long getDateDifference(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

}
