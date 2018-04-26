/**
 * Copyright (C) 2015 FUJITSU All rights reserved.
 */

package com.fujitsu.itLogs.batch.util;

public final class Constants {

	/* ROLE TYPE */
	public static final String ROLE_MANAGER = "MANAGER";
	public static final String ROLE_STAFF = "STAFF";

	public static final String STATUS_COMPLETED = "completed";
	public static final String STATUS_ONGOING = "on-going";

	/**
	 * The caller references the constants using <tt>Constants.EMPTY_STRING</tt>
	 * , and so on. Thus, the caller should be prevented from constructing
	 * objects of this class, by declaring this private constructor.
	 */
	private Constants() {
		// this prevents even the native class from
		// calling this constructor as well :
		throw new AssertionError();
	}

}
