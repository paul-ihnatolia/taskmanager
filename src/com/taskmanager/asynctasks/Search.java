package com.taskmanager.asynctasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.taskmanager.database.entities.User;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class Search extends AsyncTask<String, String, ArrayList<User>> {
	
	private ProgressDialog pleaseWait;
	
	public Search(ProgressDialog pleaseWait) {
		super();
		this.pleaseWait = pleaseWait;
	}
	
	@Override
	protected ArrayList<User> doInBackground(String... params) {
		
		String authToken = params[0];
		String serchValue = params[1];
		String url = "http://task-manager-project.heroku.com/protected/find_user";
		HttpPost request = new HttpPost(url);
		JSONObject holder = new JSONObject();
		JSONObject client = new JSONObject();
		
		try{
			
			client.put("auth_token", authToken);
			client.put("search_value", serchValue);
			holder.put("taskmanager", client);
			StringEntity se = new StringEntity(holder.toString());
			request.setEntity(se);
			request.setHeader("Accept", "taskmanager/json");
			request.setHeader("Content-Type", "taskmanager/json");
			Log.i("client", holder.toString());
			
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResponseHandler<String> rhandler = new BasicResponseHandler();
		HttpClient httpClient = new DefaultHttpClient();
		
		String responseBody = null;
		
		try {
			responseBody = httpClient.execute(request, rhandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<User> results = parseResponse(responseBody);
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
