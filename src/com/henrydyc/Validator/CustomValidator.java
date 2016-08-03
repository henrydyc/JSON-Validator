package com.henrydyc.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.henrydyc.*;
import com.henrydyc.ValidationError.*;


/**
 * An Custom Valdator class that allows user-defined validation behavior. This class:
 * <pre>
 * 1. Provides an interface for subclasses to define key : value validation rules by 
 *    implementing the constructTruthMappings() and constructResponseMappings() methods
 *    to specify the one-to-one (OO) and one-to-many (OM) validation rules between the JSONs
 * 2. Once the key-value mapping is defined as in (1) by the subclass, this class provides 
 *    a common validation implementation, validate(), that validates the OO and OM mappings
 * 3. For example, truthOO should have the key: value pairs that we construct from the truth 
 *    JSON object, and we wish to compare (validate) with the responseOO's key: value pairs. 
 * </pre>

 * @author Yichao Dong

 */
public abstract class CustomValidator implements Validator {
	
	//For truth data, these should be constructed by constructTruthMappings():
	protected HashMap<String, String> truthOO = null; 
	protected HashMap<String, ArrayList<String>> truthOM = null;
	
	//For response data, these should be constructed by constructResponseMappings()
	protected HashMap<String, String> responseOO = null;
	protected HashMap<String, ArrayList<String>> responseOM = null;
	
	
	/************** Interface Methods ******************/
	protected abstract void constructTruthMappings (JSONObject truthJSON);
	protected abstract void constructResponseMappings (JSONObject responseJSON);
		
	
	/************** Inheritable Methods ***************/	
	/**
	 * Precondition: subclasses must construct the key-value mapping rules by implementing the protected methods
	 * Validates the One-to-one and One-to-many sets with the truth JSON against the response JSON
	 */
	@Override
	public void validate(ValidationTaskResult result, JSONObject truthJSON, JSONObject responseJSON){
		constructTruthMappings (truthJSON); //constructs truthOO and truthOM
		constructResponseMappings (responseJSON); //constructs responseOO and responseOM
		
		//Checks One to one key-value mapping
		for (Map.Entry<String, String> entry : truthOO.entrySet()) {
		    String key = entry.getKey();
		    String tValue = entry.getValue();
		    if(!responseOO.containsKey(key)){
		    	result.addError(new KeyNotFoundInResponseError(key));
		    	continue;
		    }
		    String rValue = responseOO.get(key);
		    if(!tValue.equals(rValue)) {
		    	result.addError(new KeyValueMismatchOOError(key, tValue, rValue));
		    }
		}
		
		for (Map.Entry<String, ArrayList<String>> entry : truthOM.entrySet()) {
		    String key = entry.getKey();
		    ArrayList<String> tValueArr = entry.getValue();
		    if(!responseOO.containsKey(key)){
		    	result.addError(new KeyNotFoundInResponseError(key));
		    	continue;
		    }
		    ArrayList<String> rValueArr = responseOM.get(key);
		    Collections.sort(tValueArr);
		    Collections.sort(rValueArr);
		    if(tValueArr.size() != rValueArr.size()){
		    	result.addError(new KeyValueMismatchOMError(key, tValueArr, rValueArr));
		    	continue;
		    }
		    for(int i=0; i< tValueArr.size(); i++){
		    	if(!tValueArr.get(i).equals(rValueArr.get(i))){
		    		result.addError(new KeyValueMismatchOMError(key, tValueArr, rValueArr));
		    	}
		    }   
		}		
	}
	
}
