package com.henrydyc.ValidationError;

/**
 * Indicates an error that is not related to the input data, e.g. a bug in the code
 */
public class SystemError implements ValidationError {
	
	String errorType = "System Error";
	String detail;
	
	public SystemError (String detail){
		this.detail = detail;
	}
	
	@Override
	public String getFullErrorMessage() {
		
		return errorType + " : " + detail;
	}

	@Override
	public String errorType() {
		
		return errorType();
	}

}
