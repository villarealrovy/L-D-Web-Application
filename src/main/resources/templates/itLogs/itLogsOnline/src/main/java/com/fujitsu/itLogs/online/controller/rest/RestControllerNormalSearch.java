/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.controller.rest;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fujitsu.itLogs.online.bean.DoorLogErrorBean;
import com.fujitsu.itLogs.online.controller.CommonController;
import com.fujitsu.itLogs.online.model.DoorLog;
import com.fujitsu.itLogs.online.model.Employee;
import com.fujitsu.itLogs.online.service.DoorLogService;
import com.fujitsu.itLogs.online.web.utils.Constants;
import com.fujitsu.itLogs.online.web.utils.ITLogsMailNotification;

/**
 * Call REST service for normal search
 * 
 * @author t.alba@ph.fujitsu.com
 * 
 * @version 1.0.0
 * 
 *         History: Date(YYYY.MM.DD)|author|revision.
 * 
 *         2017.04.03 | r.monte@ph.fujitsu.com | added email FTS functionality.
 *         2017.04.10 | r.monte@ph.fujitsu.com | revised logic for pair date
 *         search inquiry.
 * 
 */
@RestController
@RequestMapping("/itLogs/rest/search")
@Component
@EnableGlobalAuthentication
public class RestControllerNormalSearch {
	private static Logger logger = LoggerFactory.getLogger(RestControllerNormalSearch.class);

	@Autowired
	private CommonController commonCtlr;

	@Autowired
	DoorLogService doorLogService;

	@Autowired
	private MessageSource messageSource;

	@Value("${file.fts.upload}") // get from application.properties
	private String uploadFilePath;

	@Autowired
	ITLogsMailNotification iTLogsMailNotification;

	@RequestMapping(value = "/listAll/{empNo}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<DoorLog>> retrieveAllByEmployeeNo(@PathVariable String empNo, HttpSession session) {
		logger.debug("START: retrieveAllByEmployeeNo." + " [" + empNo + "]");

		@SuppressWarnings("unchecked")
		Map<String, String> userLoginPrincipal = (Map<String, String>) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Employee loginUser = commonCtlr.getLoginUserInfo(userLoginPrincipal.get(Constants.USER_INFO_USERNAME), session);

		if (loginUser.getSys_role().equals(Constants.ROLE_MANAGER)) {
			logger.info("User is MANAGER.");

		} else if (loginUser.getSys_role().equals(Constants.ROLE_STAFF)) {
			logger.info("User is STAFF.");

		} else {
			return new ResponseEntity<List<DoorLog>>(HttpStatus.UNAUTHORIZED);
		}

		List<DoorLog> doorLogList = doorLogService.findByEmpNo(empNo);

		return new ResponseEntity<List<DoorLog>>(doorLogList, HttpStatus.OK);

	}

