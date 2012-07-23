package com.taskmanager.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taskmanager.R;
import com.taskmanager.database.entities.Task;

public class TasksArrayAdapter extends ArrayAdapter<Task>{
	private final List<Task> list;
    private final Activity context;
    final int DIALOG_ADD_FRIEND = 1;
	
	public TasksArrayAdapter(Activity context, int textViewResourceId,
			List<Task> list) {
		super(context, textViewResourceId, list);
		this.context = context;
        this.list = list;
	}
	
	public TasksArrayAdapter(Activity context, List<Task> list) {
        super(context, R.layout.tasks, list);
        this.context = context;
        this.list = list;
	}
	
	static class ViewHolder {
		public TextView authorTextView;
		public TextView contentTextView;
		public TextView dataTextView;
		public LinearLayout backraundLayuot;
		public LinearLayout priorityLayuot;
		
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
		int green = Color.parseColor("#99cc00");
		int blue = Color.parseColor("#34b6e4");
		int red = Color.parseColor("#ff4444");
		int white = Color.parseColor("#ffffff");
		int orange = Color.parseColor("#ffbb33");
		int proirityColor = -1;
		ViewHolder holder;
		View rowView = convertView;
		
		if (rowView == null){
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.tasks, null, true);
			holder = new ViewHolder();
	        holder.authorTextView = (TextView) rowView.findViewById(R.id.authorTask);
	        holder.contentTextView = (TextView) rowView.findViewById(R.id.textTask);
	        holder.dataTextView = (TextView) rowView.findViewById(R.id.dataTask);
	        holder.backraundLayuot = (LinearLayout) rowView.findViewById(R.id.background);
	        holder.priorityLayuot = (LinearLayout) rowView.findViewById(R.id.priority);
	       
	        
	        rowView.setTag(holder);
		}
		else{
			holder = (ViewHolder) rowView.getTag();
		}

		holder.authorTextView.setText(list.get(position).getAuthor());
		holder.contentTextView.setText(list.get(position).getContent());
		holder.dataTextView.setText(list.get(position).getTime());
		
	

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
			break;
		case 5:
			proirityColor = orange;
			break;	
		}
		
		holder.priorityLayuot.setBackgroundColor(proirityColor);
		
		if(list.get(position).getComplete().equals("false"))
        	holder.backraundLayuot.setBackgroundColor(white);
		else
			holder.backraundLayuot.setBackgroundColor(proirityColor);
        
        return rowView;
	}


}