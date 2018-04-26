package com.example.demo.services;

import com.example.demo.modal.Course;

public interface CourseSvc {
    //ListList<Notes> findAll();

    Course findOne(int courseid);

    Course saveCourse(Course course);

    void deleteCourse(int courseid);
}
