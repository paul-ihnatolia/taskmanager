package com.taskmanager.helpers;

import java.util.ArrayList;
import java.util.Arrays;


public class CheckFields {

	private static ArrayList<String> errors;
	
	private static String regexLogin = "^[a-z]([a-z0-9_\\.\\-])*[a-z0-9]$";
	
	private static String regexName = "^([a-zA-Zа-яА-ЯіІїЇєЄґҐ0-9_\\.\\-\\ ])*$";
	

	public static ArrayList<String> checkBeforeRegistraton(String firstName,
				String lastName, String login, String password) {
		
		errors = new ArrayList<String>();
		errors.add(checkName("First", firstName));	
		errors.add(checkName("Last", lastName));	
		errors.add(checkLoginAndPassword(password, 6, 15, "Password"));
		errors.add(checkLoginAndPassword(login, 4, 10, "Login"));	
		errors.removeAll(Arrays.asList(new Object[]{null}));  
        	return errors;
	}

	public static ArrayList<String> checkBeforeLoginisation(String login,
				String password){
		
		errors = new ArrayList<String>();
		
		errors.add(checkLoginAndPassword(password, 6, 15, "Password"));
		errors.add(checkLoginAndPassword(login, 4, 10, "Login"));    		
		errors.removeAll(Arrays.asList(new Object[]{null}));  //??
        return errors;
	
	}
	
	private static String checkLoginAndPassword(String password, int min, int max, String type) {
        if (password.equals("")) {
            return type + " is empty";
        } else {
             if (password.length() < min)
            	 return type + " is too short!";
             else if (password.length() > max) 
            	 return type + " length is more than 15 characters";
             if(!password.matches(regexLogin))
            	 return type + " consists invalid characters";
        }       
        return null;
	}
	
	private static String checkName(String type, String input) {

		if ( input.equals("")){
			return type + "Name is empty";
		}
		else {
			if(!input.matches(regexName))
				return type + "Name consists invalid characters";
		}

		return null;
	}


}