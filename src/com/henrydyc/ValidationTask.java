package com.henrydyc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.henrydyc.ValidationError.*;


/**
 * <pre>
 * Represents a validation task between two json files : truth JSON and response JSON.
 * The validation is considered successful if all fields in the truth file 
 * is validated (has the same value) in the response file
 * </pre>
 * @author Yichao Dong
 *
 */
public class ValidationTask {
	private String name;
	private String truthFilePath;
	private String responseFilePath;
	private boolean validationCompleted = false;;
	ValidationTaskResult result;
	
	public ValidationTask (String truthFilePath, String responseFilePath) {
		name = Paths.get(truthFilePath).getFileName().toString(); //default name is the base filename 
		this.truthFilePath = truthFilePath;
		this.responseFilePath = responseFilePath;
	}
	
	public ValidationTaskResult getValidationTaskResult() {
		return result;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * validates the two JSONs via field wise validation, and updates the internal validation result object 
	 */
	public void validate() {
		
		//Sanity check if both JSON files exists
		File truthFile = new File(truthFilePath);
		File responseFile = new File(responseFilePath);
		
		if(!truthFile.exists() || !responseFile.exists()) {
			result = new ValidationTaskResult("For file : " + responseFile.getName());
			if (!truthFile.exists()) {
				result.addError(new FileNotFoundError(truthFilePath));
			}
			if (!responseFile.exists()) {
				result.addError(new FileNotFoundError(responseFilePath));
			}
			return;
			
		}
		
		//JSON validation starts here
		try {
			JSONParser parser = new JSONParser();
			JSONObject truthJSON = (JSONObject) parser.parse(new FileReader(truthFilePath));	
			JSONObject responseJSON = (JSONObject) parser.parse(new FileReader(responseFilePath));
			
			if (responseJSON.get("name") != null) { //attempt to set a more meaningful id
				name = (String) responseJSON.get("name");
			}
			result = new ValidationTaskResult(name);

			//For each field in the truthJSON, validate against the equivalent in the responseJSON
			fieldWiseValidate(truthJSON, responseJSON);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		validationCompleted = true;
		
	}
	
	private void fieldWiseValidate(JSONObject truth, JSONObject response){
		for(Iterator<?> iterator = truth.keySet().iterator(); iterator.hasNext();) {
		    String key = (String) iterator.next();
		    //Check if response has the same key
		    if (response.get(key) == null) {
		    	result.addError(new KeyNotFoundInResponseError(key));
		    	continue;
		    }
		    
		    Object tVal =  truth.get(key);
		    Object rVal = response.get(key);
		    if (tVal instanceof JSONObject) {//val is an object
		    	assert (rVal instanceof JSONObject);
		    	fieldWiseValidate((JSONObject)tVal, (JSONObject)rVal);
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
	
	
	/**
	 * @return true if the validation procedure completes and the result is true (all fields in the truth file is validated in the response file)
	 */
	boolean successful () {
		return validationCompleted && result.successful();
	}

	private ArrayList<String> convertJSONArrayToList(JSONArray arr) {
		ArrayList<String> ret = new ArrayList<String>();
		for(int i= 0; i< arr.size(); i++) {
			ret.add((String)arr.get(i));
		}
		Collections.sort(ret);
		return ret;
	}
	
	
}