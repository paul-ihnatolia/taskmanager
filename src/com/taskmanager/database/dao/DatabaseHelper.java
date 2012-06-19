package com.taskmanager.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public DatabaseHelper(Context context) {
		super(context, dbName, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	static final int DB_VERSION = 1;
	static final String dbName = "taskManagerDB";
	static final String userTable = "users";
	static final String userID = "userID";
	static final String userFirstname = "firstname";
	static final String userLastname = "lastname";
	static final String userLogin = "login";
//	static final String userPassword = "password";
//	static final String userTasks = "tasks";
	
	static final String taskTable = "tasks";
	static final String taskID = "taskID";
	static final String taskComplete = "complete";
	static final String taskPriority = "priority";
	static final String taskAuthor = "author";
	static final String taskRecipient = "recipient";
	static final String taskTime = "time";
	static final String taskContent = "content";
		 
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE "+taskTable+" ("+
				taskID + " INTEGER PRIMARY KEY, "+ taskComplete + "BOOLEAN, " 
				+ taskPriority+ "INTEGER, "+ taskTime+ "TIME, "+
				taskAuthor+ "TEXT, " + taskRecipient + "TEXT, " 
				+ taskContent + "TEXT)");
		
		db.execSQL("CREATE TABLE "+userTable+" ("+
				userID+ " INTEGER PRIMARY KEY, "+
				userFirstname + "TEXT, " 
				+ userLastname + "TEXT, "
				+ userLogin + "TEXT)");
    /*    userTasks +
        " INTEGER NOT NULL ,FOREIGN KEY ("+userTasks+
        ")REFERENCES "+taskTable+" ("+taskID+"));");
        */
	/*	ContentValues cv = new ContentValues();
		String [] users = new String[] {"Oleg", "Taras", "Vasya"};
		for (int i = 0; i < users.length; i++) {
			 cv.put(userFirstname, users[i]);
			 cv.put(userID, i+1);
			 db.insert(userTable, null, cv);
		}
		String [] tasksOleg = new String[] {"Do it!", "Must", "Perhaps"};
		String [] tasksTaras = new String[] {"Do it!", "Must", "Perhaps"};
		String [] tasksVasya = new String[] {"Do it!", "Must", "Perhaps"};
		for (int i = 0; i < tasksOleg.length; i++) {
			cv.put(taskID, i+1);
			cv.put(taskContent, tasksOleg[i]);
			db.insert(taskTable, null, cv);
		}
		for (int i = 0; i < tasksTaras.length; i++) {
			cv.put(taskID, i+1);
			cv.put(taskContent, tasksTaras[i]);
			db.insert(taskTable, null, cv);
		}
		for (int i = 0; i < tasksVasya.length; i++) {
			cv.put(taskID, i+1);
			cv.put(taskContent, tasksVasya[i]);
			db.insert(taskTable, null, cv);
		}*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
/*		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+taskTable);
		db.execSQL("DROP TABLE IF EXISTS "+userTable);
		onCreate(db);*/
	}

}