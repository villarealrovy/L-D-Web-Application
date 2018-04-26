package com.fujitsu.itLogs.batch.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fujitsu.itLogs.batch.model.UploadStatus;
import com.fujitsu.itLogs.batch.service.UploadStatusService;
import com.fujitsu.itLogs.batch.util.Constants;

@ActiveProfiles("mysql")
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional

public class UploadStatusServiceImplTest {

	private static final String FILENAME_TEST = "filenametest1.xlsx";
	private static final String YEAR_TEST = "2014";

	@Autowired
	private UploadStatusService uploadStatusService;

	@Test
	public void testIsFileNameCompleted() {
		UploadStatus uploadStatusRaw = createUploadStatus(FILENAME_TEST, YEAR_TEST);

		uploadStatusService.save(uploadStatusRaw);

		boolean actual = uploadStatusService.isFileNameCompleted(FILENAME_TEST, YEAR_TEST);
		assertEquals(true, actual);

	}

	@Test
	public void testIsFileNameCompletedMissingFilename() {
		boolean actual = uploadStatusService.isFileNameCompleted("Missing.xlsx", "2013");
		assertEquals(false, actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFindByFileNameAndYear() {

		UploadStatus uploadStatusRaw = createUploadStatus(FILENAME_TEST, YEAR_TEST);

		uploadStatusService.save(uploadStatusRaw);
		System.out.println("Succesfully Added");

		UploadStatus uploadActual = uploadStatusService.findByFileNameAndYear(FILENAME_TEST, YEAR_TEST);

		assertEquals(uploadStatusRaw.getFileName(), uploadActual.getFileName());
		assertTrue(uploadActual.getId() != null);
		assertEquals(uploadStatusRaw.getProcessDate().getDate(), uploadActual.getProcessDate().getDate());
		assertEquals(uploadStatusRaw.getStatus(), uploadActual.getStatus());
		assertEquals(uploadStatusRaw.getYear(), uploadActual.getYear());

		// check case when item to save is already existing----START
		uploadStatusService.save(uploadStatusRaw);
		System.out.println("Succesfully Added");

		uploadActual = uploadStatusService.findByFileNameAndYear(FILENAME_TEST, YEAR_TEST);

		assertEquals(uploadStatusRaw.getFileName(), uploadActual.getFileName());
		assertTrue(uploadActual.getId() != null);
		assertEquals(uploadStatusRaw.getProcessDate(), uploadActual.getProcessDate());
		assertEquals(uploadStatusRaw.getStatus(), uploadActual.getStatus());
		assertEquals(uploadStatusRaw.getYear(), uploadActual.getYear());
		// check case when item to save is already existing----END

		List<UploadStatus> uploadStatusActualList = uploadStatusService.findAll();
		assertTrue(uploadStatusActualList != null);

		UploadStatus uploadActualFind = uploadStatusService.find(uploadActual.getId());
		System.out.println("This is uploadActual: " + uploadActual.toString());
		System.out.println("This is uploadActualFind: " + uploadActualFind.toString());
		assertEquals(uploadActualFind.getFileName(), uploadStatusRaw.getFileName());

		UploadStatus uploadActualFindById = uploadStatusService.findByFileId(uploadActual.getId());
		assertEquals(uploadActualFindById.getFileName(), uploadStatusRaw.getFileName());

		// UploadStatus uploadStatusExisting =
		// uploadStatusService.uploadStatusExisting(uploadStatusRaw.getId());

	}

	/**
	 * TODO: Describe this method
	 * 
	 * @author r.abella
	 * @param newProcess1
	 * 
	 */
	private UploadStatus createUploadStatus(String fileName, String year) {

		UploadStatus uploadStatus = new UploadStatus();

		uploadStatus.setId((long) 122);
		uploadStatus.setFileName(fileName);
		uploadStatus.setYear(year);
		uploadStatus.setStatus(Constants.STATUS_COMPLETED);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date date = new Date(timestamp.getTime());
		uploadStatus.setProcessDate(date);

		return uploadStatus;
	}
	
	
	@Test
	public void testTimeStamp() throws ParseException {
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		Date timeLog = null;
		
		timeLog = timeFormat.parse("12:02:41");
		
		System.out.println("timelog is " + timeLog.toString());
		
		//this is correct
		SimpleDateFormat timeFormat2 = new SimpleDateFormat("HH:mm:ss");
		Date timeLog2 = null;
		
		timeLog2 = timeFormat2.parse("12:02:41");
		
		System.out.println("timelog2 is " + timeLog2.toString());
		
	}	

}
