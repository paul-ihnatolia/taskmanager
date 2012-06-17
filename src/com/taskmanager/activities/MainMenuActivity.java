package com.taskmanager.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.taskmanager.R;

public class MainMenuActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.i("mainmenu", "MainMenu");
	}

}
