/**
 * Copyright (C) 2017 FUJITSU LIMITED All rights reserved.
 */
package com.fujitsu.itLogs.online.web.utils;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author r.monte@ph.fujitsu.com
 *
 * @version 1.0.0
 *
 */
@Configuration
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class ITLogsMailConfiguration {

	@Value("${mail.protocol}")
	private String protocol;

	@Value("${mail.host}")
	private String host;

	@Value("${mail.port}")
	private int port;

	@Value("${mail.smtp.auth}")
	private boolean auth;

	@Value("${mail.smtp.starttls.enable}")
	private boolean starttls;

	// @Value("${mail.from}")
	// private String from;

	@Value("${mail.to}")
	private String to;

	// @Value("${mail.username}")
	// private String username;
	//
	// @Value("${mail.password}")
	// private String password;

	@Value("${mail.domain}")
	private String domain;

	// /**
	// * @return the from
	// */
	// public String getFrom() {
	// return from;
	// }

	/**
	 * @return the to
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	private JavaMailSenderImpl mailSender;

	@Bean
	public JavaMailSender javaMailSender() {
		// JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		this.mailSender = new JavaMailSenderImpl();

		Properties mailProperties = new Properties();
		mailProperties.put("mail.smtp.host", host);
		mailProperties.put("mail.smtp.auth", auth);
		mailProperties.put("mail.smtp.starttls.enable", starttls);

		mailSender.setJavaMailProperties(mailProperties);
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setProtocol(protocol);
		// mailSender.setUsername(username);
		// mailSender.setPassword(password);

		return mailSender;
	}

	/**
	 * @return the mailSender
	 */
	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

}
