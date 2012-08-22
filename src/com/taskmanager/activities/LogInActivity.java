package com.taskmanager.activities;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.taskmanager.R;
import com.taskmanager.asynctasks.LoginConnection;
import com.taskmanager.database.dao.TaskDataSource;
import com.taskmanager.database.dao.UserDataSource;
import com.taskmanager.database.entities.User;

public class LogInActivity extends Activity implements OnClickListener {
    
	private SharedPreferences sPreferences;
	/** Called when the activity is first created. */
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		sPreferences = getSharedPreferences("CurrentUser", 0);
		super.onCreate(savedInstanceState);
	
        if(checkToken()){
			startActivity(new Intent(LogInActivity.this, MainMenuActivity.class));
			finish();
		}	
	    
        setContentView(R.layout.login);
        
        Button loginB = (Button) findViewById(R.id.loginB);
       	loginB.setOnClickListener(this);		
       	Button registerB = (Button) findViewById(R.id.registerB);
       	registerB.setOnClickListener(this);
       	
	}
	
	private boolean checkToken() {
		Log.i("checktoken", "checktoken");
		return sPreferences.contains("auth_token") && 
				sPreferences.getString("auth_token", null)!=null;
	}
	
	public void onClick(View v) {
		
		switch (v.getId()) {
	      case R.id.loginB:
	        doLogin();
	        break;
	      case R.id.registerB:
	        redirectToRegistration();
	        break;
	     }
		
	}

	private void redirectToRegistration() {
		//start registration activity
		startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
	}

	private void doLogin() {
		EditText loginF = (EditText) findViewById(R.id.loginET);
		EditText passwordF = (EditText) findViewById(R.id.passwordET);
		ProgressDialog pg = new ProgressDialog(LogInActivity.this);
		
		String login = loginF.getText().toString();
		String password = passwordF.getText().toString();
		
		try {			
			
			HashMap<String, Object> results = new LoginConnection(pg).execute(login,password).get();
			Log.i("sessiontokens", results.toString());
			if(results.get("error").equals("Success")){
				
				Log.i("loginisation", "Successfull loginisation!");
				SharedPreferences.Editor editor = sPreferences.edit();
				editor.putString("auth_token", results.get("auth_token").toString());
				editor.putString("login", login);
				editor.commit();				
				
				ArrayList<User> friends = (ArrayList<User>) results.get("friends");
				
				
				//open database
				UserDataSource userdatabase = new UserDataSource(this);
				TaskDataSource taskdatabase = new TaskDataSource(this);
	
				userdatabase.open();
				taskdatabase.open();
				//delete existing tables
				userdatabase.deleteAll();
				//taskdatabase.deleteAll();
				
				if(friends != null){
					for (User user : friends) {
						userdatabase.insert(user);
					}
				}
				
				userdatabase.close();
				taskdatabase.close();
				
				startActivity(new Intent(LogInActivity.this, MainMenuActivity.class));
				
			}else{
				
				//handle errors
				new AlertDialog.Builder(LogInActivity.this).setTitle("Error").setMessage(results.get("error").toString()).
					setNeutralButton("Ok", null).show();
				Log.e("loginisaton", results.get("error").toString());
			} 
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}