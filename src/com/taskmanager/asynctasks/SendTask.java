package com.taskmanager.asynctasks;

import java.util.HashMap;

import com.taskmanager.helpers.HttpConnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class SendTask extends AsyncTask<String, String, String> {
	
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
		Log.i("sendtask", response.toString());
		return (String) results.get("error");
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
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
