package com.henrydyc.Validator;

import com.henrydyc.ValidationTaskResult;

/**
 * @param result where the validation result is stored, needs to be constructed before passing in
 * @param truthJSONObj the truth JSON object or JSONArray
 * @param responseJSONObj the response JSON object or JSONArray
 * @author Yichao Dong
 *
 */
public interface Validator {
	void validate (ValidationTaskResult result, Object truthJSONObj, Object responseJSONObj);
}
