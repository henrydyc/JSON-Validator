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
	protected void constructTruthMappings(Object truthJSONObj) {
		truthOO = constructOneOneMappings((JSONObject)truthJSONObj);
		truthOM = constructOneManyMappings((JSONObject)truthJSONObj);
	}

	@Override
	protected void constructResponseMappings(Object responseJSONObj) {
		responseOO = constructOneOneMappings((JSONObject)responseJSONObj);
		responseOM = constructOneManyMappings((JSONObject)responseJSONObj);		
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