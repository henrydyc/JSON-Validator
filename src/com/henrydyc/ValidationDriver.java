package com.henrydyc;

import java.util.ArrayList;

/**
 * The driver class that manages the JSON validation on a while 
 * @author Yichao Dong
 *
 */
public class ValidationDriver {
	
	//TODO add command line configuration options
	static String CONFIGFILE_PATH = "config.json";

	ArrayList<ValidationCollection> colls;
	ValidationIO io;
	
	public ValidationDriver (ValidationIO io){
		this.io= io;
		colls = io.getValidationCollections();
	}
	
	public void validateAllTasks() {
		for (ValidationCollection coll : colls) {
			coll.validateTasks();
			io.addValidationCollectionResultToLog(coll);
		}
	}
	
	public void outputResults() {
		io.writeResultsToLog();
	}

	
	public static void main(String [] args) {

		try {		
			//The io object creates a collection of validation tasks according to the config.json file
			if (args.length > 0) {
				CONFIGFILE_PATH = args[0];
			}
			ValidationIO io = new ValidationIO(CONFIGFILE_PATH);
			ValidationDriver driver = new ValidationDriver(io);
			driver.validateAllTasks();
			driver.outputResults();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
