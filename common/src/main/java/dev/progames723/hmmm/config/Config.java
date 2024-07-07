package dev.progames723.hmmm.config;

import java.io.File;
import java.util.List;

public interface Config {
	/**
	 * @return a list of all config files, if none are found returns an empty list
	 */
	List<File> getConfigFiles();
	
	/**
	 * gets a file, duh
	 * @param path path to file
	 * @return null if file not present, not a config file, or a directory, otherwise returns the file
	 */
	File getConfigFile(String path);
	
	//TODO extend this
	
	void saveConfig(File configFile);
	
	default void saveConfigs(List<File> configFiles) {
		for (File file : configFiles) saveConfig(file);
	}
}
