/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author r.monte@ph.fujitsu.com
 *
 * @version 1.0.0
 *
 */
@Controller
@RequestMapping("/itLogs")
public class MainController {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(MainController.class);

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String display(Principal principal, Model model, HttpSession session) {
		return "login";
	}

}