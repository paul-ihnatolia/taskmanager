package com.taskmanager.activities;

import com.taskmanager.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainMenuActivity extends TabActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
     
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ContactsActivity.class);
     
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("contacts").setIndicator("Êîíòàêòè",
                          res.getDrawable(R.drawable.ic_tab_contacts))
                      .setContent(intent);
        tabHost.addTab(spec);
     
        // Do the same for the other tabs
        intent = new Intent().setClass(this, TasksActivity.class);
        spec = tabHost.newTabSpec("tasks").setIndicator("Çàâäàííÿ",
                          res.getDrawable(R.drawable.ic_tab_message))
                      .setContent(intent);
        tabHost.addTab(spec);
          
        tabHost.setCurrentTab(0);
		
	}

}
