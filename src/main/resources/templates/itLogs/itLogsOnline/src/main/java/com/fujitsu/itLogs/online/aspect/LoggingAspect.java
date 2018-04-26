/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author r.monte@ph.fujitsu.com
 *
 * @version 1.0.0
 * 
 */
@Aspect
@Component
public class LoggingAspect {
	private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Pointcut("within(com.fujitsu..*)")
	private void logAnyFunctionWithinResource() {
	}

	// After -> Any Function Within Resource
	// throws an exception ...Log it
	@AfterThrowing(pointcut = "logAnyFunctionWithinResource()", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
		logger.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
		logger.error("Cause : " + exception.getCause(), exception);
	}

	@Before("execution(public * com.fujitsu.itLogs.online.*.*.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		logger.info(className + "." + methodName + " begin");
	}

	@After("execution(public * com.fujitsu.itLogs.online.*.*.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		logger.info(className + "." + methodName + " end");
	}
}
