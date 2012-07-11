package com.taskmanager.activities;

import java.util.concurrent.ExecutionException;

import com.taskmanager.R;
import com.taskmanager.asynctasks.LogOut;
import com.taskmanager.database.dao.TaskDataSource;
import com.taskmanager.database.dao.UserDataSource;
import com.taskmanager.database.entities.Task;
import com.taskmanager.database.entities.User;
import com.taskmanager.service.UpdaterService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
        spec = tabHost.newTabSpec("newfriend").setIndicator("Add new contact",
                          res.getDrawable(R.drawable.ic_tab_newfriend))
                      .setContent(intent);
        tabHost.addTab(spec);
          
        tabHost.setCurrentTab(0);
        
        startService(new Intent(this,UpdaterService.class));
        

	}
	
	public boolean onCreateOptionsMenu(Menu menu) {	
		
	      menu.add(Menu.NONE, IDM_EXIT, Menu.NONE,"Exit").setIcon(R.drawable.ic_menu_exit);
	      menu.add(Menu.NONE, IDM_CLOSE, Menu.NONE, "Close").setIcon(R.drawable.ic_menu_close);
	      
	      return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		switch (item.getItemId()) {
		case IDM_CLOSE:

			finish();
			break;
		case IDM_EXIT:
			{
				SharedPreferences s = getSharedPreferences("CurrentUser", 0);
				String auth_token = s.getString("auth_token", null);
				ProgressDialog pleaseWait = new ProgressDialog(MainMenuActivity.this);
				
				String error=null;
				
				try {
					error = new LogOut(pleaseWait).execute(auth_token).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String result;
				if(error.equals("Success")){
					SharedPreferences.Editor editor = s.edit();
					editor.clear();
					editor.commit();
					stopService(new Intent(this,UpdaterService.class));
					System.runFinalizersOnExit(true);
					System.exit(0);
					result = "Successfull logout";
				}else{
					result = error;
				}
				new AlertDialog.Builder(this).setTitle("Result").setMessage(result).
					setNeutralButton("Ok", null).show();
				finish(); 
				break;
			}	
		}
		
	    return super.onOptionsItemSelected(item);
	    
	}
        
}