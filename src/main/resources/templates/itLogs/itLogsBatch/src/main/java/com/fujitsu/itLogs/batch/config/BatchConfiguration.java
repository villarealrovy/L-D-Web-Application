package com.fujitsu.itLogs.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fujitsu.itLogs.batch.tasklet.HouseKeepingTasklet;
import com.fujitsu.itLogs.batch.tasklet.LoadDoorLogsTasklet;
import com.fujitsu.itLogs.batch.tasklet.LoadEmployeeTasklet;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilders;

	@Autowired
	private StepBuilderFactory stepBuilders;

	@Bean
	public Job itLogsBatchEmployeeRegisterJob() {
		return jobBuilders.get("itLogsBatchEmployeeRegisterJob").start(step1()).build();
	}

	@Bean
	public Job itLogsBatchDoorLogLoadJob() {
		return jobBuilders.get("itLogsBatchDoorLogLoadJob").start(step2()).build();
	}

	@Bean
	public Job itLogsBatchHouseKeepingLoadJob() {
		return jobBuilders.get("itLogsBatchHouseKeepingLoadJob").start(step3()).build();
	}

	@Bean
	public Step step1() {
		return stepBuilders.get("step1").tasklet(tasklet1()).allowStartIfComplete(true).build();
	}

	@Bean
	public Step step2() {
		return stepBuilders.get("step2").tasklet(tasklet2()).allowStartIfComplete(true).build();
	}

	@Bean
	public Step step3() {
		return stepBuilders.get("step3").tasklet(tasklet3()).allowStartIfComplete(true).build();
	}

	@Bean
	public Tasklet tasklet1() {
		return new LoadEmployeeTasklet();
	}

	@Bean
	public Tasklet tasklet2() {
		return new LoadDoorLogsTasklet();
	}

	@Bean
	public Tasklet tasklet3() {
		return new HouseKeepingTasklet();
	}

}
