package com.henrydyc.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.henrydyc.ValidationTaskResult;
import com.henrydyc.ValidationError.KeyNotFoundInResponseError;
import com.henrydyc.ValidationError.KeyValueMismatchOMError;
import com.henrydyc.ValidationError.KeyValueMismatchOOError;

/**<pre>
 * This class handles default JSON validation formats, such as:
 * {"name": "value"}
 * "someArr" : ["val1", "val2"]
 * </pre>
 * @author Yichao Dong
 *
 */
public class DefaultValidator implements Validator {
	
	@Override
	public void validate(ValidationTaskResult result, JSONObject truthJSON, JSONObject responseJSON) {
		assert (result != null);
		
		//attempt to set a more meaningful id
		if (responseJSON.get("name") != null) { 
			result.setName((String) responseJSON.get("name"));
		}
		
		
		for(Iterator<?> iterator = truthJSON.keySet().iterator(); iterator.hasNext();) {
		    String key = (String) iterator.next();
		    //Check if response has the same key
		    if (responseJSON.get(key) == null) {
		    	result.addError(new KeyNotFoundInResponseError(key));
		    	continue;
		    }
		    
		    Object tVal =  truthJSON.get(key);
		    Object rVal = responseJSON.get(key);
		    if (tVal instanceof JSONObject) {//val is an object
		    	assert (rVal instanceof JSONObject);
		    	validate(result, (JSONObject)tVal, (JSONObject)rVal);
		    } else if (tVal instanceof JSONArray) {//val is an Array
		    	assert (rVal instanceof JSONArray);
		    	JSONArray tValArr = (JSONArray) tVal;
		    	JSONArray rValArr = (JSONArray) rVal;
		    	if (tValArr.isEmpty()) continue;
		    	assert(tValArr.get(0) instanceof String);
		    	ArrayList<String> tValList = convertJSONArrayToList(tValArr);
		    	ArrayList<String> rValList = convertJSONArrayToList(rValArr);

		    	if (!tValList.equals(rValList)){
		    		result.addError(new KeyValueMismatchOMError(key, tValList, rValList));
		    	}
		    	
		    } else { //val is a String or number etc.
		    	if (!tVal.equals(rVal)) {
		    		result.addError(new KeyValueMismatchOOError(key, tVal.toString(), rVal.toString()));
		    	}
		    }
		    
		}
	}
	
	private ArrayList<String> convertJSONArrayToList(JSONArray arr) {
		ArrayList<String> ret = new ArrayList<String>();
		for(int i= 0; i< arr.size(); i++) {
			ret.add(arr.get(i).toString());
		}
		Collections.sort(ret);
		return ret;
	}

	
	

}
