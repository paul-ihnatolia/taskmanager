package com.taskmanager.helpers;

import android.app.Activity;
import android.widget.EditText;

import com.taskmanager.R;


public class CheckFields {
	
	private static String regexLogin = "^[a-z]([a-z0-9_\\.\\-])*[a-z0-9]$";
	
	private static String regexName = "^([a-zA-Zа-яА-ЯіІїЇєЄґҐ0-9_\\.\\-\\ ])*$";
	

	public static boolean checkBeforeRegistraton(String firstName,
			String lastName, String login, String password, Activity activity) {
		
		boolean chi = true;
		String error;
		if((error = checkName("First", firstName))!=null){
			((EditText)activity.findViewById(R.id.firstName)).setError(error);
			chi = false;
		}
		
		if((error = checkName("Last", lastName))!=null){
			((EditText)activity.findViewById(R.id.lastName)).setError(error);
			chi = false;
		}

		if((error = checkLoginAndPassword(password, 6, 15, "Password"))!=null){
			((EditText)activity.findViewById(R.id.password)).setError(error);
			chi = false;
		}
		
		if((error = checkLoginAndPassword(login, 4, 10, "Login"))!=null){
			((EditText)activity.findViewById(R.id.login)).setError(error);
			chi = false;
		}

        	return chi;
	}

/*	public static ArrayList<String> checkBeforeLoginisation(String login,
				String password){
		
		errors = new ArrayList<String>();
		
		errors.add(checkLoginAndPassword(password, 6, 15, "Password"));
		errors.add(checkLoginAndPassword(login, 4, 10, "Login"));    		
		errors.removeAll(Arrays.asList(new Object[]{null}));  //??
        return errors;
	
	}
*/	
	private static String checkLoginAndPassword(String password, int min, int max, String type) {
        
		if (password.equals("")) {
            return type + " is empty";
        } else {
             if (password.length() < min)
            	 return type + " is too short!";
             else if (password.length() > max) 
            	 return type + " length is more than 15 characters";
     /*        if(!password.matches(regexLogin))
            	 return type + " consists invalid characters";*/
        }
		
        return null;
	}
	
	private static String checkName(String type, String input) {

		if ( input.equals("")){
			return type + "name is empty";
		}
		else {
			if(!input.matches(regexName))
				return type + "name consists invalid characters";
		}

		return null;
	}


}