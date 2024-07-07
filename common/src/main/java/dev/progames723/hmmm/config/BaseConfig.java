package dev.progames723.hmmm.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class BaseConfig implements Config {//barebones config impl
	@Override
	public File getConfigFile(String stringPath) {
		stringPath = stringPath.replace('\\', '/');
		String[] strings = stringPath.split("/");
		if (!strings[strings.length-1].endsWith(".config.json")) {return null;}
		File file = new File(stringPath);
		return file.isDirectory() ? null : file;
	}
	
	public File getConfigFile(String stringPath, String file) {
		return getConfigFile((stringPath + "/" + file).replace('\\', '/'));
	}
	
	//TODO finish implementing everything else
	
	@Override
	public void saveConfig(File configFile) {
		if (configFile.isDirectory()) return;
		FileWriter writer = null;
		try {
			writer = new FileWriter(configFile);
		} catch (IOException ignored) {}
		assert writer != null;
		
	}
}
