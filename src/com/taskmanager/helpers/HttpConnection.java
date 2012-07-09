package com.taskmanager.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpConnection {
	
	private static final String TAG = HttpConnection.class.getSimpleName();
	private static String BASE_URL = "http://task-manager-project.heroku.com";

	public static String makeRequest(String url,HashMap<String, Object> params){
		
		HttpPost request = new HttpPost(BASE_URL+url);
		JSONObject holder = new JSONObject();
		JSONObject client = new JSONObject();
		
		try{	
				Iterator it = params.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        client.put(pairs.getKey().toString(), pairs.getValue());
			        it.remove(); // avoids a ConcurrentModificationException
			    }
	
				holder.put("taskmanager", client);
				StringEntity se = new StringEntity(holder.toString());
				request.setEntity(se);
				request.setHeader("Accept", "taskmanager/json");
				request.setHeader("Content-Type", "taskmanager/json");
				Log.i("http_connection", holder.toString());
				
			}catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ResponseHandler<String> rhandler = new BasicResponseHandler();
			HttpClient httpClient = new DefaultHttpClient();
			
			String jsonResponse = null;
			
			try {
				jsonResponse = httpClient.execute(request, rhandler);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return jsonResponse;
	}
	
	public static HashMap<String, Object> parse (String json, String  basic, String ... keys) {
		HashMap<String, Object> results = new HashMap<String, Object>();
		if(json!=null){
			try {
				JSONObject main = new JSONObject(json).getJSONObject(basic);
				String error = main.getString("error");
				results.put("error", error);
				if(error.equals("Success")){
					for (int i = 0; i < keys.length; i++) {
						if(main.has(keys[i]))
						 results.put(keys[i], main.get(keys[i]));
					}					
				}
			} catch (JSONException e) {
				Log.e(TAG, "Json exception");
				e.printStackTrace();
			}
			
		}else{
			results.put("error", "Server error");
			Log.e(TAG, "Json is null ");
		}		
		return results;
	}
}
