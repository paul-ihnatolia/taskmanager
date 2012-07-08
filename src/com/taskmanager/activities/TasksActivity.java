package com.taskmanager.activities;


import java.util.List;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
	
	private static final String ACTION_STRING= "Refresh";
	private List<Task> list;
	private TaskDataSource taskData;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try{
			
			taskData = new TaskDataSource(this);
			list = taskData.selectAll();
        	TasksArrayAdapter adapter = new TasksArrayAdapter(this, list);
        	setListAdapter(adapter);
		}
		catch (NullPointerException e) {
			Log.e("error", "NullPointerException");
			
			Toast toast = Toast.makeText(this, "Ó âàñ ùå íå ìàº ïîâ³äîìëåíü", Toast.LENGTH_LONG);;
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
		Task task = list.get(position);
		int green = Color.parseColor("#99cc00");
		int blue = Color.parseColor("#34b6e4");
		int red = Color.parseColor("#ff4444");
		int proirityColor = -1;
		
		if(task.getComplete().equals("false")){
			task.setComplete("true");
			taskData.update(task);
			
			if (list.get(position).getPriority() == 1)
	        	proirityColor = red;
		    else if (list.get(position).getPriority() == 2) 
		    	proirityColor = blue;
		    else 
		    	proirityColor = green;
			v.setBackgroundColor(proirityColor);
			
		}
		else{
			Toast toast = Toast.makeText(this, "Çàâäàííÿ âæå ïðî÷èòàíå", Toast.LENGTH_SHORT);;
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

}