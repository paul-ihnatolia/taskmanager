package com.taskmanager.asynctasks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.taskmanager.database.entities.User;
import com.taskmanager.helpers.HttpConnection;

public class Search extends AsyncTask<String, String, HashMap<String,Object>> {
	
	private ProgressDialog pleaseWait;
	private static String URL = "/protected/find_user";
	
	public Search(ProgressDialog pleaseWait) {
		super();
		this.pleaseWait = pleaseWait;
	}
	
	@Override
	protected HashMap<String,Object> doInBackground(String... params) {
		
		HashMap<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("auth_token", params[0]);
		requestParams.put("search_value", params[1]);
		String response = HttpConnection.makeRequest(URL, requestParams);
		HashMap<String, Object> results = parseResponse(response);
		return results;
		
	}

	private HashMap<String, Object> parseResponse(String responseBody) {
		
		HashMap<String, Object> results = new HashMap<String, Object>();
		results = HttpConnection.parse(responseBody, "find_user", "users");
		ArrayList<User> users = null;
		
		if(results.get("error").equals("Success")){
			JSONArray usersJson = (JSONArray)results.get("users");
			users = new ArrayList<User>();
			for (int i = 0; i < usersJson.length(); i++) {
				JSONObject friend;
				try {
					friend = usersJson.getJSONObject(i);
					String login = friend.getString("login");
					String firstName = friend.getString("firstname");
					String lastName = friend.getString("lastname");
					User u = new User(firstName, lastName, login);
					users.add(u);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			results.put("users", users);
		}	
	
		Log.i("Search", responseBody.toString());
		return results;
	}

	@Override
	protected void onPostExecute(HashMap<String,Object> results) {
		// TODO Auto-generated method stub
		super.onPostExecute(results);
		pleaseWait.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pleaseWait.setTitle("Please wait");
		pleaseWait.setMessage("Connecting to server");
		pleaseWait.show();
	}
	
	
}
