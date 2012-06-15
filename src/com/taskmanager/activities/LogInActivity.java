package com.taskmanager.activities;



import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.taskmanager.R;
import com.taskmanager.asynctasks.LoginConnection;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends Activity implements OnClickListener {
    
	/** Called when the activity is first created. */
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        	setContentView(R.layout.login);
        
        Button loginB = (Button) findViewById(R.id.loginB);
       	loginB.setOnClickListener(this);		
       	Button registerB = (Button) findViewById(R.id.registerB);
       	registerB.setOnClickListener(this);
       	
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
		//start register activity
		startActivity(new Intent(LogInActivity.this, Register.class));
	}

	private void doLogin() {
		
		// TODO Auto-generated method stub
		EditText loginF = (EditText) findViewById(R.id.loginET);
		EditText passwordF = (EditText) findViewById(R.id.passwordET);
		ProgressDialog pg = new ProgressDialog(LogInActivity.this);
		
		String login = loginF.getText().toString();
		String password = passwordF.getText().toString();
		
		try {
			HashMap<String, String>results = new LoginConnection(pg).execute(login,password).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}