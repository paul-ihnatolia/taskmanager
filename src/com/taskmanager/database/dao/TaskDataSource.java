package com.taskmanager.database.dao;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.taskmanager.database.entities.Task;


public class TaskDataSource {
	private static final int TASK_COLUMN_ID = 0;
	private static final int TASK_COLUMN_PRIORITY = 1;
	private static final int TASK_COLUMN_AUTHOR = 2;
	private static final int TASK_COLUMN_TIME = 3;
	private static final int TASK_COLUMN_RECIPIENT = 4;
	private static final int TASK_COLUMN_CONTENT = 5;
	private static final int TASK_COLUMN_COMPLETE = 6;
	private static final int TASK_COLUMN_SERVERID = 7;
	private static final int TASK_COLUMN_OWNER = 8;
	
	private static SQLiteDatabase db;
	private static DatabaseHelper dbHelper;
	
	public TaskDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void addItem(Task task) {
		String sql = "insert into tasks values('" + task.getId() + "','" +task.getPriority() +"','" + task.getAuthor() +  "','" + task.getTime() +"','" + task.getRecipient() + "','" + task.getContent() +"','" + task.getComplete()+ "','" + task.getServerId() + "');";
		db.execSQL(sql);
	}

	public long insert(Task task) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.taskPriority, task.getPriority());
		cv.put(DatabaseHelper.taskAuthor, task.getAuthor());
		cv.put(DatabaseHelper.taskTime, task.getTime());
		cv.put(DatabaseHelper.taskRecipient, task.getRecipient());
		cv.put(DatabaseHelper.taskContent, task.getContent());
		cv.put(DatabaseHelper.taskComplete, task.getComplete());
		cv.put(DatabaseHelper.taskServerId, task.getServerId());
		cv.put(DatabaseHelper.taskOwner, task.getOwner());
		return db.insert(DatabaseHelper.taskTable, null, cv);
	}
	
	public int update(Task task) {
		ContentValues cv=new ContentValues();
		cv.put(DatabaseHelper.taskPriority, task.getPriority());
		cv.put(DatabaseHelper.taskAuthor, task.getAuthor());
		cv.put(DatabaseHelper.taskTime, task.getTime());
		cv.put(DatabaseHelper.taskRecipient, task.getRecipient());
		cv.put(DatabaseHelper.taskContent, task.getContent());
		cv.put(DatabaseHelper.taskComplete, task.getComplete());
		cv.put(DatabaseHelper.taskServerId, task.getServerId());
		cv.put(DatabaseHelper.taskOwner, task.getOwner());
		
		return db.update(DatabaseHelper.taskTable, cv, DatabaseHelper.taskID + " = ?", new String[] {String.valueOf(task.getId()) });
	}
	
	public int deleteAll() {
		return db.delete(DatabaseHelper.taskTable, null, null);
	}
		
	public ArrayList<Task> getTask(String login){
		ArrayList<Task> taskList = new ArrayList<Task>();
		Cursor mCursor = db.query(DatabaseHelper.taskTable, null, DatabaseHelper.taskAuthor + " = ?",  new String[] {login}, null, null, null);
		
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {	
			do {
				long id = mCursor.getLong(TASK_COLUMN_ID);
				String author = mCursor.getString(TASK_COLUMN_AUTHOR); 
				String content = mCursor.getString(TASK_COLUMN_CONTENT);
				int priority = mCursor.getInt(TASK_COLUMN_PRIORITY); 
				String recipient = mCursor.getString(TASK_COLUMN_RECIPIENT);
				String time = mCursor.getString(TASK_COLUMN_TIME);
				String complete = mCursor.getString(TASK_COLUMN_COMPLETE);
				int serverId = mCursor.getInt(TASK_COLUMN_SERVERID);
				String owner = mCursor.getString(TASK_COLUMN_OWNER);
				taskList.add(new Task(id, priority, author, time, recipient, content, complete, serverId, owner));
				
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		
		return taskList;
	}
	public ArrayList<Task> getRecipientAll(String login){
		ArrayList<Task> vuk = new ArrayList<Task>();
		ArrayList<Task> neVuk = new ArrayList<Task>();
		Cursor mCursor = db.query(DatabaseHelper.taskTable, null, DatabaseHelper.taskRecipient + " = ?" + " AND " + DatabaseHelper.taskOwner + " = ?",  new String[] {login, login}, null, null, null);
		
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) {	
			do {
				long id = mCursor.getLong(TASK_COLUMN_ID);
				String author = mCursor.getString(TASK_COLUMN_AUTHOR); 
				String content = mCursor.getString(TASK_COLUMN_CONTENT);
				int priority = mCursor.getInt(TASK_COLUMN_PRIORITY); 
				String recipient = mCursor.getString(TASK_COLUMN_RECIPIENT);
				String time = mCursor.getString(TASK_COLUMN_TIME);
				String complete = mCursor.getString(TASK_COLUMN_COMPLETE);
				int serverId = mCursor.getInt(TASK_COLUMN_SERVERID);
				String owner = mCursor.getString(TASK_COLUMN_OWNER);
				
				Task task = new Task(id, priority, author, time, 
						recipient, content, complete, serverId, owner);
				
				if(complete.equals("true")){
					vuk.add(task);
				}else{
					neVuk.add(task);
				}
				
			} while (mCursor.moveToNext());
		}
		
		mCursor.close();
		
		Collections.reverse(neVuk);
		Collections.reverse(vuk);
		neVuk.addAll(vuk);
		Log.i("tds", neVuk.toString());
		return neVuk;
	}
	public ArrayList<Task> selectAll() {
		Cursor mCursor = db.query(DatabaseHelper.taskTable, null, null, null, null, null, null);
		 
		ArrayList<Task> vuk = new ArrayList<Task>();
		ArrayList<Task> neVuk = new ArrayList<Task>();
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {	
			do {
				long id = mCursor.getLong(TASK_COLUMN_ID);
				String author = mCursor.getString(TASK_COLUMN_AUTHOR); 
				String content = mCursor.getString(TASK_COLUMN_CONTENT);
				int priority = mCursor.getInt(TASK_COLUMN_PRIORITY); 
				String recipient = mCursor.getString(TASK_COLUMN_RECIPIENT);
				String time = mCursor.getString(TASK_COLUMN_TIME);
				String complete = mCursor.getString(TASK_COLUMN_COMPLETE);
				int serverId = mCursor.getInt(TASK_COLUMN_SERVERID);
				String owner = mCursor.getString(TASK_COLUMN_OWNER);
				
				Task task = new Task(id, priority, author, time, 
						recipient, content, complete, serverId, owner);
				
				if(complete.equals("true")){
					vuk.add(task);
				}else{
					neVuk.add(task);
				}
				
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		neVuk.addAll(vuk);
		Log.i("tds", neVuk.toString());
		return neVuk;
	}
	
	public ArrayList<Task> getOwnerAndAuthor(String ownerLogin, String authorLogin){
		  ArrayList<Task> taskList = new ArrayList<Task>();
		  Cursor mCursor = db.query(DatabaseHelper.taskTable, null, DatabaseHelper.taskOwner + " = ?" + " AND " + DatabaseHelper.taskAuthor + " = ?",  new String[] {ownerLogin, authorLogin}, null, null, null);
		  
		  mCursor.moveToFirst();
		  
		  if (!mCursor.isAfterLast()) { 
			  do {
				    long id = mCursor.getLong(TASK_COLUMN_ID);
				    String author = mCursor.getString(TASK_COLUMN_AUTHOR); 
				    String content = mCursor.getString(TASK_COLUMN_CONTENT);
				    int priority = mCursor.getInt(TASK_COLUMN_PRIORITY); 
				    String recipient = mCursor.getString(TASK_COLUMN_RECIPIENT);
				    String time = mCursor.getString(TASK_COLUMN_TIME);
				    String complete = mCursor.getString(TASK_COLUMN_COMPLETE);
				    int serverId = mCursor.getInt(TASK_COLUMN_SERVERID);
				    String owner = mCursor.getString(TASK_COLUMN_OWNER);

				    taskList.add(new Task(id, priority, author, time, recipient, content, complete, serverId, owner));
			    
			  } while (mCursor.moveToNext());
		  }
		  mCursor.close();
		  		  
		  Collections.reverse(taskList);
		  return taskList;
	}
	public ArrayList<Task> getAuthorAndRecipient(String authorLogin, String recipientLogin){
		  ArrayList<Task> taskList = new ArrayList<Task>();
		  Cursor mCursor = db.query(DatabaseHelper.taskTable, null, DatabaseHelper.taskAuthor + " = ?" + " AND " + DatabaseHelper.taskRecipient + " = ?",  new String[] {authorLogin, recipientLogin}, null, null, null);
		  
		  mCursor.moveToFirst();
		  
		  if (!mCursor.isAfterLast()) { 
			  do {
				    long id = mCursor.getLong(TASK_COLUMN_ID);
				    String author = mCursor.getString(TASK_COLUMN_AUTHOR); 
				    String content = mCursor.getString(TASK_COLUMN_CONTENT);
				    int priority = mCursor.getInt(TASK_COLUMN_PRIORITY); 
				    String recipient = mCursor.getString(TASK_COLUMN_RECIPIENT);
				    String time = mCursor.getString(TASK_COLUMN_TIME);
				    String complete = mCursor.getString(TASK_COLUMN_COMPLETE);
				    int serverId = mCursor.getInt(TASK_COLUMN_SERVERID);
				    String owner = mCursor.getString(TASK_COLUMN_OWNER);

				    taskList.add(new Task(id, priority, author, time, recipient, content, complete, serverId, owner));
			    
			  } while (mCursor.moveToNext());
		  }
		  mCursor.close();
		  		  
		  Collections.reverse(taskList);
		  return taskList;
	}
	public int sizeNewTasks(String login) {
		int size = 0; 
		Cursor mCursor = db.query(DatabaseHelper.taskTable, null,DatabaseHelper.taskComplete + " = ?" + " AND " + DatabaseHelper.taskRecipient + " = ?",  new String[] {"false", login}, null, null, null);
		mCursor.moveToFirst();
		
		if (!mCursor.isAfterLast()) { 
			do {
				size++;
			} while (mCursor.moveToNext());
		}
		
		mCursor.close();
		return size;
	}
}