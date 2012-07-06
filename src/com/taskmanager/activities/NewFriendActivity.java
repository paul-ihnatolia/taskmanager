package com.taskmanager.activities;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.taskmanager.R;
import com.taskmanager.asynctasks.Search;
import com.taskmanager.database.entities.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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
				String authToken = getSharedPreferences("CurrentUser", 0).getString("auth_token", null);
				ProgressDialog pleaseWait = new ProgressDialog(NewFriendActivity.this);
				
				try {
					
					ArrayList<User> results  = new Search(pleaseWait).execute(authToken,searchItem).get();
					if(results == null){
						//nema rezultativ
					}else{
						//spisok rezultativ
						for (int i = 0; i < results.size(); i++) {
							Log.i("user", results.get(i).toString());
						}
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
