package com.taskmanager.activities;

import com.taskmanager.R;
import com.taskmanager.database.dao.TaskDataSource;
import com.taskmanager.database.dao.UserDataSource;
import com.taskmanager.database.entities.Task;
import com.taskmanager.database.entities.User;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class MainMenuActivity extends TabActivity { 

	public SQLiteDatabase db;
	public static final int IDM_CLOSE = 101;
	public static final int IDM_EXIT = 102;
	
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
        
        intent = new Intent().setClass(this, TasksActivity.class);
        spec = tabHost.newTabSpec("tasks").setIndicator("Tasks",
                          res.getDrawable(R.drawable.ic_tab_message))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ContactsActivity.class);
        spec = tabHost.newTabSpec("contacts").setIndicator("Contacts",
                          res.getDrawable(R.drawable.ic_tab_contacts))
                      .setContent(intent);
        tabHost.addTab(spec);
     
        intent = new Intent().setClass(MainMenuActivity.this, NewFriendActivity.class);
        spec = tabHost.newTabSpec("newfriend").setIndicator("New friend",
                          res.getDrawable(R.drawable.ic_tab_newfriend))
                      .setContent(intent);
        tabHost.addTab(spec);
          
        tabHost.setCurrentTab(0);
        
     // startService(new Intent(this,UpdaterService.class));
        

	}
	
	public boolean onCreateOptionsMenu(Menu menu) {	
		
	      menu.add(Menu.NONE, IDM_EXIT, Menu.NONE,"Exit").setIcon(R.drawable.ic_menu_exit);
	      menu.add(Menu.NONE, IDM_CLOSE, Menu.NONE, "Close").setIcon(R.drawable.ic_menu_close);
	      
	      return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		switch (item.getItemId()) {
		case IDM_CLOSE:
			SharedPreferences.Editor editor= getSharedPreferences("CurrentUser", 0).edit();
			editor.clear();
			editor.commit();
			finish();
			break;
		case IDM_EXIT:
			
			break;
		}
	    return super.onOptionsItemSelected(item);
	}
        
}