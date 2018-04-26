package com.fujitsu.itLogs.batch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fujitsu.itLogs.batch.model.UploadStatus;
import com.fujitsu.itLogs.batch.repository.UploadStatusRepository;
import com.fujitsu.itLogs.batch.service.UploadStatusService;
import com.fujitsu.itLogs.batch.util.Constants;

/**
 * @author m.cahinod
 *
 */
@Service
public class UploadStatusServiceImpl implements UploadStatusService {

	@Autowired
	UploadStatusRepository uploadStatusRepository;

	@Override
	public UploadStatus save(UploadStatus file) {

		// find first existing
		UploadStatus uploadStatusExisting = uploadStatusRepository.findByFileNameAndYear(file.getFileName(),
				file.getYear());

		if (uploadStatusExisting != null) {
			file.setId(uploadStatusExisting.getId());
		}

		return uploadStatusRepository.save(file);
	}

	@Override
	public List<UploadStatus> findAll() {
		Iterable<UploadStatus> uploadStatus = uploadStatusRepository.findAll();
		List<UploadStatus> uploadStatusList = new ArrayList<>();
		for (UploadStatus uploadStat : uploadStatus) {
			uploadStatusList.add(uploadStat);
		}

		return uploadStatusList;
	}

	@Override
	public UploadStatus find(Long id) {
		return uploadStatusRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		uploadStatusRepository.delete(id);
	}

	@Override
	public UploadStatus findByFileId(Long id) {
		UploadStatus uploadStatus = uploadStatusRepository.findByFileId(id);

		return uploadStatus;
	}

	@Override
	public UploadStatus findByFileNameAndYear(String fileName, String year) {
		UploadStatus uploadStatus = uploadStatusRepository.findByFileNameAndYear(fileName, year);

		return uploadStatus;
	}

	public boolean isFileNameCompleted(String fileName, String year) {
		UploadStatus uploadStatus = uploadStatusRepository.findByFileNameAndYear(fileName, year);

		if (uploadStatus == null) {

			return false;
		}
		return (uploadStatus.getStatus().contains(Constants.STATUS_COMPLETED));
	}

}
