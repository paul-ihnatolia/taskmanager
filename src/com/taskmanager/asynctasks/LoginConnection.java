package com.taskmanager.asynctasks;

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
		
		HashMap<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("login", arg0[0]);
		requestParams.put("password", arg0[1]);
		String response = HttpConnection.makeRequest(URL, requestParams);
		HashMap<String, Object> sessionTokens = parseToken(response);
		return sessionTokens;
		
	}

	private HashMap<String, Object> parseToken(String jsonResponse) {
		
		HashMap<String, Object> sessionTokens = new HashMap<String, Object>();
		if(jsonResponse != null) {
		JSONObject jObject;
		
		try {
				Log.i("serverresponse1", jsonResponse);
				
				jObject = new JSONObject(jsonResponse);
				JSONObject sessionObject = jObject.getJSONObject("login");
				String attributeError = sessionObject.getString("error");
				sessionTokens.put("error", attributeError);
				if(attributeError.equals("Success")){
					String attributeToken = sessionObject.getString("auth_token");
					JSONArray friendsJson = sessionObject.getJSONArray("friends");
					User [] friends = null;
					for (int i = 0; i < friendsJson.length(); i++) {
						JSONObject friend = friendsJson.getJSONObject(i);
						String login = friend.getString("login");
						String firstName = friend.getString("firstname");
						String lastName = friend.getString("lastname");
						User u = new User(firstName, lastName, login);
						friends[i] = u;
					}		
					sessionTokens.put("auth_token", attributeToken);
					sessionTokens.put("friends", friends);
				}	
				
			} catch (JSONException e) {
				
				Log.e("jsonexInLoginConnection","jsonExceptionInLoginConnection");
				e.printStackTrace();
			}
		
		} else {
			
			sessionTokens.put("error", "Server error");
			
		}
		
		Log.i("parsetoken",sessionTokens.toString());
		return sessionTokens;	
	
	}
	
	@Override
	protected void onPostExecute(HashMap<String, Object> result) {
		// TODO Auto-generated method stub
		Log.i("onpostexecuteLoginActivity", "onpostexecuteLoginActivity");
		super.onPostExecute(result);
		pleaseWait.dismiss();
	}
	
}
