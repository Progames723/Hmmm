package dev.progames723.hmmm.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class BaseConfig implements Config {//barebones config impl
	@Override
	public File getConfigFile(String file) {
		if (getCurrentConfigFile().getPath().equals(file)) return getCurrentConfigFile();
		file = file.replace('\\', '/');
		String[] strings = file.split("/");
		if (!strings[strings.length-1].endsWith(".config.json")) {return null;}
		File newFile = new File(file);
		return newFile.isDirectory() ? null : newFile;
	}
	
	public File getConfigFile(String stringPath, String file) {
		return getConfigFile((stringPath.charAt(stringPath.length()-1) == '/' ? stringPath + file : stringPath + "/" + file).replace('\\', '/'));
	}
	
	@Override
	public abstract File getCurrentConfigFile();
	
	//TODO finish implementing everything else
	
	@Override
	public void saveConfig(File configFile) {
		if (configFile.isDirectory()) return;
		if (!configFile.canWrite()) return;
		FileWriter writer = null;
		try {
			writer = new FileWriter(configFile);
		} catch (IOException ignored) {}
		assert writer != null;
		
	}
}
