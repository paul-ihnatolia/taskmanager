package com.taskmanager.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.taskmanager.R;
import com.taskmanager.asynctasks.Search;
import com.taskmanager.database.entities.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class NewFriendActivity extends Activity implements OnClickListener{

	private ListView frendList;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newfriend);
		
		Button search = (Button) findViewById(R.id.searchbutton);
		frendList = (ListView) findViewById(R.id.serchList);
				
		search.setOnClickListener(this);
		
	}

	private List<Map<String, String>> createContactsList(ArrayList<User> results) {	
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		for (User i : results) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i.getFirstname() + " " + i.getLastname());
			map.put("login", i.getLogin());

			items.add(map);
		}
			return items;
	}
	
	public void onClick(View v) {
		
		String searchItem = ((EditText) findViewById(R.id.serchfield)).getText().toString();
		String authToken = getSharedPreferences("CurrentUser", 0).getString("auth_token", null);
		ProgressDialog pleaseWait = new ProgressDialog(NewFriendActivity.this);
		
		try {
			HashMap<String, Object> results = new Search(pleaseWait).execute(authToken,searchItem).get();
			if(!results.get("error").equals("Success")){
				Log.e("error", results.get("error").toString());
			}else{
				
				ArrayList<User> users = (ArrayList<User>) results.get("users");
				SimpleAdapter adapter = new SimpleAdapter(NewFriendActivity.this, createContactsList(users), android.R.layout.simple_list_item_2,
						new String[] {"name", "login"},
						new int[] {android.R.id.text1, android.R.id.text2});
				
				frendList.setAdapter(adapter);
				
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}catch (Exception e) {
			Log.e("new friend","some exception");
		}
	}
}