package com.henrydyc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * This class manages a list of Validation tasks (each is a JSON validation)
 * @author Yichao Dong
 */
public class ValidationCollection {
	private String name; //name of the collection of JSONs
	private String truthDirPath;
	private String responseDirPath;
	private ArrayList<ValidationTask> tasks;
	
	public ValidationCollection (String name, String truthDirPath, String responseDirPath) throws FileNotFoundException {
		this.name = name;
		this.truthDirPath = truthDirPath;
		this.responseDirPath = responseDirPath;
		
		File f1 = new File(truthDirPath);
		if (!(f1.exists() && f1.isDirectory())) throw new FileNotFoundException(truthDirPath);
		File f2 = new File (responseDirPath);
		if (!(f2.exists() && f2.isDirectory())) throw new FileNotFoundException(responseDirPath);
		
		tasks = new ArrayList<ValidationTask>();
	}
	
	public void validateTasks() {
		final File folder = new File(responseDirPath);
		for (final File fileEntry : folder.listFiles()) {
			String fname = fileEntry.getName();
			String truthFilePath = truthDirPath + fname;
			String responseFilePath = responseDirPath + fname;
			
			ValidationTask task = new ValidationTask(truthFilePath, responseFilePath);
			task.validate();
			
			tasks.add(task);
			
			System.out.println(fname + " processed.");
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<ValidationTask> getTasks () {
		return tasks;
	}

	public int successTaskCounts() {
		int count = 0;
		for (ValidationTask task : tasks){
			if (task.successful())
				count++;
		}
		return count;
	}

	public int totalTaskCounts() {
		
		return tasks.size();
	}

}
