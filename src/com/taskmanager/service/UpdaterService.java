package com.taskmanager.service;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.taskmanager.database.dao.TaskDataSource;
import com.taskmanager.database.dao.UserDataSource;
import com.taskmanager.database.entities.Task;
import com.taskmanager.database.entities.User;
import com.taskmanager.helpers.HttpConnection;
import com.taskmanager.helpers.NotificationUtils;

public class UpdaterService extends Service {

	private static final String TAG = UpdaterService.class.getSimpleName();
	private Updater updater;
	public boolean isRunning = false;
	private static HashMap<String, Object> requestParams;
	TaskDataSource taskdatabase;
	UserDataSource userDataSource;
	private String authToken;
	private String login;
	private Context context = this;
	
	@Override
	public void onCreate() {
		
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreated'd");
		updater = new Updater();
		super.onCreate();
		authToken = getSharedPreferences("CurrentUser", 0).getString(
				"auth_token", null);
		login = getSharedPreferences("CurrentUser", 0).getString("login", null);
		taskdatabase = new TaskDataSource(this);
		userDataSource = new UserDataSource(this);
		if (authToken == null) {
			Log.e(TAG, "Token error");
			this.stopSelf();
		} else {
			requestParams = new HashMap<String, Object>();
			requestParams.put("auth_token", authToken);
		}
	}

	@Override
	public synchronized void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRunning) {
			updater.stop();
			this.isRunning = false;
		}

		Log.d(TAG, "onDestroy'd");
	}

	@Override
	public synchronized void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		if (!isRunning) {
			updater.start();
			this.isRunning = true;
		}

		Log.d(TAG, "onStart'd");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	// Update thread
	class Updater extends Thread {
		private static final long DELAY = 30000;
		private static final String URL = "/protected/get_task";

		@Override
		public void run() {
			while (true) {
				// TODO Auto-generated method stub
				super.run();
				Log.d(TAG, "Service is running!");
				checkForNewTask();
				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void checkForNewTask() {
			requestParams.put("auth_token", authToken);
			Log.i(TAG, requestParams.toString());
			String response = HttpConnection.makeRequest(URL, requestParams);
			if(response != null){
				parseResponse(response);
			}else{
				Log.e(TAG, "Server response is null!");
			}	
		}

		private void parseResponse(String responseJsone) {
			Log.i(TAG, responseJsone);
			HashMap<String, Object> response = HttpConnection.parse(
					responseJsone, "get_task", "quantity", "tasks");
			if (response.get("error").equals("Success")) {
				if (Integer.parseInt(response.get("quantity").toString()) != 0) {

					try {
						taskdatabase.open();
						JSONArray tasksJson = (JSONArray) response.get("tasks");
						String authorLogin=null;
						for (int i = 0; i < tasksJson.length(); i++) {
							JSONObject task = tasksJson.getJSONObject(i);
							Integer serverId = task.getInt("id");
							String content = task.getString("content");
							authorLogin = task.getString("user_login");
							Integer priority = task.getInt("priority");
							
							if (priority == 5)
								content = saveNewFriend(content, authorLogin);

							String createdAt = task.getString("created_at");
							
							Task t = new Task(priority, authorLogin, createdAt,
									login, content, "false", serverId, login);
							taskdatabase.insert(t);
							
							
						}

						taskdatabase.close();
						
						//create notification
						NotificationUtils notification = NotificationUtils.getInstance(context);
						notification.createInfoNotification("You have new message!", login);
						
						
						sendBroadcast(new Intent(
								"com.taskmanager.TasksActivity").putExtra("sound", true));
						if(com.taskmanager.activities.NewTaskActivity.isActive() && 
								com.taskmanager.activities.NewTaskActivity.getLogin().equals(authorLogin)){
							sendBroadcast(new Intent(
									"com.taskmanager.NewTaskActivity"));
						}
						
					    
						Log.i(TAG, "parsing");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				Log.e(TAG, response.get("error").toString());
			}
		}

		private String saveNewFriend(String content, String login) {
			String[] contentArray = content.split(" ");
			String firstname = contentArray[0];
			String lastname = contentArray[1];

			if (contentArray[2].equals("true")) {
				User user = new User(firstname, lastname, login);
				userDataSource.open();
				userDataSource.insert(user);
				userDataSource.close();
				content = firstname + " " + lastname + " added you to friend";
				sendBroadcast(new Intent("com.taskmanager.ContactActivity"));
			} else {

				content = firstname + " " + lastname + " didn't add you to friend";
			}

			return content;
		}
	}
}
