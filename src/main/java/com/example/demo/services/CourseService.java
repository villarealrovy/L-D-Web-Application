package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.modal.Course;

import com.example.demo.repository.CourseRepository;


@Service
@Transactional
public class CourseService {

	private final CourseRepository courserepository;
	
	
	public CourseService(CourseRepository courserepository) {
		this.courserepository=courserepository;
	}
	public void saveMyCourse(Course course) {
		courserepository.save(course);
	}
	
	public List<Course> showAllCourses(){
		List<Course> courses = new ArrayList<Course>();
		for(Course course:courserepository.findAll()) {
			courses.add(course);
		
	}
		return courses;
	}
	
	public Course editCourse(int courseid) {
		return courserepository.findById(courseid).orElse(null);
	}
	public void deleteCourse(int courseid) {
		courserepository.deleteById(courseid);
	}
	
	
}

