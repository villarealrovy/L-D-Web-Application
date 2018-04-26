package com.example.demo.modal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="coursedb")
public class Course {

	@Id
	private int courseid;
	private String course_desc;
	private String start_date;
	private String end_date;
	private String start_time;
	private String end_time;
	private String slot;
	private String instructor;
	private String location;
	
	public Course() {
		
	}
	
	
	public Course(String course_desc, String start_date, String end_date, String start_time, String end_time,
			String slot, String instructor, String location) {
		this.course_desc = course_desc;
		this.start_date = start_date;
		this.end_date = end_date;
		this.start_time = start_time;
		this.end_time = end_time;
		this.slot = slot;
		this.instructor = instructor;
		this.location = location;
	}
	
	
	
	@Override
	public String toString() {
		return "Course [courseid=" + courseid + ", course_desc=" + course_desc + ", start_date=" + start_date
				+ ", end_date=" + end_date + ", start_time=" + start_time + ", end_time=" + end_time + ", slot=" + slot
				+ ", instructor=" + instructor + ", location=" + location + "]";
	}
	
	
	
	
	public int getCourseid() {
		return courseid;
	}
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}
	public String getCourse_desc() {
		return course_desc;
	}
	public void setCourse_desc(String course_desc) {
		this.course_desc = course_desc;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getSlot() {
		return slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	
	
}
