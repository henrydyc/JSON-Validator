package com.henrydyc.Validator;

public class ValidatorFactory {
	public static Validator getValidator (String type) {
		Validator ret = null;
		if (type.equalsIgnoreCase ("SourceVersion")){
			ret = new CustomValidatorSourceVersion();
		} else {
			//type.equals("default") and other cases
			ret = new DefaultValidator();
		}
		
		return ret;
	}
}
