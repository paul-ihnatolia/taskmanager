package com.taskmanager.activities;

import com.taskmanager.R;
import com.taskmanager.database.dao.TaskDataSource;
import com.taskmanager.database.dao.UserDataSource;
import com.taskmanager.database.entities.Task;
import com.taskmanager.database.entities.User;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TabHost;

public class MainMenuActivity extends TabActivity { 

	public SQLiteDatabase db;
	UserDataSource userdatabase = new UserDataSource(this);
	TaskDataSource taskdatabase = new TaskDataSource(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
        Resources res = getResources(); 
        TabHost tabHost = getTabHost();  
        TabHost.TabSpec spec;  
        Intent intent;
        
        //(Test)Create database
        //database();
        
        intent = new Intent().setClass(MainMenuActivity.this, NewFriendActivity.class);
        spec = tabHost.newTabSpec("newfriend").setIndicator("������ �������",
                          res.getDrawable(R.drawable.ic_tab_newfriend))
                      .setContent(intent);
        tabHost.addTab(spec);
        
     /*   intent = new Intent().setClass(this, ContactsActivity.class);
        spec = tabHost.newTabSpec("contacts").setIndicator("��������",
                          res.getDrawable(R.drawable.ic_tab_contacts))
                      .setContent(intent);
        tabHost.addTab(spec);
     
        intent = new Intent().setClass(this, TasksActivity.class);
        spec = tabHost.newTabSpec("tasks").setIndicator("��������",
                          res.getDrawable(R.drawable.ic_tab_message))
                      .setContent(intent);
        tabHost.addTab(spec); */
          
        tabHost.setCurrentTab(0);
	}
	
	
	private void database() {
		userdatabase.open();
		taskdatabase.open();
		userdatabase.deleteAll();
		taskdatabase.deleteAll();
		
		User user1 = new User(1, "�����", "�����", "kot");
		User user2 = new User(2, "����", "��������", "java");
		User user3 = new User(3, "����", "�����", "dos");
		
		userdatabase.addItem(user1);
		userdatabase.addItem(user2);
		userdatabase.addItem(user3);
		
		Task task = new Task(1, 1, "kot", "21.01.2012", "�����", "� ���� ������� ������ � ����", "true");
		Task task2 = new Task(2, 2, "kot", "23.03.2012", "�����", "�����", "false");
		Task task3 = new Task(3, 3, "dos", "13.13.2013", "����", "������ ����!!!", "false");
		Task task4 = new Task(4, 3, "dos", "11.11.2012", "����", "� ���� ������� ������ � ����", "true");
		Task task5 = new Task(5, 2, "kot", "09.01.2012", "�����", "�����", "true");
		Task task6 = new Task(6, 3, "java", "21.01.2012", "����", "������ ����!!!", "true");
		
		taskdatabase.addItem(task);
		taskdatabase.addItem(task2);
		taskdatabase.addItem(task3);
		taskdatabase.addItem(task4);
		taskdatabase.addItem(task5);
		taskdatabase.addItem(task6);
	}
}
