/*
 * Copyright (C) 2015 FUJITSU All rights reserved.
 */

package com.fujitsu.itLogs.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fujitsu.itLogs.batch.model.UploadStatus;

//Enter import model here.

/**
 * @author m.cahinod
 *
 */

public interface UploadStatusRepository extends JpaRepository<UploadStatus, Long> {

	@Query("SELECT f FROM UploadStatus f WHERE f.id = :id")
	UploadStatus findByFileId(@Param("id") Long id);

	@Query("SELECT f FROM UploadStatus f WHERE f.filename = :filename and f.year = :year")
	UploadStatus findByFileNameAndYear(@Param("filename") String fileName, @Param("year") String year);

}
