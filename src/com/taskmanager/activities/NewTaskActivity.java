package com.taskmanager.activities;

import java.util.ArrayList;
import java.util.List;

import com.taskmanager.R;
import com.taskmanager.adapter.TasksArrayAdapter;
import com.taskmanager.database.dao.TaskDataSource;
import com.taskmanager.database.entities.Task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class NewTaskActivity extends Activity implements OnClickListener{
	
	private EditText taskEdit;
	private String[] priority = {"�������", "�������", "�������"};
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtask);
		
		TextView text = (TextView) findViewById(R.id.name);
		Button sendButton = (Button) findViewById(R.id.send);
		sendButton.setOnClickListener(this);
		taskEdit = (EditText) findViewById(R.id.serchfield);
		
		//Create adapter for spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priority);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setAdapter(adapter);
		
		//���� �������� �����������
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		Intent intent = getIntent();
	    String name = intent.getStringExtra("name");
	    String login = intent.getStringExtra("login");
	    
	    text.setText(name + " (" + login + ")");
	    
	    List<Task> list = new ArrayList<Task>();

		//Select all of task 
		TaskDataSource taskData = new TaskDataSource(this);
		list = taskData.getTask(login);
		
        TasksArrayAdapter adapterTask = new TasksArrayAdapter(this, R.id.taskslist, list);
       
        ListView listView = (ListView) findViewById(R.id.taskslist);
		listView.setAdapter(adapterTask);
   
	}

	public void onClick(View v) {
		switch (v.getId()) {
	
		case R.id.send:
			//��� �� ���� ����� ��� �������� �����������
			break;
		}
	}
		
}
