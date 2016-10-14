package com.example.myapplication;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Comment {

	private String content;
	private LocalDateTime publishedTime;
	Comment[] replies;
	private User writer;
	private Course course;
	
}
