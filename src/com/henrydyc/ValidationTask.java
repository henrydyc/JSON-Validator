package com.henrydyc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.henrydyc.ValidationError.*;
import com.henrydyc.Validator.Validator;
import com.henrydyc.Validator.ValidatorFactory;


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
	private JSONObject truthJSON;
	private JSONObject responseJSON;
	Validator validator;
	ValidationTaskResult result;
	
	public ValidationTask (String truthFilePath, String responseFilePath) {
		initValidationTask(truthFilePath, responseFilePath, "default");
	}
	
	
	public ValidationTask (String truthFilePath, String responseFilePath, String validatorType) {
		initValidationTask(truthFilePath, responseFilePath, validatorType);
	}
	
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

		try {
			JSONParser parser = new JSONParser();
			truthJSON = (JSONObject) parser.parse(new FileReader(truthFilePath));	
			responseJSON = (JSONObject) parser.parse(new FileReader(responseFilePath));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		validator.validate(result, truthJSON, responseJSON);
	}
	
	public ValidationTaskResult getValidationTaskResult() {
		return result;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	
	/**
	 * @return true if the validation procedure completes and the result is true (all fields in the truth file is validated in the response file)
	 */
	boolean successful () {
		return result.successful();
	}
	
	private void initValidationTask(String truthFilePath, String responseFilePath, String validatorType){
		this.truthFilePath = truthFilePath;
		this.responseFilePath = responseFilePath;
		name = Paths.get(truthFilePath).getFileName().toString(); //default name is the base filename 
		result = new ValidationTaskResult(name);
		validator = ValidatorFactory.getValidator(validatorType);

		
		
	}

	
	
}