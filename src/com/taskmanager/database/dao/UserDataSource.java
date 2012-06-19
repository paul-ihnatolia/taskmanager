package com.taskmanager.database.dao;

import java.util.ArrayList;

import com.taskmanager.database.entities.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class UserDataSource {
	private static final int USER_COLUMN_ID = 0;
	private static final int USER_COLUMN_FIRSTNAME= 1;
	private static final int USER_COLUMN_LASTNAME = 2;
	private static final int USER_COLUMN_LOGIN = 3;

	private static SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	//private ContentValues cv = new ContentValues();
	//private String[] allTaskColumns = { DatabaseHelper.taskAuthor, DatabaseHelper.taskID, DatabaseHelper.taskRecipient, DatabaseHelper.taskComplete, DatabaseHelper.taskContent, DatabaseHelper.taskPriority, DatabaseHelper.taskTime };
//	private String[] allUserColumns = { DatabaseHelper.userFirstname, DatabaseHelper.userID, DatabaseHelper.userLastname, DatabaseHelper.userLogin};

	public UserDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public long insert(User user) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.userFirstname, user.getFirstname());
		cv.put(DatabaseHelper.userLastname, user.getLastname());
		cv.put(DatabaseHelper.userLogin, user.getLogin());
		return database.insert(DatabaseHelper.userTable, null, cv);
	}
	public int update(User user) {
		ContentValues cv=new ContentValues();
		cv.put(DatabaseHelper.userFirstname, user.getFirstname());
		cv.put(DatabaseHelper.userLastname, user.getLastname());
		cv.put(DatabaseHelper.userLogin, user.getLogin());
		return database.update(DatabaseHelper.userTable, cv, DatabaseHelper.userID + " = ?", new String[] {String.valueOf(user.getId()) });
	}
	
	public int deleteAll() {
		return database.delete(DatabaseHelper.userTable, null, null);
	}
	
	public void delete(long id) {
		database.delete(DatabaseHelper.userTable, DatabaseHelper.userID + " = ?", new String[] { String.valueOf(id) });
	}
	
	public User select(long id) {
		Cursor mCursor = database.query(DatabaseHelper.userTable, null, DatabaseHelper.userID + " = ?", new String[] {String.valueOf(id)}, null, null, DatabaseHelper.userFirstname);
		 
		mCursor.moveToFirst();
		String firstname = mCursor.getString(USER_COLUMN_FIRSTNAME); 
		String lastname = mCursor.getString(USER_COLUMN_LASTNAME);
		String login = mCursor.getString(USER_COLUMN_LOGIN);
		mCursor.close();
		return new User(id, firstname, lastname, login);
	}
	public ArrayList<User> selectAll() {
		Cursor mCursor = database.query(DatabaseHelper.userTable, null, null, null, null, null, DatabaseHelper.userTable);
		 
		ArrayList<User> arr = new ArrayList<User>();
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {	
			do {
				
				long id = mCursor.getLong(USER_COLUMN_ID);
				String firstname = mCursor.getString(USER_COLUMN_FIRSTNAME); 
				String lastname = mCursor.getString(USER_COLUMN_LASTNAME);
				String login = mCursor.getString(USER_COLUMN_LOGIN);
				arr.add(new User(id, firstname, lastname, login));
				
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return arr;
	}	  
	
	public User selectByLogin(String login) {
		Cursor mCursor = database.query(DatabaseHelper.userTable, null, DatabaseHelper.userLogin + " = ?", new String[] {String.valueOf(login)}, null, null, DatabaseHelper.userFirstname);
		 
		mCursor.moveToFirst();
		long id = mCursor.getLong(USER_COLUMN_ID);
		String firstname = mCursor.getString(USER_COLUMN_FIRSTNAME); 
		String lastname = mCursor.getString(USER_COLUMN_LASTNAME);
		mCursor.close();
		return new User(id, firstname, lastname, login);
	}
	
	

	public Cursor getTask(String login){
		return database.query(DatabaseHelper.userTable, null, login, null, null, null, null);
	}
	public Cursor getUserData() {
	    return database.query(DatabaseHelper.userTable, null, null, null, null, null, null);
	  }

	public static Cursor getTaskData(long userID) {
	    return database.query(DatabaseHelper.taskTable, null, DatabaseHelper.taskAuthor+ " = "
	        + userID, null, null, null, null);
	  }


/*	public Task createTask (String task) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.taskAuthor, task.toString());
		values.put(DatabaseHelper.taskContent, task.toString());
		values.put(DatabaseHelper.taskID, task.toString());
		long insertId = database.insert(DatabaseHelper.taskTable, null,	values);
		Cursor cursor = database.query(DatabaseHelper.taskTable,
				allTaskColumns, DatabaseHelper.taskID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Task newTask = cursorToTask(cursor);
		cursor.close();
		return newTask;
	}

	public void deleteTask (Task Task) {
		long id = Task.getId();
		System.out.println("Task deleted with id: " + id);
		database.delete(DatabaseHelper.taskTable, DatabaseHelper.taskID
				+ " = " + id, null);
	}

/*	public List<Task> getAllTask() {
		List<Task> tasks = new ArrayList<Task>();

		Cursor cursor = database.query(DatabaseHelper.taskTable,
				allTaskColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Task task = cursorToTask(cursor);
			tasks.add(task);
			cursor.moveToNext();
		}
		cursor.close();
		return tasks;
	}

/*	private Task cursorToTask(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getLong(0));
		task.setAuthor(cursor.getString(1));
		return task;
	}*/


}