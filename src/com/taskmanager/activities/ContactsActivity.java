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
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;



public class ContactsActivity extends ListActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		SimpleAdapter adapter = new SimpleAdapter(this, createContactsList(), android.R.layout.simple_list_item_2, 
	            new String[] {"name", "login"}, 
	            new int[] {android.R.id.text1, android.R.id.text2});
	  
	    setListAdapter(adapter);    
	}

	private List<Map<String, String>> createContactsList() {
		
		//Select all of task
		UserDataSource userData = new UserDataSource(this);
		ArrayList<User> contacts = userData.selectAll();
		
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		
		for (User i : contacts) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i.getFirstname() + " " + i.getLastname());
			map.put("login", i.getLogin());
			
			items.add(map);
		}
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
