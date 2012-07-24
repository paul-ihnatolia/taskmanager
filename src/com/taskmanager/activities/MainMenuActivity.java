package com.taskmanager.activities;

import java.util.concurrent.ExecutionException;

import com.taskmanager.R;
import com.taskmanager.asynctasks.LogOut;

import com.taskmanager.service.UpdaterService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class MainMenuActivity extends TabActivity implements OnClickListener{ 
	public static final int DIALOG_EXIT = 102;
	private Context context = this;
	static TabHost tabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		ImageView closeImageView = (ImageView) findViewById(R.id.closeImage);
		ImageView exitImageView = (ImageView) findViewById(R.id.exirImage);
		closeImageView.setClickable(true);
		exitImageView.setClickable(true);
		closeImageView.setOnClickListener(this);
		exitImageView.setOnClickListener(this);
		
        Resources res = getResources(); 
        tabHost = getTabHost();  
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
        
        setTabColor(tabHost);
        
    

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			public void onTabChanged(String arg0) {
				setTabColor(tabHost);
				
			}
                
        });  
	}
	
	public static void setTabColor(TabHost tabHost) {
	    for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
	    	tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_not_pressed_bg);
	    }
	    tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_pressed_bg);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.closeImage:
			finish();
			break;
		case R.id.exirImage:
			showDialog(DIALOG_EXIT);
			break;
		}
		
	}
	protected Dialog onCreateDialog(int id) {
		//Create dialogue for the task
		if (DIALOG_EXIT == id) {
			AlertDialog.Builder adb2 = new AlertDialog.Builder(this);
	        
	        adb2.setTitle("Exit");
	        adb2.setMessage("You want to go?");
	      
	        adb2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
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
						stopService(new Intent(context,UpdaterService.class));
						System.runFinalizersOnExit(true);
						System.exit(0);
						result = "Successfull logout";
					}else{
						result = error;
					}
					new AlertDialog.Builder(context).setTitle("Result").setMessage(result).
						setNeutralButton("Ok", null).show();
					finish(); 
					
				}
			});
	        
	        adb2.setNegativeButton("No", new DialogInterface.OnClickListener() {
	   			public void onClick(DialogInterface dialog, int which) {
	   				dialog.cancel();	
	   			}
	   	    });
	        return adb2.create();
		}
		
		return super.onCreateDialog(id);
	}
        
}