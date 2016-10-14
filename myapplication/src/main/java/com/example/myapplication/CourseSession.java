package com.example.myapplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class CourseSession {

	private LocalDateTime time; //start time
	private int durationHours;
	private String location = "";
	private Course course;
	
	/*
	 * Pre-condition: durationHours > 0;
	 */
	public CourseSession(Course course, LocalDateTime dt, int durationHours){
		this.course = course;
		this.time = dt;
		this.durationHours = durationHours;
		course.addSession(this);
	}
	
	public CourseSession(Course course, LocalDateTime dt, int durationHours, String location){
		this.course = course;
		this.time = dt;
		this.durationHours = durationHours;
		this.location = location;
		course.addSession(this);
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public int getDurationHours() {
		return durationHours;
	}

	public void setDurationHours(int durationHours) {
		this.durationHours = durationHours;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Course getCourse() {
		return course;
	}

	/*public void setCourse(Course course) {
		this.course = course;
	}*/
	
	
}
