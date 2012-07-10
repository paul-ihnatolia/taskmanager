package com.taskmanager.activities;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.taskmanager.adapter.TasksArrayAdapter;
import com.taskmanager.asynctasks.SendRequestForFriendship;
import com.taskmanager.database.dao.TaskDataSource;
import com.taskmanager.database.dao.UserDataSource;
import com.taskmanager.database.entities.Task;
import com.taskmanager.database.entities.User;

public class TasksActivity extends ListActivity{
	
	final int DIALOG_ADD_FRIEND = 1;
	private List<Task> list;
	private BroadcastReceiver receiver;
	TaskDataSource taskdatabase;
	private int positionUser;
	UserDataSource userdatabase;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		taskdatabase = new TaskDataSource(this);
		userdatabase = new UserDataSource(this);
		IntentFilter intentFilter = new IntentFilter(
                "com.taskmanager.TasksActivity");
		  receiver = new BroadcastReceiver() {
			  
			 
			@Override
			public void onReceive(Context context, Intent intent) {
				
				Log.i("broadcastreceiver_at task_activity", "Received");
				createTaskList();
			}
		};
		Log.i("taskactivity","receiver is created");
		this.registerReceiver(receiver, intentFilter);
		
		try{
			createTaskList();
		}catch (NullPointerException e) {
			Log.e("error", "NullPointerException");
			
			Toast toast = Toast.makeText(this, "Tasks are not already", Toast.LENGTH_SHORT);;
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	
	private void createTaskList(){
		taskdatabase.open();
		String login = getSharedPreferences("CurrentUser", 0).getString("login", null);
		list = taskdatabase.getRecipientAll(login);
		taskdatabase.close();
    	TasksArrayAdapter adapter = new TasksArrayAdapter(this, list);
    	setListAdapter(adapter);
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
		
		Task task = list.get(position);
		int green = Color.parseColor("#99cc00");
		int blue = Color.parseColor("#34b6e4");
		int red = Color.parseColor("#ff4444");
		int orange = Color.parseColor("#ffbb33");
		int white = Color.parseColor("#ffffff");
		int proirityColor = -1;
		
		if(task.getComplete().equals("false")){
			task.setComplete("true");
			taskdatabase.open();
			taskdatabase.update(task);
			taskdatabase.close();
			
			switch (list.get(position).getPriority()) {
			case 1:
				proirityColor = red;
				break;
			case 2:
				proirityColor = blue;
				break;
			case 3:
				proirityColor = green;
				break;
			case 4:
				proirityColor = orange;
				showDialog(DIALOG_ADD_FRIEND);
				positionUser=position;
				break;
			case 5:
				proirityColor = orange;
				break;	
			}
			v.setBackgroundColor(proirityColor);
			
		}
		else{
			Toast toast = Toast.makeText(this, "The task is made", Toast.LENGTH_SHORT);;
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_ADD_FRIEND) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
	        
	        adb.setTitle("Add friend");
	        adb.setMessage("Submit a request?");
	      
	        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					HashMap<String, Object> results = friendship("Ok");
					if(results.get("error").equals("Success")){
						String login = results.get("login").toString();
						String firstName = results.get("firstname").toString();
						String lastName = results.get("lastname").toString();
						userdatabase.open();
						User user = new User(firstName,lastName,login);
						userdatabase.insert(user);
						userdatabase.close();
						sendBroadcast(new Intent("com.taskmanager.ContactActivity"));
					}
					new AlertDialog.Builder(TasksActivity.this).setTitle("Result").setMessage(results.get("error").toString()).
						setNeutralButton("Ok", null).show();
				}
	        });
	        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					HashMap<String, Object> results = friendship("No");
					dialog.cancel();	
				}
	        });

	    	return adb.create();
	    }
		
		return super.onCreateDialog(id);
	}
	
	  private HashMap<String, Object> friendship(String button){
			ProgressDialog pg = new ProgressDialog(TasksActivity.this);
			String auth_token = getSharedPreferences("CurrentUser", 0).getString("auth_token", null);
			String login = list.get(positionUser).getAuthor();
			String chi;
			if(button.equals("Ok")){
				chi = "true";
			}else{
				chi="false";
			}
			
			HashMap<String, Object> results = new HashMap<String, Object>();
			try {
				results = new SendRequestForFriendship(pg).
						execute("response",auth_token,login,chi).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return results;
	  }
}