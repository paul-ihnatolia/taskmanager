package com.taskmanager.database.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.taskmanager.database.entities.User;


public class UserDataSource {
	
	final String LOG_TAG = "myLogs";

	private static final int USER_COLUMN_ID = 0;
	private static final int USER_COLUMN_FIRSTNAME= 1;
	private static final int USER_COLUMN_LASTNAME = 2;
	private static final int USER_COLUMN_LOGIN = 3;
	private String[] allUserColumns = { DatabaseHelper.userID, 
			DatabaseHelper.userFirstname, 
			DatabaseHelper.userLastname, 
			DatabaseHelper.userLogin};

	private static SQLiteDatabase db;
	private static DatabaseHelper dbHelper;

	public UserDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public void addItem(User user) {
		String sql = "insert into users values('" + user.getId() + "','" +user.getFirstname()+ "','" + user.getLastname() +  "','" + user.getLogin() + "');";
		db.execSQL(sql);
	}
	
	public long insert(User user) {
		ContentValues cv = new ContentValues();
		cv.put("firstname", user.getFirstname());
		cv.put("lastname", user.getLastname());
		cv.put(DatabaseHelper.userLogin, user.getLogin());
		return db.insert("users", null, cv);
	}
	public int  update(User user) {
		ContentValues cv=new ContentValues();
		
		cv.put(DatabaseHelper.userFirstname, user.getFirstname());
		cv.put(DatabaseHelper.userLastname, user.getLastname());
		cv.put(DatabaseHelper.userLogin, user.getLogin());
		return db.update(DatabaseHelper.userTable, cv, DatabaseHelper.userID + " = ?", new String[] {String.valueOf(user.getId()) });
	}
	
	public int deleteAll() {
		return db.delete(DatabaseHelper.userTable, null, null);
	}
	
	public void delete(long id) {
		db.delete(DatabaseHelper.userTable, DatabaseHelper.userID + " = ?", new String[] { String.valueOf(id) });
	}
	
	public User select(int id) {
		Cursor mCursor = db.query(DatabaseHelper.userTable, null, DatabaseHelper.userID + " = ?", new String[] {String.valueOf(id)}, null, null, DatabaseHelper.userFirstname);
		 
		mCursor.moveToFirst();
		String firstname = mCursor.getString(USER_COLUMN_FIRSTNAME); 
		String lastname = mCursor.getString(USER_COLUMN_LASTNAME);
		String login = mCursor.getString(USER_COLUMN_LOGIN);
		mCursor.close();
		return new User(id, firstname, lastname, login);
	}
	public ArrayList<User> selectAll() {
		Cursor mCursor = db.query(DatabaseHelper.userTable, allUserColumns, null, null, null, null, null);
		int idColIndex = mCursor.getColumnIndex ("_id");
		int firstnameColIndex = mCursor.getColumnIndex ("firstname");
		int lastnameColIndex = mCursor.getColumnIndex ("lastname");
		int loginColIndex = mCursor.getColumnIndex ("login");
		 
		ArrayList<User> arr = new ArrayList<User>();
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {	
			do {
				
				int id = mCursor.getInt(idColIndex);
				String firstname = mCursor.getString(firstnameColIndex); 
				String lastname = mCursor.getString(lastnameColIndex);
				String login = mCursor.getString(loginColIndex);
				arr.add(new User(id, firstname, lastname, login));
				
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return arr;
	}	  
	
	public User selectByLogin(String login) {
		Cursor mCursor = db.query(DatabaseHelper.userTable, null, DatabaseHelper.userLogin + " = ?", new String[] {String.valueOf(login)}, null, null, DatabaseHelper.userFirstname);
		 
		mCursor.moveToFirst();
		int id = mCursor.getInt(USER_COLUMN_ID);
		String firstname = mCursor.getString(USER_COLUMN_FIRSTNAME); 
		String lastname = mCursor.getString(USER_COLUMN_LASTNAME);
		mCursor.close();
		return new User(id, firstname, lastname, login);
	}
			
	public Cursor getUserData() {
	    return db.query(DatabaseHelper.userTable, null, null, null, null, null, null);
	}

	public  Cursor getTaskData(String login) {
	    return db.query(DatabaseHelper.taskTable, null, DatabaseHelper.taskAuthor+ " = "
	        + login, null, null, null, null);
	}
		
	public Cursor getAllEntries() {
		return db.query(DatabaseHelper.userTable, allUserColumns,
				null, null, null, null, DatabaseHelper.userID);
	}

}