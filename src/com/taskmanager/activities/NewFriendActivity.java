package com.taskmanager.activities;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.concurrent.ExecutionException;

import com.taskmanager.R;
import com.taskmanager.adapter.ContactsArrayAdapter;
import com.taskmanager.asynctasks.Search;
import com.taskmanager.asynctasks.SendRequestForFriendship;
import com.taskmanager.database.entities.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

public class NewFriendActivity extends Activity implements OnClickListener{

	private ListView frendList;
	private ArrayList<User> users;
	final int DIALOG_NEW_FRIEND = 1;
	private int positionUser;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newfriend);
		
		ImageButton searchImageButton = (ImageButton) findViewById(R.id.searchbutton);
		searchImageButton.setOnClickListener(this);
		
		frendList = (ListView) findViewById(R.id.serchList);		
		frendList.setClickable(true);
		frendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				showDialog(DIALOG_NEW_FRIEND);
				positionUser = position;
			}
		});	
	}

	public void onClick(View v) {
		
		String searchItem = ((EditText) findViewById(R.id.serchfield)).getText().toString();
		
		if(searchItem.length()<2) {
			((EditText)findViewById(R.id.serchfield)).setError("Request must contain at least 2 characters");
		}else {
			
			String authToken = getSharedPreferences("CurrentUser", 0).getString("auth_token", null);
			ProgressDialog pleaseWait = new ProgressDialog(NewFriendActivity.this);
			
			try {
			
				HashMap<String, Object> results = new Search(pleaseWait).execute(authToken,searchItem).get();
				users = new ArrayList<User>();
				
				if(!results.get("error").equals("Success")){
					new AlertDialog.Builder(this).setTitle("Sorry").setMessage(results.get("error").toString()).
						setNeutralButton("Ok", null).show();
				}else{				
					users.addAll((ArrayList<User>) results.get("users"));				
				}
				
				ContactsArrayAdapter adapter = new ContactsArrayAdapter(this, users);
				frendList.setAdapter(adapter);
	
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}catch (Exception e) {
				Log.e("new friend","some exception");
			}
		}
	}
	
	protected Dialog onCreateDialog(int id) {
	      if (id == DIALOG_NEW_FRIEND) {
	        AlertDialog.Builder adb = new AlertDialog.Builder(this);
	        
	        adb.setTitle("Add friend");
	        adb.setMessage("Send an inquiry?");
	      
	        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					String authToken = getSharedPreferences("CurrentUser", 0).getString("auth_token", null);
					ProgressDialog pg = new ProgressDialog(NewFriendActivity.this);
			
					try {
						String error = new SendRequestForFriendship(pg).execute("request", authToken, 
								users.get(positionUser).getLogin()).get().get("error").toString();
						String result;
						if(error.equals("Success")){
							result = "Request was successfully sended!";
						}else{
							result = error;
						}
						new AlertDialog.Builder(NewFriendActivity.this).setMessage(result).
							setNeutralButton("Ok", null).show();
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			});
	        
	        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.cancel();
					
				}
			});

	        return adb.create();
	      }
	      return super.onCreateDialog(id);
	    }
}