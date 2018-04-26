/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//import com.fujitsu.itLogs.online.exception.ExceptionCodes;
//import com.fujitsu.itLogs.online.model.DoorLog;
import com.fujitsu.itLogs.online.model.Employee;
import com.fujitsu.itLogs.online.service.DoorLogService;
import com.fujitsu.itLogs.online.service.EmployeeService;
//import com.fujitsu.itLogs.online.model.Employee;
import com.fujitsu.itLogs.online.web.utils.Constants;

/**
 * @author r.monte@ph.fujitsu.com
 *
 * @version 1.0.0
 *
 */
@Controller
@RequestMapping("/itLogs/search")
public class NormalSearchController {
	private static Logger logger = LoggerFactory.getLogger(NormalSearchController.class);

	@Autowired
	private CommonController commonCtlr;

	@Autowired
	DoorLogService doorLogService;

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(method = RequestMethod.GET)
	public String display(Principal principal, Model model, HttpSession session) throws ApplicationException {

		@SuppressWarnings("unchecked")
		Map<String, String> userLoginPrincipal = (Map<String, String>) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Employee loginUser = commonCtlr.getLoginUserInfo(userLoginPrincipal.get(Constants.USER_INFO_USERNAME), session);

		List<Employee> employeeList = new ArrayList<>();
		boolean isReadOnlyEmpList = false;

		if (loginUser.getSys_role().equals(Constants.ROLE_MANAGER)) {
			logger.info("User is MANAGER.");

			//employeeList = employeeService.findAll();
			employeeList = employeeService.findByCompanyId(loginUser.getCompany().getId());
			
			isReadOnlyEmpList = false;

		} else if (loginUser.getSys_role().equals(Constants.ROLE_STAFF)) {
			logger.info("User is STAFF.");

			employeeList.add(loginUser);
			isReadOnlyEmpList = true;
		}

		logger.debug(employeeList.toString());
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("readOnlyEmpList", isReadOnlyEmpList);

		return "doorLogs/search_by_managers_staffs";
	}
}
