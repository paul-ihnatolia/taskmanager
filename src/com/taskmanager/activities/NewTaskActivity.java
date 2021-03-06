package com.taskmanager.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taskmanager.R;
import com.taskmanager.adapter.TasksArrayAdapter;
import com.taskmanager.asynctasks.SendTask;
import com.taskmanager.database.dao.TaskDataSource;
import com.taskmanager.database.entities.Task;

public class NewTaskActivity extends Activity implements OnClickListener {
	
	private static final String TAG = NewTaskActivity.class.getSimpleName();
	private EditText taskEdit;
	Context context = this;
	private String[] priorities = {"Low priority" ,"Medium priority","High priority"};
	private List<Task> list;
	private int priority;
	private final int DIALOG_COMPLETE = 1;
	private int positionUser;
	private View taskListView;
	private TaskDataSource taskdatabase;
	private ImageButton sendButton;
	private ImageButton closeButton;
	// if activity is active
	private static boolean active = false;
	private BroadcastReceiver receiver;
	private TasksArrayAdapter adapterTask;
	private static String recipientLogin;
	
	@Override
	protected void onStart() {
		
		super.onStart();
		this.active = true;
		Log.i("newtaskactivity", "active");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.active = false;
		Log.i("newtaskactivity", "not active");
	}
	
	
	public static String getLogin() {
		if(recipientLogin == null)
			return "";
		return recipientLogin;
	}

	public static Boolean isActive() {
		return active;
	}

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtask);
		
		TextView text = (TextView) findViewById(R.id.name);
		
		sendButton = (ImageButton) findViewById(R.id.send);
		sendButton.setOnClickListener(this);
		
		closeButton = (ImageButton) findViewById(R.id.closeButton);
		closeButton.setOnClickListener(this);
		
		taskEdit = (EditText) findViewById(R.id.content);
		
		Intent intent = getIntent();
	    String name = intent.getStringExtra("name");
	    recipientLogin = intent.getStringExtra("login");
		
	    Log.d(TAG, "login is " + recipientLogin);
		IntentFilter intentFilter = new IntentFilter(
                "com.taskmanager.NewTaskActivity");
		  receiver = new BroadcastReceiver() {
			  
			 
			@Override
			public void onReceive(Context context, Intent intent) {
				
				Log.i("broadcastreceiver_at_new_task_activity", "Received");
				if(active == true && adapterTask != null){
					createTaskList(recipientLogin);
					Log.i(TAG, "creating tasklist...");
				}
			}
		};
		
		this.registerReceiver(receiver, intentFilter);

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
		
		taskdatabase = new TaskDataSource(this);
		

	    
	    text.setText(name + " (" + recipientLogin + ")");
		try{
			createTaskList(recipientLogin);
		}catch (NullPointerException e) {
			Log.e("error", "NullPointerException");			
			Toast toast = Toast.makeText(this, "No task", Toast.LENGTH_LONG);;
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private void createTaskList(String authorLogin){
		String login = getSharedPreferences("CurrentUser", 0).getString("login", null);
		
		list = new ArrayList<Task>();
		taskdatabase.open();
		
		list = taskdatabase.getOwnerAndAuthor(login, authorLogin);
		list.addAll(taskdatabase.getAuthorAndRecipient(login, recipientLogin));
		
		list = taskdatabase.sortTaskList((ArrayList<Task>)list);
		
		taskdatabase.close();
    	adapterTask = new TasksArrayAdapter(this, R.id.taskslist, list);
  
    	ListView listView = (ListView) findViewById(R.id.taskslist);
    	
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				
				Task task = list.get(position);
				
				if(task.getComplete().equals("false")) {
					positionUser = position;
					taskListView = v;
					showDialog(DIALOG_COMPLETE);
				}
				else{
					Toast toast = Toast.makeText(context, "Already completed", Toast.LENGTH_LONG);;
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});
		listView.setAdapter(adapterTask);
	}
	
	//Send task
	public void onClick(View button) {
		if(sendButton.getId() == button.getId()){
			
			String authToken = getSharedPreferences("CurrentUser", 0).getString("auth_token", null);
		    String content = taskEdit.getText().toString();
		    Integer pri = priority;
		    String author = getSharedPreferences("CurrentUser", 0).getString("login", null);
		    ProgressDialog pg = new ProgressDialog(NewTaskActivity.this);
		    try {
		    	String error = new SendTask(pg).execute(authToken, recipientLogin, content,pri.toString()).get();
		    	if(error.equals("Success")){
		    		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		    		
			    	taskdatabase.open();
			    	Task task = new Task(pri, author, dateFormat.format(new Date()).toString(), recipientLogin, content, "true", 0, author);
			    	taskdatabase.insert(task);
			    	taskdatabase.close();	
			    	createTaskList(recipientLogin);
			    	taskEdit.getText().clear();
			    	
		    	}else{
			    	new AlertDialog.Builder(NewTaskActivity.this).setTitle("Error").setMessage(error).
			    		setNeutralButton("Ok", null).show();
		    	}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(closeButton.getId() == button.getId()){
			Log.i("close_button", "was clicked");
			finish();
		}
	}
	
	protected Dialog onCreateDialog(int id) {
		//Create dialogue for the task
		if (DIALOG_COMPLETE == id) {
			AlertDialog.Builder adb2 = new AlertDialog.Builder(this);
	        
	        adb2.setTitle("Task");
	        adb2.setMessage("You have completed the task?");
	      
	        adb2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					ImageView completeImageView = (ImageView) findViewById(R.id.completeImageView);
					Task task = list.get(positionUser);
					task.setComplete("true");
					taskdatabase.open();
					taskdatabase.update(task);
					taskdatabase.close();
					
					completeImageView.setImageResource(R.drawable.complete);
					
					//Update task list
					createTaskList(recipientLogin);
				}
			});
	        
	        adb2.setNegativeButton("No", new DialogInterface.OnClickListener() {
	   			public void onClick(DialogInterface dialog, int which) {
	   				dialog.cancel();	
	   			}
	   	    });
	        return adb2.create();
		}
		
		return super.onCreateDialog(id);
	}
	
}