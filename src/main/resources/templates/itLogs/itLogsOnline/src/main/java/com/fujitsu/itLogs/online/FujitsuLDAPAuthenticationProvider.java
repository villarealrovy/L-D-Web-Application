/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.fujitsu.itLogs.online.service.EmployeeService;
import com.fujitsu.itLogs.online.web.utils.Constants;
import com.fujitsu.login.authentication.Credential;
import com.fujitsu.login.authentication.LoginAuthentication;

/**
 * @author r.monte@ph.fujitsu.com
 * 
 * @version 1.0.0
 *
 *         History: Date(YYYY.MM.DD)|author|revision.
 * 
 *         2017.03.07 | m.cahinod@ph.fujitsu.com | added handling of login errors.
 *         
 */
@Component
public class FujitsuLDAPAuthenticationProvider implements AuthenticationProvider {
	private static Logger logger = LoggerFactory.getLogger(FujitsuLDAPAuthenticationProvider.class);

	@Autowired
	EmployeeService employeeUserService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		logger.info("authenticate -> user:[" + username + "]");

		try {

			if (employeeUserService.findByUsername(username) == null) {
				throw new BadCredentialsException("Username is not Registered.");
			}
		} catch (InvalidDataAccessResourceUsageException e) {
			throw new BadCredentialsException("Database Error Encountered.");
		}

		LoginAuthentication login = new LoginAuthentication();
		Credential credential = new Credential();
		credential.setUsername(username);
		credential.setPassword(password);

		Map<String, String> userLoginPrincipal = new LinkedHashMap<>();
		userLoginPrincipal.put(Constants.USER_INFO_USERNAME, username);
		userLoginPrincipal.put(Constants.USER_INFO_USERPASS, password);

		if (!login.isValidCredential(credential)) {
			throw new BadCredentialsException("Account Authentication Failed.");
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		logger.info("successful authentication.");

		return new UsernamePasswordAuthenticationToken(userLoginPrincipal, password, authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
