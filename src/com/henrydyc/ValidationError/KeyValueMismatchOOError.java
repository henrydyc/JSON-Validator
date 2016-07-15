package com.henrydyc.ValidationError;

/**
 * Indicates that there is a mismatch of the value for the given key (One-to-one KV mapping) in the two (JSON) objects 
 */
public class KeyValueMismatchOOError implements ValidationError {
	String errorType = "Key-Value Mismatch";
	String key;
	String truthVal;
	String responseVal;
	
	public KeyValueMismatchOOError (String key, String truthVal, String responseVal) {
		this.truthVal = truthVal;
		this.responseVal = responseVal;
		this.key = key;
	}
	
	@Override
	public String getFullErrorMessage() {
		String message = errorType + " : field \""+ key+ "\": truth has value \""
    			+ truthVal + "\", but response has value  \""+ responseVal + "\"";
		return message;
	}

	@Override
	public String errorType() {
		return errorType;
	} 
	
}
