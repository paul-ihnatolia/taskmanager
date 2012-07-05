package com.taskmanager.database.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.StaticLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.taskmanager.database.entities.Task;
import com.taskmanager.database.entities.User;


public class UserDataSource extends BaseAdapter{
	
	final String LOG_TAG = "myLogs";

	private static final int USER_COLUMN_ID = 0;
	private static final int USER_COLUMN_FIRSTNAME= 1;
	private static final int USER_COLUMN_LASTNAME = 2;
	private static final int USER_COLUMN_LOGIN = 3;
	private String[] allUserColumns = { DatabaseHelper.userID, 
			DatabaseHelper.userFirstname, 
			DatabaseHelper.userLastname, 
			DatabaseHelper.userLogin};

	private Cursor cursor;
	private static SQLiteDatabase db;
	private static DatabaseHelper dbHelper;

	public UserDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public static void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public static void close() {
		dbHelper.close();
	}
	
	public void addItem(User user) {
		String sql = "insert into users values('" + user.getId() + "','" +user.getFirstname()+ "','" + user.getLastname() +  "','" + user.getLogin() + "');";
		db.execSQL(sql);
	}
	
	public static long insert(User user) {
		ContentValues cv = new ContentValues();
	//	cv.put("_id", user.getId());
		cv.put("firstname", user.getFirstname());
		cv.put("lastname", user.getLastname());
		cv.put(DatabaseHelper.userLogin, user.getLogin());
		return db.insert("users", null, cv);
	}
	public static int  update(User user) {
		ContentValues cv=new ContentValues();
		
		cv.put(DatabaseHelper.userID, user.getId());
		cv.put(DatabaseHelper.userFirstname, user.getFirstname());
		cv.put(DatabaseHelper.userLastname, user.getLastname());
		cv.put(DatabaseHelper.userLogin, user.getLogin());
		return db.update(DatabaseHelper.userTable, cv, DatabaseHelper.userID + " = ?", new String[] {String.valueOf(user.getId()) });
	}
	
	public static int deleteAll() {
		return db.delete(DatabaseHelper.userTable, null, null);
	}
	
	public static void delete(long id) {
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
	
	
//	public Cursor getTask(String login){
//		return db.query(DatabaseHelper.userTable, null, login, null, null, null, null);
//	}
	
	public Cursor getUserData() {
	    return db.query(DatabaseHelper.userTable, null, null, null, null, null, null);
	  }

	public static Cursor getTaskData(String login) {
	    return db.query(DatabaseHelper.taskTable, null, DatabaseHelper.taskAuthor+ " = "
	        + login, null, null, null, null);
	}
	
	public ArrayList<User> getAllUser() {
		ArrayList<User> users = new ArrayList<User>();

		Cursor cursor = db.query(DatabaseHelper.userTable, allUserColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			User user = cursorToUser(cursor);
			users.add(user);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}
	private User cursorToUser(Cursor cursor) {
		User user = new User();
		user.setId(cursor.getInt(cursor.getInt(0)));
		user.setFirstname(cursor.getString(1));
		user.setLastname(cursor.getString(2));
		user.setLogin(cursor.getString(3));
		return user;
	}
	
	public ArrayList<User> getAllUsers(){
		ArrayList<User> users = new ArrayList<User>();
		Cursor c = db.query ("users", null, null, null, null, null, null);
		
		// ���������� ������ �������� �� ����� � �������
		int idColIndex = c.getColumnIndex ("_id");
		int firstnameColIndex = c.getColumnIndex ("firstname");
		int lastnameColIndex = c.getColumnIndex ("lastname");
		int loginColIndex = c.getColumnIndex ("login");

		if (c.moveToFirst ()) {

			do {
				// �������� �������� �� ������� �������� � ����� ��� � ���
					
				int id =  c.getInt (idColIndex);
				String firstname = c.getString (firstnameColIndex);
				String lastname = c.getString (lastnameColIndex); 
				String login = c.getString(loginColIndex);
				// ������� �� ��������� ������
				// � ���� ��������� ��� (������� � ���������), �� false � ������� �� �����
				users.add(new User(id, firstname, lastname, login));
			} while (c.moveToNext ());
		} 

		return users;
	}
	
	public Cursor getAllEntries() {
		//������ ������� ����, ������� ������� �������� � ���������
		// ���������� ������ � ����
		return db.query(DatabaseHelper.userTable, allUserColumns,
				null, null, null, null, DatabaseHelper.userID);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}