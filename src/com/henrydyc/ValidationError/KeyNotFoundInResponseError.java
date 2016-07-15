package com.henrydyc.ValidationError;


/**
 * Indicates that a key field in the truth object is missing in the response object
 */
public class KeyNotFoundInResponseError implements ValidationError {
	String errorType = "Key Field Missing";
	String key;
	
	public KeyNotFoundInResponseError (String key) {
		this.key = key;
	}
	
	@Override
	public String getFullErrorMessage() {
		String message = errorType + " : field \"" + key+ "\" is missing from the response JSON";
		return message;
	}

	@Override
	public String errorType() {
		return errorType;
	}

}
