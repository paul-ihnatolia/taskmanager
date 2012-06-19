package com.taskmanager.database.dao;

import java.util.ArrayList;

import com.taskmanager.database.entities.Task;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class TaskDataSource {
	private static final int TASK_COLUMN_ID = 0;
	private static final int TASK_COLUMN_PRIORITY = 1;
	private static final int TASK_COLUMN_AUTHOR = 2;
	private static final int TASK_COLUMN_TIME = 3;
	private static final int TASK_COLUMN_RECIPIENT = 4;
	private static final int TASK_COLUMN_CONTENT = 5;

	private static SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	public TaskDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public long insert(Task task) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.taskAuthor, task.getAuthor());
		cv.put(DatabaseHelper.taskContent, task.getContent());
		cv.put(DatabaseHelper.taskPriority, task.getPriority());
		cv.put(DatabaseHelper.taskRecipient, task.getRecipient());
		cv.put(DatabaseHelper.taskTime, task.getTime());
		return database.insert(DatabaseHelper.taskTable, null, cv);
	}
	public int update(Task task) {
		ContentValues cv=new ContentValues();
		cv.put(DatabaseHelper.taskAuthor, task.getAuthor());
		cv.put(DatabaseHelper.taskContent, task.getContent());
		cv.put(DatabaseHelper.taskPriority, task.getPriority());
		cv.put(DatabaseHelper.taskRecipient, task.getRecipient());
		cv.put(DatabaseHelper.taskTime, task.getTime());
		return database.update(DatabaseHelper.taskTable, cv, DatabaseHelper.taskID + " = ?", new String[] {String.valueOf(task.getId()) });
	}
	
	public int deleteAll() {
		return database.delete(DatabaseHelper.taskTable, null, null);
	}
	
	public void delete(long id) {
		database.delete(DatabaseHelper.taskTable, DatabaseHelper.taskID + " = ?", new String[] { String.valueOf(id) });
	}
	
	public Task select(long id) {
		Cursor mCursor = database.query(DatabaseHelper.taskTable, null, DatabaseHelper.taskID + " = ?", new String[] {String.valueOf(id)}, null, null, DatabaseHelper.userFirstname);
		 
		mCursor.moveToFirst();
		String author = mCursor.getString(TASK_COLUMN_AUTHOR); 
		String content = mCursor.getString(TASK_COLUMN_CONTENT);
		int priority = mCursor.getInt(TASK_COLUMN_PRIORITY); 
		String recipient = mCursor.getString(TASK_COLUMN_RECIPIENT);
		long time = mCursor.getLong(TASK_COLUMN_TIME); 
		mCursor.close();
		return new Task(id, priority, author, time, recipient, content);
	}
	public ArrayList<Task> selectAll() {
		Cursor mCursor = database.query(DatabaseHelper.userTable, null, null, null, null, null, DatabaseHelper.userTable);
		 
		ArrayList<Task> arr = new ArrayList<Task>();
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {	
			do {
				long id = mCursor.getLong(TASK_COLUMN_ID);
				String author = mCursor.getString(TASK_COLUMN_AUTHOR); 
				String content = mCursor.getString(TASK_COLUMN_CONTENT);
				int priority = mCursor.getInt(TASK_COLUMN_PRIORITY); 
				String recipient = mCursor.getString(TASK_COLUMN_RECIPIENT);
				long time = mCursor.getLong(TASK_COLUMN_TIME); 
				arr.add(new Task(id, priority, author, time, recipient, content));
				
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return arr;
	}	  
	
/*	public User selectByLogin(String login) {
		Cursor mCursor = database.query(DatabaseHelper.userTable, null, DatabaseHelper.userLogin + " = ?", new String[] {String.valueOf(login)}, null, null, DatabaseHelper.userFirstname);
		 
		mCursor.moveToFirst();
		long id = mCursor.getLong(USER_COLUMN_ID);
		String firstname = mCursor.getString(USER_COLUMN_FIRSTNAME); 
		String lastname = mCursor.getString(USER_COLUMN_LASTNAME);
		mCursor.close();
		return new User(id, firstname, lastname, login);
	}*/


}
