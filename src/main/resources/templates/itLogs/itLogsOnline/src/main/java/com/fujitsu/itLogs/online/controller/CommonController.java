/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fujitsu.itLogs.online.model.Employee;
import com.fujitsu.itLogs.online.service.EmployeeService;
import com.fujitsu.itLogs.online.web.utils.Constants;

/**
 * @author r.monte@ph.fujitsu.com
 *
 * @version 1.0.0
 *
 */
@Service
public class CommonController {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	EmployeeService employeeUserService;

	public Employee getLoginUserInfo(String username, HttpSession session) {

		Employee loginUser = employeeUserService.findByUsername(username);

//		 if (loginUser == null) {
//			 //for testing if devtools in pom.xml is activated
//			 loginUser = new Employee();
//			 loginUser.setEmpNo(160961);
//			 loginUser.setFullname("Monte, Randy");
//			 loginUser.setUsername("r.monte");
//			 loginUser.setId(1L);
//			 loginUser.setRole(Constants.ROLE_MANAGER);
//			 //loginUser.setRole(Constants.ROLE_STAFF);
//		 }

		session.setAttribute(Constants.USER_INFO, loginUser);

		return loginUser;
	}

}
