package com.taskmanager.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.taskmanager.database.dao.UserDataSource;
import com.taskmanager.database.entities.User;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ContactsActivity extends ListActivity {
	UserDataSource userdatabase = new UserDataSource(this);
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try{
			createContactsList();
		}
		catch (NullPointerException e) {
			Log.e("error", "NullPointerException");
			
			Toast toast = Toast.makeText(this, "No contact", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	private List<Map<String, String>> createContactsList() {
		
		//Select all of task
		userdatabase.open();
		ArrayList<User> contacts = userdatabase.selectAll();
		userdatabase.close();
		
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		
		for (User i : contacts) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i.getFirstname() + " " + i.getLastname());
			map.put("login", i.getLogin());
			
			items.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, 
	            new String[] {"name", "login"}, 
	            new int[] {android.R.id.text1, android.R.id.text2});
		
		setListAdapter(adapter);
		
		return items;
	}
	public void onListItemClick(ListView parent, View v, int position, long id) {
		String name = createContactsList().get(position).get("name");
		String login = createContactsList().get(position).get("login");		
		Intent intent = new Intent(this, NewTaskActivity.class); 
	    intent.putExtra("name", name);
	    intent.putExtra("login", login);
	    startActivity(intent);     
	}
	
}