package com.taskmanager.asynctasks;

import java.util.HashMap;

import com.taskmanager.helpers.HttpConnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class SendRequestForFriendship extends
		AsyncTask<String, String, HashMap<String, Object>> {
	
	
	private final String URL = "/protected/add_friend";
	private ProgressDialog pleaseWait;
	
	public SendRequestForFriendship(ProgressDialog pleaseWait) {
		super();
		this.pleaseWait = pleaseWait;
	}

	@Override
	protected HashMap<String, Object> doInBackground(String... params) {
		
		HashMap<String, Object> requestParams = new HashMap<String, Object>();
		int priority;
		String auth_token = params[1];
		String receiverLogin = params[2];
		requestParams.put("auth_token", auth_token);
		requestParams.put("receiver_login",receiverLogin);
		
		if(params[0].equals("request")){
			priority = 4;
		}else {
			String friendship = params[3];
			requestParams.put("friendship", friendship);
			priority = 5;
		}
		requestParams.put("priority", priority);
		
		String responseJson = HttpConnection.makeRequest(URL, requestParams);
	//	Log.e("SendRequestForFriendship", msg)
		return HttpConnection.parse(responseJson, "add_friend", "firstname","lastname","login");
		
	}

	@Override
	protected void onPostExecute(HashMap<String, Object> result) {
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
