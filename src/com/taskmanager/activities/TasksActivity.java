package com.taskmanager.activities;


import android.R;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class TasksActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ScrollView sroll = new ScrollView(this);
        sroll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        
		LinearLayout layout = new LinearLayout(this);
	    layout.setOrientation(LinearLayout.VERTICAL);
	    layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	    
	    sroll.addView(layout);
	    
		onCreateTask(layout, 3, "Taras", "Hello", "12.12.2012");
		onCreateTask(layout, 3, "Ðóñëàí", "Ïðèâ³ò", "07.20.2012");
		onCreateTask(layout, 1, "Taras", "Hello", "12.12.2012");
		onCreateTask(layout, 2, "Ðóñëàí", "Ïðèâ³ò", "07.20.2012");
		onCreateTask(layout, 2, "Taras", "Hello", "12.12.2012");
		onCreateTask(layout, 1, "Ðóñëàí", "Ïðèâ³ò", "07.20.2012");
		
		setContentView(sroll);		
	}
	
	private void onCreateTask(LinearLayout layout, int priority, String author, String task, String data) {
	    
		int white = Color.parseColor("#ffffff");
		int green = Color.parseColor("#99cc00");
		int blue = Color.parseColor("#34b6e4");
		int red = Color.parseColor("#ff4444");
		
		LinearLayout layout2 = new LinearLayout(this);
	    layout2.setOrientation(LinearLayout.VERTICAL);
	    layout2.setPadding(5, 5, 5, 5);
	    
	    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
	    	     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    
	    layoutParams.setMargins(0, 0, 0, 5);
	     
	    if (priority == 1)
	    	layout2.setBackgroundColor(green);
	    else if (priority == 2) 
	    	layout2.setBackgroundColor(blue);
	    else if (priority == 3) 
	    	layout2.setBackgroundColor(red);
	    else
	    	Log.e("priority", "Invalid priority");
		
	    layout.addView(layout2, layoutParams);
	    
	    TextView titleView = new TextView(this);
	    titleView.setGravity(0x11);
	    titleView.setTextAppearance(this, android.R.attr.textAppearanceLarge);
	    titleView.setText(author);
	    titleView.setTextColor(white);
	    layout2.addView(titleView);

	    TextView textView = new TextView(this);
	    textView.setGravity(0x03);
	    textView.setTextAppearance(this, android.R.attr.textAppearanceLarge);
	    textView.setText(task);
	    textView.setTextColor(white);
	    layout2.addView(textView);
	    
	    TextView dataView = new TextView(this);
	    dataView.setGravity(0x05);
	    dataView.setTextAppearance(this, android.R.attr.textAppearanceSmall);
	    dataView.setText(data);
	    dataView.setTextColor(white);
	    layout2.addView(dataView);
	}
}