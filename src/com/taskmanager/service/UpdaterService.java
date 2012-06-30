package com.taskmanager.service;

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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	
	private static final String TAG = UpdaterService.class.getSimpleName();
	private Updater updater;
	public boolean isRunning = false;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreated'd");
		updater = new Updater();
		super.onCreate();
	}

	@Override
	public synchronized void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(isRunning){
			updater.stop();
			this.isRunning=false;
		}
		
		Log.d(TAG, "onDestroy'd");
	}

	@Override
	public synchronized void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		if(!isRunning){
			updater.start();
			this.isRunning=true;
		}
		
		Log.d(TAG, "onStart'd");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Update thread
	class Updater extends Thread {
		static final long DELAY = 30000;
		static final String url = "http://task-manager-project.heroku.com/protected/get_task";
		@Override
		public void run() {
			while (true) {
				// TODO Auto-generated method stub
				super.run();
				checkForNewTask();
				//checkForNewFriendRequests();
				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		private void checkForNewTask() {
			// TODO Auto-generated method stub
			HttpPost request = new HttpPost(url);
			JSONObject holder = new JSONObject();
			JSONObject client = new JSONObject();
					
			String authToken = getSharedPreferences("CurrentUser", 0).getString("auth_token", null);
			
			if(authToken==null){
				
				Log.e(TAG, "Token error");
				
			}else{
				
				try{
					
					client.put("auth_token", authToken);
					holder.put("taskmanager", client);
					
					StringEntity se = new StringEntity(holder.toString());
					request.setEntity(se);
					request.setHeader("Accept", "taskmanager/json");
					request.setHeader("Content-Type", "taskmanager/json");
					Log.i(TAG, holder.toString());
					
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
					Log.i("login", "executing");
					responseBody = httpClient.execute(request, rhandler);
					Log.i("login", "post-executing");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				parseResponse(responseBody);
			}
					
		}

		private void parseResponse(String responseBody) {
			
			if(responseBody == null){
				Log.e(TAG, "Server error");
			}else{
				JSONObject jObject;
				
				try {
					jObject = new JSONObject(responseBody).getJSONObject("get_task");
					if(jObject.getString("errors").equals("Success")){
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}
			
		}
		
	}
}
