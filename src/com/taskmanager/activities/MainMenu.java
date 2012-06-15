package com.taskmanager.activities;

import com.taskmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class MainMenu extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("mainmenu", "MainMenu");
		if(!checkToken()){
			startActivity(new Intent(MainMenu.this, LogInActivity.class));
		}	
		setContentView(R.layout.main);
	}
	
	private boolean checkToken() {
		// TODO Auto-generated method stub
		SharedPreferences mtoken = getSharedPreferences("CurrentUser", 0);
		return mtoken.contains("token");
	}

}
