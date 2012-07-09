package com.taskmanager.activities;


import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
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
import com.taskmanager.database.dao.TaskDataSource;
import com.taskmanager.database.entities.Task;

public class TasksActivity extends ListActivity{
	
	final int DIALOG_ADD_FRIEND = 1;
	private List<Task> list;
	private BroadcastReceiver receiver;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		IntentFilter intentFilter = new IntentFilter(
                "com.taskmanager.TasksActivity");
		  receiver = new BroadcastReceiver() {
			  
			 
			@Override
			public void onReceive(Context context, Intent intent) {
				
				Log.i("broadcastreceiver_at task_activity", "Received");
				
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
		list = TaskDataSource.selectAll();
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
			TaskDataSource.update(task);
			
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
										
				}
	        });
	        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();	
				}
	        });

	    	return adb.create();
	    }
		return super.onCreateDialog(id);
	}

}