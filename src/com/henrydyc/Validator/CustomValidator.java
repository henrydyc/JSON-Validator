package com.henrydyc.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.henrydyc.*;
import com.henrydyc.ValidationError.*;


/**
 * An Custom Valdator class that allows user-defined validation behavior. This class:
 * <pre>
 * 1. Provides an interface for subclasses to define key : value validation rules by 
 *    implementing the constructOneOneMappings() and constructOneManyMappings() methods
 * 2. Once the key value mapping in (1) is defined in the subclass, it provides a 
 *    common validation implementation, validate(), for code reuse 
 * </pre>

 * @author Yichao Dong

 */
public abstract class CustomValidator implements Validator {
	
	//For truth data:
	protected HashMap<String, String> truthOO = null;  //One-to-one key : val mapping
	protected HashMap<String, ArrayList<String>> truthOM = null; //One-to-many key : vals mapping
	
	//For response data:
	protected HashMap<String, String> responseOO = null;
	protected HashMap<String, ArrayList<String>> responseOM = null;
	
	
	/************** Interface Methods ******************/
	protected abstract void constructTruthMappings (JSONObject truthJSON);
	protected abstract void constructResponseMappings (JSONObject responseJSON);
	
	//Implementation must return "newed" (non-null) objects, otherwise will raise an error in validate() below
	protected abstract HashMap<String,String> constructOneOneMappings (JSONObject json);
	protected abstract HashMap<String, ArrayList<String>> constructOneManyMappings (JSONObject json);
		
	
	/************** Inheritable Methods ***************/	
	/**
	 * Precondition: subclasses must construct the key-value mapping rules by implementing the protected methods
	 * Validates the One-to-one and One-to-many sets with the truth JSON against the response JSON
	 */
	@Override
	public void validate(ValidationTaskResult result, JSONObject truthJSON, JSONObject responseJSON){
		constructTruthMappings (truthJSON);
		constructResponseMappings (responseJSON);
		
		//Checks One to one key-value mapping
		for (HashMap.Entry<String, String> entry : truthOO.entrySet()) {
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
		
		for (HashMap.Entry<String, ArrayList<String>> entry : truthOM.entrySet()) {
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

