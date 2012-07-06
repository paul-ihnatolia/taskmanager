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

public class Search extends AsyncTask<String, String, ArrayList<User>> {
	
	private ProgressDialog pleaseWait;
	private static String URL = "/protected/find_user";
	
	public Search(ProgressDialog pleaseWait) {
		super();
		this.pleaseWait = pleaseWait;
	}
	
	@Override
	protected ArrayList<User> doInBackground(String... params) {
		
		HashMap<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("auth_token", params[0]);
		requestParams.put("search_value", params[1]);
		String response = HttpConnection.makeRequest(URL, requestParams);
		ArrayList<User> results = parseResponse(response);
		return results;
		
	}

	private ArrayList<User> parseResponse(String responseBody) {
		ArrayList<User> results = null;
		if(responseBody!=null){
			try {
				JSONObject jobject = new JSONObject(responseBody).getJSONObject("find_user");
				if(jobject.getString("error").equals("Success")){
					JSONArray resultsJson = jobject.getJSONArray("users");
					results = new ArrayList<User>();
					for (int i = 0; i < resultsJson.length(); i++) {
						JSONObject friend = resultsJson.getJSONObject(i);
						String login = friend.getString("login");
						String firstName = friend.getString("firstname");
						String lastName = friend.getString("lastname");
						User u = new User(firstName, lastName, login);
						results.add(u);
					}	
					
				}else{
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			
		}
		
		Log.i("Search", responseBody.toString());
		
		return results;
	}

	@Override
	protected void onPostExecute(ArrayList<User> results) {
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
