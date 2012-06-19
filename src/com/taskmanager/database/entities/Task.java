package com.taskmanager.database.entities;

import java.io.Serializable;

public class Task implements Serializable{
	public Task(long id, int priority, String author, long time,
			String recipient, String content) {
		super();
		this.id = id;
		this.priority = priority;
		this.author = author;
		this.time = time;
		this.recipient = recipient;
		this.content = content;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private int priority;
	private String author;
	private long time;
	private String recipient;
	private String content;
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
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}