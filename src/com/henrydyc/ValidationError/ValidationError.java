package com.henrydyc.ValidationError;

/**
 * An error that raises when the two IR objects are not validated, for example, a key-value mismatch
 */
public interface ValidationError {
	String getFullErrorMessage();
	String errorType();
}
