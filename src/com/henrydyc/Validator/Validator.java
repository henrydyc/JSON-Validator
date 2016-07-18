package com.henrydyc.Validator;

import org.json.simple.JSONObject;

import com.henrydyc.ValidationTaskResult;

/**
 * @param result where the validation result is stored, needs to be constructed before passing in
 * @param truthJSON represents the truth JSON object
 * @param responseJSON represents the responseJSON object 
 * @author Yichao Dong
 *
 */
public interface Validator {
	void validate (ValidationTaskResult result, JSONObject truthJSON, JSONObject responseJSON);
}
