package com.taskmanager.service;

import java.util.HashMap;

import org.json.JSONArray;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.taskmanager.helpers.HttpConnection;

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
		private static final long DELAY = 30000;
		private static final String URL = "/protected/get_task";
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
			String authToken = getSharedPreferences("CurrentUser", 0).getString("auth_token", null);
			if(authToken==null){
				Log.e(TAG, "Token error");
			}else{
				HashMap<String, String> requestParams = new HashMap<String, String>();
				requestParams.put("auth_token", authToken);
				String response = HttpConnection.makeRequest(URL, requestParams);
				parseResponse(response);
			}
		}

		private void parseResponse(String responseJsone) {
			HashMap<String, Object> response = HttpConnection.name(responseJsone, "get_task", "quantity","tasks");
			if(response.get("error").equals("Success")){
				if(!response.get("quantity").equals("0")){
					JSONArray tasksJson = (JSONArray) response.get("tasks");
					parseTasks();
				}
			}
		}

		private void parseTasks() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
