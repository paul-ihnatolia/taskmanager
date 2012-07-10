package com.taskmanager.asynctasks;

import java.util.HashMap;

import com.taskmanager.helpers.HttpConnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class LogOut extends AsyncTask<String, String, String> {
	
	private final String URL = "/protected/logout";
	private ProgressDialog pleaseWait;
	
	public LogOut(ProgressDialog pg) {
		super();
		this.pleaseWait = pg;
	}

	@Override
	protected String doInBackground(String... arg0) {
		String auth_token = arg0[0];
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("auth_token", auth_token);
		String response = HttpConnection.makeRequest(URL, params);
		return HttpConnection.parse(response, "logout", "error").get("error").toString();
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pleaseWait.dismiss();
	}

	@Override
	protected void onPreExecute() {
		pleaseWait.setTitle("Please wait");
		pleaseWait.setMessage("Connecting to server");
		pleaseWait.show();
	}

}
