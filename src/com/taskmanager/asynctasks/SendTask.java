package com.taskmanager.asynctasks;

import java.util.HashMap;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.taskmanager.helpers.HttpConnection;

public class SendTask extends AsyncTask<String, Void, String> {
	
	private final String URL = "/protected/new_task";
	private ProgressDialog pleaseWait;
	
	public SendTask(ProgressDialog pleaseWait) {
		super();
		this.pleaseWait = pleaseWait;
	}

	@Override
	protected String doInBackground(String... arg0) {
		
		String auth_token = arg0[0];
		String receiver_login = arg0[1];
		String content = arg0[2];
		Integer priority = Integer.parseInt(arg0[3]);
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("auth_token", auth_token);
		params.put("receiver_login", receiver_login);
		params.put("content", content);
		params.put("priority", priority);
		
		String response = HttpConnection.makeRequest(URL, params);
		HashMap<String, Object> results = HttpConnection.parse(response, "new_task", "error");
		return (String) results.get("error");
	}

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);
		pleaseWait.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pleaseWait.setTitle("Please wait");
		pleaseWait.setMessage("Sending task");
		pleaseWait.show();	
	}

}