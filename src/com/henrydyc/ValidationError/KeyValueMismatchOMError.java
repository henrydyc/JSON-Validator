package com.henrydyc.ValidationError;
import java.util.List;

/**
 * Indicates that there is a mismatch of the value for the given key (One-to-many KV mapping) in the two (JSON) objects
 */
public class KeyValueMismatchOMError implements ValidationError {

	String errorType = "Key-Value Mismatch";
	String key;
	List<String> truthVal;
	List<String> responseVal;
	
	public KeyValueMismatchOMError (String key, List<String> truthVal, List<String> responseVal) {
		this.truthVal = truthVal;
		this.responseVal = responseVal;
		this.key = key;
	}
	
	@Override
	public String getFullErrorMessage() {
		String message = errorType + " : field \""+ key+ "\": truth has value(s) " +
				truthVal.toString()+ ", but response has has value(s) " + responseVal.toString();
		return message;
	}

	@Override
	public String errorType() {
		return errorType;
	} 

}
