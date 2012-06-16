package com.taskmanager.helpers;

import java.util.ArrayList;

public class CheckFields {
	
	private static ArrayList<String> errors;
	
	public static Boolean checkLogin(String input) {
        return input.matches("^[a-z]([a-z0-9_\\.\\-])*[a-z0-9]$");
    }

	
	public static ArrayList<String> checkBeforeRegistraton(String firstName,
			String lastName, String login, String password) {

        if (login == null || login.equals("")) {
            errors.add("Login is empty");
        } else {
            if (login.length() > 10)                
                errors.add("Login length is more than 10 characters");
            else if (login.length() < 4)                
                errors.add("Login length is less than 4 characters");
            if (!checkLogin(login))
                errors.add("Login consists invalid characters");
        }
        
        if (password.equals("")) {
            errors.add("Password is empty");
        } else {
             if (password.length() < 6)                
                errors.add("Password is too short!");                
        }
        
        return errors;
	}
	
	public static ArrayList<String> checkBeforeLoginisation(String login,
			String password){
		return errors;
	}
	
}
