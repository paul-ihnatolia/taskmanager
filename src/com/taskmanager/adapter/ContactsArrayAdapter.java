package com.taskmanager.adapter;

import java.util.List;

import com.taskmanager.R;
import com.taskmanager.database.entities.User;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactsArrayAdapter extends ArrayAdapter<User>{
	private final List<User> list;
    private final Activity context;
    
	public ContactsArrayAdapter(Activity context, int textViewResourceId,
			List<User> list) {
		super(context, textViewResourceId, list);
		this.context = context;
        this.list = list;
	}
	public ContactsArrayAdapter(Activity context, List<User> list) {
        super(context, R.layout.contactlist, list);
        this.context = context;
        this.list = list;
	}
	
	static class ViewHolder {
		public TextView nameTextView;
		public TextView loginTextView;
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View rowView = convertView;
		
		if (rowView == null){
			
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.contactlist, null, true);
			
			holder = new ViewHolder();
	        holder.nameTextView = (TextView) rowView.findViewById(R.id.nameContact);
	        holder.loginTextView = (TextView) rowView.findViewById(R.id.loginContact);
	        
	        holder.nameTextView.setTextColor(Color.BLACK);
	        holder.loginTextView.setTextColor(Color.BLACK);
	        
	        holder.nameTextView.setText(list.get(position).getFirstname() + " " + list.get(position).getLastname());
	        holder.loginTextView.setText(list.get(position).getLogin());
	        
	        rowView.setTag(holder);
		}
		else{
			holder = (ViewHolder) rowView.getTag();
		}
		return rowView;
	}
}
