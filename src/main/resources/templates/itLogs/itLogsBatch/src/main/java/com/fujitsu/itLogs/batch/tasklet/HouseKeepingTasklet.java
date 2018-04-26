package com.fujitsu.itLogs.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fujitsu.itLogs.batch.service.DoorLogService;

public class HouseKeepingTasklet implements Tasklet {
	@Autowired
	private DoorLogService doorLogService;

	private static Logger logger = LoggerFactory.getLogger(HouseKeepingTasklet.class);

	@Value("${retentionDate}") // get from application.properties
	private String retentionDate;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info("----- START Running HouseKeepingTasklet -------------------");

		doorLogService.deleteOld(retentionDate);

		logger.info("----- END Running HouseKeepingTasklet -------------------");
		return RepeatStatus.FINISHED;
	}

}
