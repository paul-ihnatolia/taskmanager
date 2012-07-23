package com.taskmanager.asynctasks;

import java.util.HashMap;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.taskmanager.helpers.HttpConnection;

public class Registration extends AsyncTask<String, Void, String> {
	
	private ProgressDialog pleaseWait;
	private static String URL = "/register";
	
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

		HashMap<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("firstname", arg0[0]);
		requestParams.put("lastname", arg0[1]);
		requestParams.put("login", arg0[2]);
		requestParams.put("password", arg0[3]);
		
		String response = HttpConnection.makeRequest(URL, requestParams);
		String errors = parseResponse(response);
		return errors;
		
	}

	private String parseResponse(String jsonResponse ) {
		
		return HttpConnection.parse(jsonResponse, "register", "error").get("error").toString();
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pleaseWait.dismiss();
	}
	
}
