/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fujitsu.itLogs.online.model.Employee;
import com.fujitsu.itLogs.online.service.EmployeeService;
//import com.fujitsu.itLogs.online.model.Employee;
import com.fujitsu.itLogs.online.web.utils.Constants;

/**
 * @author y.umayam@ph.fujitsu.com
 *
 * @version 1.0.0
 *
 */
@Controller
@RequestMapping("/itLogs/advanceSearch")
public class AdvanceSearchController {
	private static Logger logger = LoggerFactory.getLogger(AdvanceSearchController.class);

	@Autowired
	private CommonController commonCtlr;

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(method = RequestMethod.GET)
	public String display(Principal principal, Model model, HttpSession session) {

		@SuppressWarnings("unchecked")
		Map<String, String> userLoginPrincipal = (Map<String, String>) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Employee loginUser = commonCtlr.getLoginUserInfo(userLoginPrincipal.get(Constants.USER_INFO_USERNAME), session);

		if (loginUser.getSys_role().equals(Constants.ROLE_MANAGER)) {
			logger.info("User is MANAGER.");

		}

		if (loginUser.getSys_role().equals(Constants.ROLE_STAFF)) {
			logger.info("User is STAFF.");

		}

		List<Employee> employeeList = new ArrayList<>();
		
		//employeeList = employeeService.findAll();
		employeeList = employeeService.findByCompanyId(loginUser.getCompany().getId());

		logger.debug(employeeList.toString());

		model.addAttribute("employeeList", employeeList);


		return "doorLogs/advance_search";
	}
}
