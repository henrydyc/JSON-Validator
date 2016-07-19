package com.henrydyc;

import java.io.File;
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
 *		"tasks" : [ {"name" : "taskX", "validator": "MyCustomValidator", "truthFolder" : "taskX/truthJSON", "responseFolder" : "taskX/responseJSON"},  
 *					{"name" : "taskY", "truthFolder" : "taskY/truthJSON", "responseFolder" : "taskY/responseJSON"} ,
 *					...... 
 * 				],
 * 		"workdingDir" : "myWorkingDir",
 *		"logPath" : "myWorkingDir/Result.log"
 * }
 * 
 * where the "tasks" array and "workingDir" is mandatory
 * 
 */

public class ValidationConfiguration {
	
	String workingDir = null;
	ArrayList<ValidationCollection> colls;
	String logPath = null;
	
	public ValidationConfiguration(String configFilePath) throws ParseException, FileNotFoundException, IOException {
		
		    if (!new File(configFilePath).exists()) throw new FileNotFoundException(configFilePath); 
	
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(new FileReader(configFilePath));
			//Optional keys
			if (jsonObj.containsKey("logPath"))
				logPath = (String) jsonObj.get("logPath");

			//Mandatory keys in the config JSON array and working diretory

			workingDir = (String) jsonObj.get("workingDir"); 

			colls = new ArrayList<ValidationCollection> ();
			
			JSONArray tasks = (JSONArray) (jsonObj.get("tasks"));
			for(int i=0; i< tasks.size(); i++) {
				String validatorType = "default";
				
				JSONObject task = (JSONObject) tasks.get(i);
				
				String name = (String) task.get("name");
				String truthFolder = (String) task.get("truthFolder");
				String responseFolder = (String) task.get("responseFolder");
				if (task.containsKey("validator")){
					validatorType = (String) task.get("validator");
				}
				
				
				colls.add( new ValidationCollection (name, truthFolder, responseFolder, validatorType) );
			}			
			
			
			
	}

	public ArrayList<ValidationCollection> getValidationCollections() {
		return colls;
	}
	
	public boolean hasLogPath() {
		return logPath != null;
	}
	
	
	public String getLogPath() {
		return logPath;
	}
	
	public String getWorkingDir() {
		return workingDir;
	}
}
