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

public class LoginConnection extends AsyncTask<String,Void,HashMap<String, Object>> {

	private ProgressDialog pleaseWait;
	private static String URL = "/login";
	
	public LoginConnection(ProgressDialog pleaseWait) {
		super();
		this.pleaseWait = pleaseWait;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		pleaseWait.setTitle("Please wait");
		pleaseWait.setMessage("Connecting to server");
		pleaseWait.show();
		Log.i("login", "onpreexecute");
	}
	
	@Override
	protected HashMap<String, Object> doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		
		HashMap<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("login", arg0[0]);
		requestParams.put("password", arg0[1]);
		String response = HttpConnection.makeRequest(URL, requestParams);
		HashMap<String, Object> sessionTokens = parseToken(response);
		return sessionTokens;
		
	}

	private HashMap<String, Object> parseToken(String jsonResponse) {
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result = HttpConnection.parse(jsonResponse, "login", "auth_token","friends");

		if(result.get("error").equals("Success")){
			Log.i("parsetoken",result.toString());
			JSONArray friendsJson = (JSONArray) result.get("friends");
			ArrayList<User> friends = new ArrayList<User>();
			for (int i = 0; i < friendsJson.length(); i++) {
				JSONObject friend;
				try {
					friend = friendsJson.getJSONObject(i);
					String login = friend.getString("login");
					String firstName = friend.getString("firstname");
					String lastName = friend.getString("lastname");
					User u = new User(firstName, lastName, login);
					friends.add(u);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			result.put("friends", friends);
		}			
		return result;	
	
	}
	
	@Override
	protected void onPostExecute(HashMap<String, Object> result) {
		// TODO Auto-generated method stub
		Log.i("onpostexecuteLoginActivity", "onpostexecuteLoginActivity");
		super.onPostExecute(result);
		pleaseWait.dismiss();
	}
	
}
