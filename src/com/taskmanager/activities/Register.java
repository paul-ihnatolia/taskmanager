package com.taskmanager.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.taskmanager.R;
import com.taskmanager.helpers.CheckFields;

public class Register extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);
		
		Button registerB = (Button) findViewById(R.id.doRegisterB);
		
		registerB.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				String firstName = ((TextView) findViewById(R.id.firstName)).getText().toString();
				String lastName = ((TextView) findViewById(R.id.lastName)).getText().toString();
				String login = ((TextView) findViewById(R.id.login)).getText().toString();
				String password = ((TextView) findViewById(R.id.password)).getText().toString();
				
				ArrayList<String> errors = CheckFields.checkBeforeRegistraton(firstName,lastName,login,password);
			}
		});
	}
	
}
