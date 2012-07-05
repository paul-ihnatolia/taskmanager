package com.taskmanager.activities;

import com.taskmanager.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewFriendActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newfriend);
		
		Button search = (Button) findViewById(R.id.searchbutton);
	
		search.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				String searchItem = ((EditText) findViewById(R.id.serchfield)).getText().toString();
				ProgressDialog pleaseWait = new ProgressDialog(NewFriendActivity.this);
			}
		});
	}
}
