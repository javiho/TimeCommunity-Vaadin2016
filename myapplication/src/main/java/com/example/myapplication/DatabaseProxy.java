package com.example.myapplication;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

//This class is only for testing and can be replaced by a class which has access to the database.
public class DatabaseProxy {
	
	private Course[] courses;
	
	public DatabaseProxy(){
		courses = createCourses();
		createCourseSessions();
	}
	
	public Course[] getCourses(){
		return courses;
	}
	
	private Course[] createCourses(){
		Course c1 = new Course("Test Course");
		Course c2 = new Course("Golf Course");
		return new Course[]{c1, c2};
	}

	private void createCourseSessions(){
		LocalDateTime[] dts = {
				LocalDateTime.of(2016, Month.OCTOBER, 12, 14, 0),
				LocalDateTime.of(2016, Month.OCTOBER, 13, 14, 0),
				LocalDateTime.of(2016, Month.OCTOBER, 14, 14, 0)
		};
		CourseSession[] css = {
				new CourseSession(courses[0], dts[0], 2),
				new CourseSession(courses[0], dts[1], 2),
				new CourseSession(courses[1], dts[2], 4)
		};
	}
}
