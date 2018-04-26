package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.modal.Course;

public interface CourseRepository extends CrudRepository<Course, Integer > {

}
