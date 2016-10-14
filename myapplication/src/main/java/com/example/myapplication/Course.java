package com.example.myapplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Course {

	private String name;
	private String shortDescription = "";
	private String moreInformation = "";
	private String time = "";
	private String location = "";
	private ArrayList<CourseSession> courseSessions;
	private Comment[] comments;
	
	public Course(String name){
		this.name = name;
		courseSessions = new ArrayList<CourseSession>();
	}
	
	/*
	 * start parameter is inclusive. end parameter is exclusive.
	 */
	public ArrayList<CourseSession> getCourseSessionsInInterval(LocalDateTime start, LocalDateTime end){
		ArrayList<CourseSession> inInterval = new ArrayList<CourseSession>();
		CourseSession courseSession;
		for(int i = 0; i < courseSessions.size(); i++){
			courseSession = courseSessions.get(i);
			if(courseSession.getTime().compareTo(start) >= 0 &&
					courseSession.getTime().compareTo(end) < 0){
				inInterval.add(courseSessions.get(i));
			}
		}
		return inInterval;
	}
	
	public void addSession(CourseSession cs){
		if(!courseSessions.contains(cs)){
			courseSessions.add(cs);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getMoreInformation() {
		return moreInformation;
	}

	public void setMoreInformation(String moreInformation) {
		this.moreInformation = moreInformation;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ArrayList<CourseSession> getCourseSessions() {
		return courseSessions;
	}

	public void setCourseSessions(ArrayList<CourseSession> courseSessions) {
		this.courseSessions = courseSessions;
	}
	
}
