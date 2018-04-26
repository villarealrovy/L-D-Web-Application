package com.fujitsu.itLogs.batch.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The persistent class for the upload_status database table.
 *
 * @author m.cahinod
 */

@Entity
@Table(name = "upload_status")
@NamedQuery(name = "UploadStatus.findAll", query = "SELECT f FROM UploadStatus f")

public class UploadStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String filename;

	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "process_date")
	private Date processDate;

	private String year;

	public UploadStatus() {
	}

	public UploadStatus(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return filename;
	}

	public void setFileName(String fileName) {
		this.filename = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UploadStatus [id=" + id + ", filename=" + filename + ", status=" + status + ", processDate="
				+ processDate + ", year=" + year + "]";
	}

}
