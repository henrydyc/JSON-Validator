package com.henrydyc.Validator;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/*This class is a CustumValidor class that allows user to customize the 
 * key-value pairs to be compared against.
 * Specifically, this class deals with JSON with the following format: {
 * "name" : "val", 
 * "sources" : [
 * 		{"name": x, "version": "1"} ,
 * 		{"name": y, "version": "2"}
 * 		...
 * 		]
 * }
 * The task is to validate if "name"/"version" pair has correct values
 */
public class CustomValidatorSourceVersion extends CustomValidator {
	

	@Override
	protected void constructTruthMappings(JSONObject truthJSON) {
		truthOO = constructOneOneMappings(truthJSON);
		truthOM = constructOneManyMappings(truthJSON);
	}

	@Override
	protected void constructResponseMappings(JSONObject responseJSON) {
		responseOO = constructOneOneMappings(responseJSON);
		responseOM = constructOneManyMappings(responseJSON);		
	}

	private HashMap<String, String> constructOneOneMappings(JSONObject json) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		JSONArray arr = (JSONArray) json.get("sources");
		for (int i=0; i < arr.size(); i++) {
			JSONObject annotations = (JSONObject) arr.get(i);
			map.put((String) annotations.get("name"), (String) annotations.get("version"));
		}	
		
		return map;
	}

	private HashMap<String, ArrayList<String>> constructOneManyMappings(JSONObject json) {
		return new HashMap<String, ArrayList<String>>();
	}

}