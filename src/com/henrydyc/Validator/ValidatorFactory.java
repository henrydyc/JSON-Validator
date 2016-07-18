package com.henrydyc.Validator;

public class ValidatorFactory {
	public static Validator getValidator (String type) {
		Validator ret = null;
		if (type.equalsIgnoreCase ("AnnotationVersion")){
			ret = new CustomValidatorAnnotationVersion();
		} else {
			//type.equals("default") and other cases
			ret = new DefaultValidator();
		}
		
		return ret;
	}
}
