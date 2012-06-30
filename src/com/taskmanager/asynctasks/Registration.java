package com.taskmanager.asynctasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class Registration extends AsyncTask<String, Void, String> {
	
	private ProgressDialog pleaseWait;
	
	public Registration(ProgressDialog pleaseWait) {
		super();
		this.pleaseWait = pleaseWait;
	}

	@Override
	protected void onPreExecute() {
		
		pleaseWait.setTitle("Please wait");
		pleaseWait.setMessage("Connecting to server");
		pleaseWait.show();
	}

	@Override
	protected String doInBackground(String... arg0) {
		String firstName = arg0[0];
		String lastName = arg0[1];
		String login = arg0[2];
		String password = arg0[3];
		
		String url = "http://task-manager-project.heroku.com/register";
		HttpPost request = new HttpPost(url);
		JSONObject holder = new JSONObject();
		JSONObject client = new JSONObject();
		
		try{			
			client.put("firstname", firstName);
			client.put("lastname", lastName);
			client.put("login", login);
			client.put("password", password);
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
		
		String errors = parseResponse(responseBody);
		return errors;

	}

	private String parseResponse(String jsonResponse ) {
		
		String error = null;
		if(jsonResponse != null) {
		JSONObject jObject;
		try {
				jObject = new JSONObject(jsonResponse);
				JSONObject sessionObject = jObject.getJSONObject("register");				
				error = sessionObject.getString("error");
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		
		} else {
			
			error = "Server error";
			
		}
		
		return error;	

	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pleaseWait.dismiss();
	}
	
}
