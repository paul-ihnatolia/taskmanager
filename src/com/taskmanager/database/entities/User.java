package com.taskmanager.database.entities;

import java.io.Serializable;

public class User implements Serializable{
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(int id, String firstname, String lastname, String login) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.login = login;
	}
	private static final long serialVersionUID = 1L;
	private long id;
	private String firstname;
	private String lastname;
	private String login;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}