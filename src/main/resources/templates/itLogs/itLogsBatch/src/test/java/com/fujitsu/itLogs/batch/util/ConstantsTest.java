package com.fujitsu.itLogs.batch.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class ConstantsTest {

	/**
	 * INIT() method
	 * 
	 * @throws Exception
	 *             the Exception
	 */
	@Before
	public void init() throws Exception {

	}

	/**
	 * getConstantsGood() method
	 * 
	 */
	@Test
	public void getConstantsGood() {

		String ongoing = "on-going";

		Assert.assertEquals(ongoing, Constants.STATUS_ONGOING);

	}

	/**
	 * testValidatesNotInstantiable() on Constants class
	 * 
	 * @throws Exception
	 *             Exception
	 */
	@Test
	public void testValidatesNotInstantiable() throws Exception {

		try {
			Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
			assertTrue(Modifier.isPrivate(constructor.getModifiers()));
			constructor.setAccessible(true);
			constructor.newInstance();

		} catch (InvocationTargetException exception) {

			System.out.println(exception.getCause());
			assertEquals(java.lang.AssertionError.class, exception.getCause().getClass());
		}

	}




}
