/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.web.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author r.monte@ph.fujitsu.com
 * 
 * @version 1.0.0
 * 
 */
@Configuration
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class AppConfig {

	@Value("${file.fts.upload}")
	private String attachmentDirectory;

	/**
	 * @return the attachmentDirectory
	 */
	public String getAttachmentDirectory() {
		return attachmentDirectory;
	}

}
