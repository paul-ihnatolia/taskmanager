package com.taskmanager.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	public DatabaseHelper(Context context) {
		super(context, dbName, null, DB_VERSION);
	}
	
	final String LOG_TAG = "myLogs";
	static final int DB_VERSION = 21;
	static final String dbName = "taskManagerDB";
	static final String userTable = "users";
	static final String userID = "_id";
	static final String userFirstname = "firstname";
	static final String userLastname = "lastname";
	static final String userLogin = "login";
	private static final String DATABASE_CREATE = "create table "
			+ userTable + "(" + userID
			+ " integer primary key autoincrement, " + userFirstname + " TEXT," + userLastname + " TEXT," 
			+ userLogin + " TEXT);";

	
	static final String taskTable = "tasks";
	static final String taskID = "_id";
	static final String taskPriority = "priority";
	static final String taskAuthor = "author";
	static final String taskTime = "time";
	static final String taskRecipient = "recipient";
	static final String taskContent = "content";
	static final String taskComplete = "complete";
	static final String taskServerId = "serverId";

	private static final String DATABASE_CREATE2 = "create table "
			+ taskTable + "(" + taskID
			+ " integer primary key autoincrement, " + taskPriority + " INTEGER," + taskAuthor + " TEXT,"   
			+ taskTime + " TEXT," + taskRecipient + " TEXT,"  + taskContent + " TEXT," + taskComplete + " TEXT," + taskServerId + " INTEGER);";
		 
	@Override
	public void onCreate(SQLiteDatabase db) {
				
		db.execSQL (DATABASE_CREATE);
		db.execSQL (DATABASE_CREATE2);
		Log.d (LOG_TAG, "--- onCreate database ---");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + "users");
		db.execSQL("DROP TABLE IF EXISTS " + "tasks");
		onCreate(db);

	}

}