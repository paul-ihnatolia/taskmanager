package com.taskmanager.activities;



import com.taskmanager.R;
import com.taskmanager.asynctasks.LoginConnection;

import android.app.Activity;
import android.app.ProgressDialog;
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
        setContentView(R.layout.main);
        
        Button loginB = (Button) findViewById(R.id.button1);
       	loginB.setOnClickListener(this);		
       	Button registerB = (Button) findViewById(R.id.button2);
       	registerB.setOnClickListener(this);
       	
	}

	public void onClick(View v) {
		
		switch (v.getId()) {
	      case R.id.button1:
	        doLogin();
	        break;
	      case R.id.button2:
	        doRegister();
	        break;
	     }
		
		
		
			EditText loginF = (EditText) findViewById(R.id.editText1);
			EditText passwordF = (EditText) findViewById(R.id.editText2);
			TextView tv = (TextView) findViewById(R.id.textView3);
			ProgressDialog pg = new ProgressDialog(LogInActivity.this);
			
			String login = loginF.getText().toString();
			String password = passwordF.getText().toString();
			
			new LoginConnection(pg, tv).execute(login,password);
	}

	private void doRegister() {
		// TODO Auto-generated method stub
		
	}

	private void doLogin() {
		// TODO Auto-generated method stub
		
	}
}