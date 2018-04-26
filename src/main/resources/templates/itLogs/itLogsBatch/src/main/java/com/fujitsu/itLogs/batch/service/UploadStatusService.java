/*
 * Copyright (C) 2015 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.batch.service;

import java.util.List;

import com.fujitsu.itLogs.batch.model.UploadStatus;

/**
 * @author m.cahinod
 *
 */

public interface UploadStatusService {

	UploadStatus save(UploadStatus uploadStatus);

	List<UploadStatus> findAll();

	UploadStatus find(Long id);

	void delete(Long id);

	UploadStatus findByFileId(Long id);

	UploadStatus findByFileNameAndYear(String fileName, String year);

	boolean isFileNameCompleted(String fileName, String year);

}
