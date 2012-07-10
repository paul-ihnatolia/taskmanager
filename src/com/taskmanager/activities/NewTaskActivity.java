package com.taskmanager.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.taskmanager.R;
import com.taskmanager.adapter.TasksArrayAdapter;
import com.taskmanager.asynctasks.SendTask;
import com.taskmanager.database.dao.TaskDataSource;
import com.taskmanager.database.entities.Task;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewTaskActivity extends Activity implements OnClickListener {
	
	private EditText taskEdit;
	Context context = this;
	private String[] priorities = {"low", "average", "high"};
	private List<Task> list;
	private int priority;
	TaskDataSource taskdatabase;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtask);
		
		TextView text = (TextView) findViewById(R.id.name);
		Button sendButton = (Button) findViewById(R.id.send);
		sendButton.setOnClickListener(this);
		taskEdit = (EditText) findViewById(R.id.content);
		
		//Create adapter for spinner 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {	
		    	priority = position + 1;
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		Intent intent = getIntent();
	    String name = intent.getStringExtra("name");
	    String login = intent.getStringExtra("login");
	    
	    text.setText(name + " (" + login + ")");
		try{
			createTaskList(login);
		}catch (NullPointerException e) {
			Log.e("error", "NullPointerException");
			
			Toast toast = Toast.makeText(this, "No task", Toast.LENGTH_LONG);;
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	private void createTaskList(String login){
		list = new ArrayList<Task>();
		taskdatabase.open();
		list = taskdatabase.getAuthorAndRecipient(login);
		taskdatabase.close();
    	TasksArrayAdapter adapterTask = new TasksArrayAdapter(this, R.id.taskslist, list);
   
    	ListView listView = (ListView) findViewById(R.id.taskslist);
    	
    	//
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				
				int green = Color.parseColor("#99cc00");
				int blue = Color.parseColor("#34b6e4");
				int red = Color.parseColor("#ff4444");
				int orange = Color.parseColor("#ffbb33");
				int proirityColor = -1;
				
				Task task = list.get(position);
				
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
					case 5:
						proirityColor = orange;
						break;	
					}
					v.setBackgroundColor(proirityColor);
					
				}
				else{
					Toast toast = Toast.makeText(context, "The task is made", Toast.LENGTH_LONG);;
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
			}
    	);
		listView.setAdapter(adapterTask);
	}
	
	//Send task
	public void onClick(View arg0) {
		Intent intent = getIntent();
		String authToken = getSharedPreferences("CurrentUser", 0).getString("auth_token", null);
		String login = intent.getStringExtra("login");
	    String content = taskEdit.getText().toString();
	    Integer pri = priority;
	    String author = getSharedPreferences("CurrentUser", 0).getString("login", null);
	    ProgressDialog pg = new ProgressDialog(NewTaskActivity.this);
	    try {
			String error = new SendTask(pg).execute(authToken,login,content,pri.toString()).get();
			String result;
			if(error.equals("Success")){			
				taskdatabase.open();
				Task task = new Task(pri, author, new Date().toString(), login, content, "true", 0);
				taskdatabase.insert(task);
				taskdatabase.close();		
				result = "Task was successfully sended!";
				createTaskList(login);
				taskEdit.getText().clear();
			}else{
				result = error;
			}
			new AlertDialog.Builder(NewTaskActivity.this).setMessage(result).
				setNeutralButton("Ok", null).show();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

}