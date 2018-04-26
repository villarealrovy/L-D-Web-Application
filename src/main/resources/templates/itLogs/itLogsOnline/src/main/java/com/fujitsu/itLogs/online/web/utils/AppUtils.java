/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.web.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author a.serrano_d@ph.fujitsu.com
 *
 * @version 1.0.0
 *
 */
public class AppUtils {

	/**
	 * Format date to MONTH DD, YYYY. example : JANUARY 26, 2015
	 *
	 * @param date
	 * @return
	 */
	public static String formatDateMMMMMDDYYYY(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy");
		if (date == null) {
			return null;
		}
		return sdf.format(date).toUpperCase();
	}

	public static long getCurrentDatetime() {
		return (new Date()).getTime();
	}

	public static Date parseDatePickerValue(String date) {
		Date parsedDate = new Date();
		try {
			if (date != null) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMMM-yyyy");
				parsedDate = formatter.parse(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return parsedDate;
	}

	public static String formatDateToDatePicker(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMMM-yyyy");
		if (date == null) {
			return "";
		}
		return formatter.format(date).toUpperCase();
	}

	public static Long convertStringDateToLong(String sDate) {
		long dateInLong = new Date().getTime();

		try {
			DateFormat formatter = new SimpleDateFormat("dd-MMMMM-yyyy hh:mm a");
			if (!sDate.contains(" ")) {
				formatter = new SimpleDateFormat("dd-MMMMM-yyyy");
			}

			Date date = formatter.parse(sDate);
			dateInLong = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dateInLong;
	}

	/**
	 * Get the time of interview on the database and format
	 *
	 * @param date
	 *            - interview date and time
	 * @return - formatted time - date is removed
	 *
	 * @author r.saldua
	 */
	public static String getTimeOfDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
		if (date == null) {
			return null;
		}
		return sdf.format(date).toUpperCase();
	}

	/**
	 *
	 * @param domainObject
	 *            - domain object to check
	 * @return - boolean - true or false
	 *
	 * @author r.saldua
	 */
	public static boolean checkIfDomainIsNotNull(Object domainObject) {
		boolean result = false;
		if (domainObject != null) {
			result = true;
		}
		return result;
	}

	public static long getElapsedDays(Date date) {

		long diff = getCurrentDatetime() - date.getTime();

		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		System.out.print(diffDays + " days, ");
		System.out.print(diffHours + " hours, ");
		System.out.print(diffMinutes + " minutes, ");
		System.out.print(diffSeconds + " seconds.");

		return diffDays;
	}
}
