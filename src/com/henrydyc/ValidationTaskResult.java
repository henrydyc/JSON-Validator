package com.henrydyc;

import java.util.*;

import com.henrydyc.ValidationError.ValidationError;

enum ValidationResultState {
    SUCCESS, FAILURE, OTHERS 
}

/**
 * Represents the outcome of a validation task
 * @author Yichao Dong
 */
public class ValidationTaskResult {
	private String name = "no name"; //e.g. name of json file, name of the json object etc.
	private ArrayList<ValidationError> errors = new ArrayList <ValidationError>(); //A list of possible errors
	private ValidationResultState status = ValidationResultState.SUCCESS; //default
	public ValidationTaskResult(String name){
		this.name = name;
	}
	
	
	/**Adds an error message for this validation entry and automatically sets this validation result to FAILURE
	 * @param error the error message to add 
	 */
	public void addError(ValidationError error){
		//In the future, the status can be determined specifically by the nature of the error
		status = ValidationResultState.FAILURE;
		errors.add(error);
	}
	
	public String getName() {
		return name;
	}

	public boolean successful (){
		return status == ValidationResultState.SUCCESS;
	}
	
	public List<ValidationError> getErrors() {
		//Don't want the client to modify this list
		return errors;
	}
}