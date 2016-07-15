package com.henrydyc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/* The configuration JSON file should have the following schema:
 * {
 *		"tasks" : [ {"name" : "taskX", "truthFolder" : "taskX/truthJSON", "responseFolder" : "taskX/responseJSON"},  
 *					{"name" : "taskY", "truthFolder" : "taskY/truthJSON", "responseFolder" : "taskY/responseJSON"} ,
 *					...... 
 * 				],
 * 		"workdingDir" : "myWorkingDir",
 *		"logPath" : "myWorkingDir/Result.log"
 * }
 * 
 * where the "tasks" array is mandatory
 * 
 */

public class ValidationConfiguration {
	
	String workingDir = null;
	ArrayList<ValidationCollection> colls;
	String logPath = null;
	
	public ValidationConfiguration(String configFilePath) throws FileNotFoundException, IOException, ParseException {
	
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(new FileReader(configFilePath));
			colls = new ArrayList<ValidationCollection> ();
			//Mandatory keys in the config JSON
			JSONArray tasks = (JSONArray) (jsonObj.get("tasks"));
			for(int i=0; i< tasks.size(); i++) {
				JSONObject task = (JSONObject) tasks.get(i);
				
				String name = (String) task.get("name");
				String truthFolder = (String) task.get("truthFolder");
				String responseFolder = (String) task.get("responseFolder");
				
				
				colls.add( new ValidationCollection (name, truthFolder, responseFolder) );
			}			
			
			//Optional keys
			if (jsonObj.containsKey("logPath"))
				logPath = (String) jsonObj.get("logPath");
			if (jsonObj.containsKey("workingDir"))
				workingDir = (String) jsonObj.get("workdingDir"); 
	}

	public ArrayList<ValidationCollection> getValidationCollections() {
		return colls;
	}
	
	public boolean hasLogPath() {
		return logPath != null;
	}
	
	public boolean hasWorkdingDir() {
		return workingDir != null;
	}
	
}
