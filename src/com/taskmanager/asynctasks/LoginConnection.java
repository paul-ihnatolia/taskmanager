package com.taskmanager.asynctasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class LoginConnection extends AsyncTask<String, Void, Boolean> {
	private UrlEncodedFormEntity formEntity;
	private ProgressDialog pleaseWait;
	private final TextView message;
	
	
	public LoginConnection(ProgressDialog pleaseWait, TextView message) {
		super();
		this.pleaseWait = pleaseWait;
		this.message = message;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		pleaseWait.setTitle("Please wait");
		pleaseWait.setMessage("Connecting to server");
		pleaseWait.show();
	}
	
	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
	
		String url = "http://json-login.heroku.com/login";
		
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("login", arg0[0]));
		postParameters.add(new BasicNameValuePair("pass", arg0[1]));
		
		try {
			formEntity = new UrlEncodedFormEntity(postParameters);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpPost request = new HttpPost(url);
		request.setEntity(formEntity);
		ResponseHandler<String> rhandler = new BasicResponseHandler();
		HttpClient client = new DefaultHttpClient();
		
		boolean chi = false;
		try {
			String responseBody = client.execute(request, rhandler);
			JSONObject json = new JSONObject(responseBody);
			Log.i("json", json.toString());
			chi = json.getBoolean("auth");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return chi;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		pleaseWait.dismiss();
		if(result){
			message.setText("Connected!");
		}else{
			message.setText("Wrong login or password!");
		}

	}

}
