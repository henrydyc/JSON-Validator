package com.henrydyc;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.henrydyc.ValidationError.*;

/**
 * Handles cross-platform Input/Output of the JSON validation, including processing a JSON-format config file
 * and writing a log file to the disk reporting errors and the validation results
 * @author Yichao Dong
 */
public class ValidationIO {
	//For cross-platform
	private String nl = System.getProperty("line.separator");

	final static String DEFAULT_CONFIGFILE_PATH = "config.json";

	private String workingDir;
	private String logPath; 

	ArrayList<ValidationCollection> colls;
	
	
		
	ValidationConfiguration config; 
	private String log = ""; //A log message that will be written to the log file in disk
	
	public ValidationIO () {
		buildDataFromConfigurationFile(DEFAULT_CONFIGFILE_PATH);
		
	}
		
	public ValidationIO (String configFilePath) {
		buildDataFromConfigurationFile(configFilePath);
	}

	public ArrayList<ValidationCollection> getValidationCollections() {
		// TODO Auto-generated method stub
		return colls;
	}

	/**Extracts the validation results for a collection of tasks and adds the formatted information to the log file
	 * @param coll contains the to-be-extracted validation results for the task collection
	 */
	public void addValidationCollectionResultToLog (ValidationCollection coll) {
		
			log += titleLine(coll.getName()) + nl + nl;
			//Only prints incorrect entries
			for (ValidationTask task : coll.getTasks()) {
				if (task.successful()) continue; //Ignores successful validations
				
				ValidationTaskResult result = task.getValidationTaskResult();
				
				log += task.getName() + " : " + nl + nl;
				for (ValidationError err : result.getErrors()) {
						log += "  " + err.getFullErrorMessage() + nl;
				}
				log += nl + nl;
			}
			
			log += "Summary : " + nl + "\t " + coll.successTaskCounts() + " out of " 
					+ coll.totalTaskCounts() + " entries validated successfully." + nl + nl + nl + nl; 
	}

	public void writeResultsToLog() {
		writeMessageToLog(log);
	}
	
	public void writeMessageToLog(String messageToPrint){
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logPath)));
			out.println(messageToPrint);
			out.close();
			System.out.println("Message is written to : "+ logPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Raises a system error, prints error to console as well as writes the log, and terminates the program
	 * @param error An program error that requires the developer attention, e.g. a bug
	 */
	public void raiseSystemError(ValidationError error){
		String messageToPrint = error.getFullErrorMessage() + nl + nl + "Program terminated." + nl;
		System.out.println(messageToPrint);
		writeMessageToLog(messageToPrint);
		System.exit(0);
	}
	
	
	/************ Private Helper Methods **************/
	
	private void buildDataFromConfigurationFile(String configFilePath) {

		try {
			config = new ValidationConfiguration(configFilePath);
		} catch (FileNotFoundException e) {
			raiseSystemError(new SystemError("Config File at Path: " + configFilePath + " is not found"));
			e.printStackTrace();
		} catch (IOException | ParseException e) {
			raiseSystemError (new SystemError("An exception has occurred when parsing the config file: " + e.getMessage()) );
			e.printStackTrace();
		} 		
		
		colls = config.getValidationCollections();
				
		workingDir = config.getWorkingDir();
		
		if (config.hasLogPath()){
			logPath = config.getLogPath();
		} else {
			logPath = getOutputFilePathByDate();
		}
		
	}
	
	
	private String getOutputFilePathByDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		return workingDir + "Result_"+ sdf.format(date) + ".log";
	}
	
	private String titleLine(String title) {
		return "===================== " + title + " =====================";
	}
	
	
	
	
	/************ Static Utility Methods **************/
	
	public static String getJSONFilename (String json, String identifier) {
		return replaceBackslashAndSpacesWithDash(getIdentifierFromJSON(json, identifier));
	}
	
	/**
	 * @param json the json string
	 * @param identifier key value of the identifier, typically "name", or "id" etc.
	 * @return the identifier value, or null if the required identifier key is not in the json
	 */
	public static String getIdentifierFromJSON (String json, String identifier) {
		String ret = "";
		try { 
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(json);
			if (jsonObj.get(identifier) == null) {
				ret = null;
			} else {
				ret = (String) jsonObj.get(identifier);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static String replaceBackslashAndSpacesWithDash(String s){
		return s.replaceAll("\\s+","-").replace("\\", "-");
	}
	
	
}
