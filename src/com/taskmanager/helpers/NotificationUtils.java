package com.taskmanager.helpers;

import java.util.HashMap;

import com.taskmanager.activities.MainMenuActivity;
import com.taskmanager.activities.TasksActivity;
import com.taskmanager.database.dao.TaskDataSource;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationUtils {
	private static final String TAG = NotificationUtils.class.getSimpleName();  
	private static NotificationUtils instance;
	private static Context context;
	private NotificationManager manager; 
	private HashMap<Integer, Notification> notifications; 
	 
	private NotificationUtils(Context context){
	   this.context = context;
	   manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	   notifications = new HashMap<Integer, Notification>();
	}

	public static NotificationUtils getInstance(Context context){
		if(instance == null)
	        instance = new NotificationUtils(context);
		else
	        instance.context = context;
	    return instance;
	}
	
	public void createInfoNotification(String message, String login){
	    int icon = android.R.drawable.sym_action_email;
	    int id = 0; 
	    int numberNewTasks = 0;
	    CharSequence titleText = message; 
	    long when = System.currentTimeMillis();
	    
	    
	    TaskDataSource taskdatabase = new TaskDataSource(context);
	    taskdatabase.open();
	    numberNewTasks = taskdatabase.sizeNewTasks(login);
	    
	    Notification notification = new Notification(icon, titleText, when);
	    Intent intent = new Intent(context, MainMenuActivity.class);
	    
	    PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);
		notification.setLatestEventInfo(context, message, "You have "+ numberNewTasks +" new messages.", activity);
		
		//Cancel notification 
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		manager.notify(id, notification);
	    
	}
	public void clearInfoNotification(){
		
	}
	
}
