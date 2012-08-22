package com.taskmanager.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.taskmanager.adapter.ContactsArrayAdapter;
import com.taskmanager.database.dao.UserDataSource;
import com.taskmanager.database.entities.User;

import android.R;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends ListActivity {
	UserDataSource userdatabase = new UserDataSource(this);
	private BroadcastReceiver receiver;
	private List<User> contactsList;
	private TextView messageTextView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		IntentFilter intentFilter = new IntentFilter(
                "com.taskmanager.ContactActivity");
		  receiver = new BroadcastReceiver() {
			  
			 
			@Override
			public void onReceive(Context context, Intent intent) {
				
				Log.i("broadcastreceiver_at_contact_activity", "Received");
				createContactsList();
			}
		};
		
		this.registerReceiver(receiver, intentFilter);

		try{
			createContactsList();
		}catch (NullPointerException e) {
			Log.e("error", "NullPointerException");
		}
	}
	private void createContactsList() {
		//Select all of task
		userdatabase.open();
		contactsList = userdatabase.selectAll();
		userdatabase.close();

		ContactsArrayAdapter adapter = new ContactsArrayAdapter(this, contactsList);
		setListAdapter(adapter);
	}
	public void onListItemClick(ListView parent, View v, int position, long id) {
		String name = contactsList.get(position).getFirstname() + " " + contactsList.get(position).getLastname();
		String login = contactsList.get(position).getLogin();		
		Intent intent = new Intent(this, NewTaskActivity.class); 
	    intent.putExtra("name", name);
	    intent.putExtra("login", login);
	    startActivity(intent);     
	}
	
}