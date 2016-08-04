# JSON-Validator

This is a general-purpose tool for validation across files of the JSON format. In addition to the default validation rule that comes with this program, user can also customize specific rules of the validation by writing simple extension code.

## Installation
First download the source code by cloning the project into some parent directory of your choice:
```sh
$ cd /path/to/parent_dir
$ git clone https://github.com/henrydyc/JSON-Validator.git
```
Before building, make sure you have installed **JDK 7** or above in your system.
Next, you can build the project within an IDE such as _Eclipse_ and _IntelliJ IDEA_ , but you can always do so on the command line:
```sh
# You are sitting in the directory in which you just did "git clone"
$ cd JSON-Validator 
$ mkdir bin
$ javac -cp lib/json-simple-1.1.1.jar:src src/com/henrydyc/ValidationDriver.java -d bin/
```
##Basic Usage

### 1. Setting up config.json
Before using JSON-Validator, you should set up a **config.json** file to specify where your JSON files are and which [validator](#validator) you wish to use. By default, place this configuration file at the root directory of the project, otherwise you can specify its path as a command line argument.
Under the project root directory, there is a sample **config.json** as following:
```
{
    "tasks": [
        {
            "name": "Cars",
            "truthFolder": "./test/truthJSON/cars/",
            "responseFolder": "./test/responseJSON/cars/"
        },
        {
            "name": "Versions",
            "validator": "SourceVersion",
            "truthFolder": "./test/truthJSON/versions/",
            "responseFolder": "./test/responseJSON/versions/"
        }
    ],
    "logPath" : "ValidationResult.txt"
}
```
In this configuration JSON:<a name="configJSON"></a>

 - **"task"** is mandatory, this array stores a list of validation tasks (a collection of JSON files that have the same format and validation rule), in which:
	 -  **name** is any name you wish to give to this task collection
	 - **truthFolder** is the path to your collection of JSON files that represents the _truth_ to compared with
	 - **responseFolder** is the path to your collection of JSON files that represents the *response/test* JSONs that will be compared to the truth JSONs
	 - **validator** is optional. If not specified, the default validator will be used. Otherwise, specify the custom validator class name. See in the Custom Validator session for how to add your [custom validator](#customValidator) specifier to a `Factory` class.
 - **"logPath"** is optional. This is the path to output the validation result file. If not specified, a file named "*Result_yyyy_mm_dd.log*" will be generated in your root directory
 
In order for the program to find the two corresponding files, **please make sure the truth file has the same name as the response file, but place them in different directories**. Failure to do so will be considered a validation error and will be reported to the result file (program will not crash).

### 2. Running the program <a name="runningProg"></a>

The class that contains the main method is **ValidationDriver** in the _com.henrydyc_ package, which you can invoke through the IDE or export an executable.
For command line users, run the program with the following command after building:
```sh
# Suppose you are at the project root directory
$ java -cp lib/json-simple-1.1.1.jar:bin/ com.henrydyc.ValidationDriver [path/to/config.json]
```
By default, you should have a file named config.json sitting under the project root directory. Alternatively, you can specify the path to your configuration json file as the 1st argument of command line.
The validation result file will be saved to the disk at locations describe above.

###<a name="validator"></a> 3. Customizing validation rules 
 *JSON Validator* has two kinds of validators (under package ***com.henrydyc.Validator*** ), the *default* one, and the *custom* ones, and a validator specifies how the JSON files should be compared:

#### Default validator
If not specified in the *config.json* file, the tasks will be using the default validator implemented in the program (**DefaultValidator.java**), which does a field-wise comparison of simple structures like:
```
{
    "key" : "value",
    "listKey" : ["val1", "val2", "val3"]
}
```
####<a name="customValidator"></a> Custom validator 
Users can implement their own validation rules, by implementing a custom validator class in the source code. This becomes necessary if the two JSONs being compared has a complicated structure (deeply nested), or only a subset of the key-value pairs needs to be validated.
To implement your own validator object, please do the following:

 1. Under *src/com/henrydyc/Validator/,* create a class, e.g. `MyCustomValidator` that extends the `CustomValidator` class in the same folder
 2. In the `MyCustomValidator` class, override the two required methods: `constructTruthMappings()` and `constructResponseMappings()`which should process the JSON file and extract your key-value pairs to a hash table data structure. An example class, `CustomValidatorSourceVersion`, is provided in the source code to show how this can be done.
 3. In the `ValidatorFactory` class, give a name to your custom validator, and add this name to an `if` condition to make it return your custom class instance.
 4. In the [validator field](#configJSON)  in *config.json*, use the custom validator name you created in (3) to make the program use this validator for the validation task.
 
## License
This project is licensed under the MIT License

