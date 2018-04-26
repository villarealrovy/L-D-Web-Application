/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */

package com.fujitsu.itLogs.batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * @author r.monte@ph.fujitsu.com
 * 
 * @version 1.0.0
 *
 *         History: Date(YYYY.MM.DD)|author|revision.
 * 
 *         2017.04.07 | r.abella@ph.fujitsu.com | added handling of DB retention period of logs.
 *         
 */
@SpringBootApplication
public class ItLogsBatchApplication {

	private static Logger logger = LoggerFactory.getLogger(ItLogsBatchApplication.class);

	public static void main(String[] args) throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, IOException {

		// if NOT spring.batch.job.enabled=false
		// SpringApplication.run(ItLogsBatchApplication.class, args);

		String isReUploadTemp = "false";
		if (args.length > 0) {
			if (args[0].equals("reUpload")) {
				isReUploadTemp = "true";

			}
		}
		System.out.println("check " + isReUploadTemp);

		String isReUpload = isReUploadTemp;

		// running if spring.batch.job.enabled=false
		SpringApplication app = new SpringApplication(ItLogsBatchApplication.class);
		ConfigurableApplicationContext ctx = app.run(args);
		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);

		// run itLogsBatchEmployeeRegisterJob
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date date = new Date(timestamp.getTime());
		JobParameters jobParameters = new JobParametersBuilder().addDate("date", date).toJobParameters();
		Job itLogsBatchEmployeeRegisterJob = ctx.getBean("itLogsBatchEmployeeRegisterJob", Job.class);
		jobLauncher.run(itLogsBatchEmployeeRegisterJob, jobParameters);

		// run itLogsBatchDoorLogLoadJob
		String sharedFolderPath = ctx.getEnvironment().getProperty("sharedFolderPath");
		logger.info("Inspecting sharedFolder[{}]", sharedFolderPath);

		try (Stream<Path> paths = Files.walk(Paths.get(sharedFolderPath))) {

			paths.filter(filePath -> filePath.toFile().isFile()).forEach(filePath -> {

				Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
				Date date2 = new Date(timestamp2.getTime());

				JobParameters jobParameters2 = new JobParametersBuilder().addString("filePath", filePath.toString())
						.addString("reUpload", isReUpload).addDate("date", date2).toJobParameters();
				// isReUpload

				Job itLogsBatchDoorLogLoadJob = ctx.getBean("itLogsBatchDoorLogLoadJob", Job.class);

				// handle error to check all files
				try {
					jobLauncher.run(itLogsBatchDoorLogLoadJob, jobParameters2);
				} catch (Exception e) {
					// logger.error("Encountered Error: " + e);

				}

			});

		} catch (Exception ex) {
			// logger.error("Encountered Error: " + ex);
		}

		// run itLogsBatchHouseKeepingLoadJob
		jobParameters = new JobParametersBuilder().addDate("date", date).toJobParameters();
		Job itLogsBatchHouseKeepingLoadJob = ctx.getBean("itLogsBatchHouseKeepingLoadJob", Job.class);
		jobLauncher.run(itLogsBatchHouseKeepingLoadJob, jobParameters);

	}
}