	@RequestMapping(value = "/listAllByDate/{fromDate}/{toDate}/{strEmpNo}/{searchCategory}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> retrieveAllByDate(@PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String strEmpNo, @PathVariable String searchCategory, HttpSession session, Model model) {

		@SuppressWarnings("unchecked")
		Map<String, String> userLoginPrincipal = (Map<String, String>) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Employee loginUser = commonCtlr.getLoginUserInfo(userLoginPrincipal.get(Constants.USER_INFO_USERNAME), session);

		if (loginUser.getSys_role().equals(Constants.ROLE_MANAGER)) {
			logger.info("User is MANAGER.");

		} else if (loginUser.getSys_role().equals(Constants.ROLE_STAFF)) {
			logger.info("User is STAFF.");

		} else {
			return new ResponseEntity<List<DoorLog>>(HttpStatus.UNAUTHORIZED);
		}

		// START: Input Validation
		List<DoorLogErrorBean> errorBeanList = new ArrayList<>();

		String dateFormat = "ddMMyyyy";
		Date dateFrom = getStringToDate(fromDate, dateFormat);
		Date dateTo = getStringToDate(toDate, dateFormat);

		errorBeanList = validateInputs(strEmpNo, dateFrom, dateTo, searchCategory);
		if (!errorBeanList.isEmpty()) {
			return new ResponseEntity<List<DoorLogErrorBean>>(errorBeanList, HttpStatus.BAD_REQUEST);
		}
		// END: Input Validation.

		logger.debug(
				"START: retrieveAllByDate" + " [" + fromDate + "," + toDate + "," + strEmpNo + "," + searchCategory + "]");

		List<DoorLog> doorLogList = new ArrayList<>();

		try {

			java.sql.Date sqlDateFrom = new java.sql.Date(dateFrom.getTime());
			java.sql.Date sqlDateTo = new java.sql.Date(dateTo.getTime());

			if (Constants.ALL_ENTRIES.equalsIgnoreCase(searchCategory)) {

				// Get details from DoorLog Table
				doorLogList = doorLogService.findByDate(sqlDateFrom, sqlDateTo, strEmpNo);

			} else if (Constants.PAIR_ENTRY.equalsIgnoreCase(searchCategory)) {

				doorLogList = doorLogService.findByPairDate(sqlDateFrom, sqlDateTo, strEmpNo);

			}

			logger.debug("From Date: " + fromDate + "\n" + "To Date: " + toDate + "\n" + "Employee No: " + strEmpNo);

		} catch (Exception e) {
			return new ResponseEntity<List<DoorLog>>(HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<List<DoorLog>>(doorLogList, HttpStatus.OK);
	}

	private Date getStringToDate(String date, String format) {

		try {
			return new SimpleDateFormat(format).parse(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/emailFTS", method = RequestMethod.POST)
	public ResponseEntity<?> getFileFTStoEmail(@RequestParam("file") MultipartFile file,
			@RequestParam("messageBody") String messageBody, @RequestParam("subjectMail") String subjectMail,
			@RequestParam("emailApprover") String emailApprover,
			HttpSession session) {

		try {
			
			@SuppressWarnings("unchecked")
			Map<String, String> userLoginPrincipal = (Map<String, String>) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			Employee loginUser = commonCtlr.getLoginUserInfo(userLoginPrincipal.get(Constants.USER_INFO_USERNAME),
					session);

			if (loginUser.getSys_role().equals(Constants.ROLE_MANAGER)) {
				logger.info("User is MANAGER.");

			} else if (loginUser.getSys_role().equals(Constants.ROLE_STAFF)) {
				logger.info("User is STAFF.");

			} else {
				return new ResponseEntity<List<DoorLog>>(HttpStatus.UNAUTHORIZED);
			}

			File destinationFile = new File(uploadFilePath + File.separator + file.getOriginalFilename());
			file.transferTo(destinationFile);

			logger.info(file.getOriginalFilename() + " uploaded!");

			// send email
			String username = loginUser.getUsername();
			String password = userLoginPrincipal.get(Constants.USER_INFO_USERPASS);

			iTLogsMailNotification.send(username, password, subjectMail, messageBody, emailApprover,
					destinationFile.getAbsolutePath());

			if (destinationFile.exists()) {
				if (destinationFile.delete()) {
					logger.info(destinationFile + " : Copy on resources has been deleted");
				}
			}

			List<DoorLogErrorBean> errorBeanList = new ArrayList<>();
			DoorLogErrorBean errorBean = new DoorLogErrorBean();
			errorBean.setErrorMessage(
					messageSource.getMessage(Constants.INFO_MESSAGE_2001, null, LocaleContextHolder.getLocale()));

			logger.debug(errorBean.getErrorMessage());
			errorBeanList.add(errorBean);

			return new ResponseEntity<List<DoorLogErrorBean>>(errorBeanList, HttpStatus.OK);

		} catch (IOException | MailException | MessagingException e) {
			System.err.println(e);
		}

		List<DoorLogErrorBean> errorBeanList = new ArrayList<>();
		DoorLogErrorBean errorBean = new DoorLogErrorBean();
		errorBean.setErrorMessage(
				messageSource.getMessage(Constants.ERROR_MESSAGE_2001, null, LocaleContextHolder.getLocale()));

		logger.debug(errorBean.getErrorMessage());
		errorBeanList.add(errorBean);

		return new ResponseEntity<List<DoorLogErrorBean>>(errorBeanList, HttpStatus.BAD_REQUEST);
	}

	private List<DoorLogErrorBean> validateInputs(String strEmployeeID, Date dateFrom, Date dateTo,
			String searchCategory) {

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

			if (Constants.PAIR_ENTRY.equalsIgnoreCase(searchCategory)
					&& dateDifference > Constants.INT_LIMIT_ONE_YEAR) {
				DoorLogErrorBean errorBean = new DoorLogErrorBean();
				errorBean.setErrorMessage(messageSource.getMessage(Constants.ERROR_MESSAGE_2002,
						new String[] { Constants.FROM_DATE, Constants.TO_DATE, Constants.ONE_YEAR },
						LocaleContextHolder.getLocale()));
				logger.debug(errorBean.getErrorMessage());
				errorBeanList.add(errorBean);

			} else if (Constants.ALL_ENTRIES.equalsIgnoreCase(searchCategory)
					&& dateDifference > Constants.INT_LIMIT_TWO_MONTH) {
				DoorLogErrorBean errorBean = new DoorLogErrorBean();
				errorBean.setErrorMessage(messageSource.getMessage(Constants.ERROR_MESSAGE_2003,
						new String[] { Constants.FROM_DATE, Constants.TO_DATE, Constants.TWO_MONTH },
						LocaleContextHolder.getLocale()));
				logger.debug(errorBean.getErrorMessage());
				errorBeanList.add(errorBean);
			}
		}

		return errorBeanList;
	}

	private long getDateDifference(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

}
