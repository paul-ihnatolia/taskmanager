package com.taskmanager.database.entities;

import java.io.Serializable;

public class Task implements Serializable{
	public Task() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Task(long id, int priority, String author, String time,
			String recipient, String content, String complete) {
		super();
		this.id = id;
		this.priority = priority;
		this.author = author;
		this.time = time;
		this.recipient = recipient;
		this.content = content;
		this.complete = complete;
	}
	private static final long serialVersionUID = 1L;
	private long id;
	private int priority;
	private String author;
	private String time;
	private String recipient;
	private String content;
	private String complete;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getComplete() {
		return complete;
	}
	public void setComplete(String complete) {
		this.complete = complete;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}