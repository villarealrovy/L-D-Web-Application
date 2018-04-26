/**
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.web.utils;

/**
 * @author r.monte@ph.fujitsu.com
 *
 * @version 1.0.0
 *
 */
public class Constants {

	/* ROLE TYPE */
	public static final String ROLE_MANAGER = "MANAGER";
	public static final String ROLE_STAFF = "STAFF";

	/* SESSION KEYS */
	public static final String USER_INFO = "userInfo";
	public static final String USER_INFO_USERNAME = "username";
	public static final String USER_INFO_USERPASS = "password";

	public static final String STR_NULL = "null";
	public static final String EMPLOYEE_NAME = "Employee Name";
	public static final String FDC_LIST = "FDC Member List";
	public static final String FROM_DATE = "From Date";
	public static final String TO_DATE = "To Date";
	public static final String ONE_YEAR = "One Year";
	public static final String TWO_MONTH = "Two Months";
	public static final String SPACE = " ";
	public static final String DASH = "-";
	public static final String SLASH = "/";
	public static final String COMMA = ",";
	public static final String OPEN_PARENTHESIS = "(";
	public static final String CLOSE_PARENTHESIS = ")";
	public static final String NEWLINE = "newline";

	public static final int INT_LIMIT_ONE_YEAR = 366;
	public static final int INT_LIMIT_TWO_MONTH = 62;
	public static final String ALL_ENTRIES = "All Entries";
	public static final String PAIR_ENTRY = "Pair Entry";

	public static final String NOTICE_TARDINESS = "NOTICE TO EXPLAIN_Tardiness";
	public static final String DOC_EXTENSION = ".docx";

	// Date and Time Formats
	public static final String DATE_FORMAT_DDMMYY = "ddMMyyyy";
	public static final String DATE_FORMAT_DDMMYY_2 = "dd/MM/yyyy";
	public static final String TIME_FORMAT_HHMM = "HH:mm";
	public static final String DAY_FORMAT_EEEE = "EEEE";

	// Notice to Explain Tardiness Placeholders
	public static final String NAME_PLACEHOLDER = "(Name of alleged employee)";
	public static final String DATE_PLACEHOLDER = "(Date)";
	public static final String DETAIL_PLACEHOLDER = "(Detail)";
	public static final String DAYS_PLACEHOLDER = "(Days)";
	public static final String MINUTES_PLACEHOLDER = "(Minutes)";
	public static final String TIME_SCHEDULE_PLACEHOLDER = "(Schedule)";

	// String Format Length
	public static final String STRING_FORMAT_20_L = "%-20s";
	public static final String STRING_FORMAT_40_L = "%-40s";
	public static final String STRING_FORMAT_80_L = "%-80s";

	// Info Messages
	public static final String INFO_MESSAGE_2001 = "info.message.2001";

	// Error Messages
	public static final String ERROR_MESSAGE_1002 = "error.message.1002";
	public static final String ERROR_MESSAGE_1004 = "error.message.1004";
	public static final String ERROR_MESSAGE_1005 = "error.message.1005";

	public static final String ERROR_MESSAGE_2001 = "error.message.2001";
	public static final String ERROR_MESSAGE_2002 = "error.message.2002";
	public static final String ERROR_MESSAGE_2003 = "error.message.2003";

}
