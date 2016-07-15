package com.henrydyc.ValidationError;

/**
 *	Indicates that the specified (JSON) file is not found in the designated location, it is not a system error
 */
public class FileNotFoundError implements ValidationError{
	String errorType = "File Not Found";
	String fname;
	
	public FileNotFoundError(String fname) {
		this.fname = fname; 
	}
	
	@Override
	public String getFullErrorMessage() {
		return errorType + " : " + fname;
	}

	@Override
	public String errorType() {
		return errorType;
	}

}
