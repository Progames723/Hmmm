package dev.progames723.hmmm.config.v1;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.misc.IterableHashMap;
import dev.progames723.hmmm.misc.IterableMap;
import dev.progames723.hmmm.utils.XplatUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public abstract class PropertiesConfig implements Config<String, String> {
	private final boolean isSided;
	private final Properties commonValues = new Properties();
	private final Properties serverValues = new Properties();
	private final Properties clientValues = new Properties();
	private final Path configPath;
	private final File commonConfigFile;
	private final File serverConfigFile;
	private final File clientConfigFile;
	
	private boolean isLoaded = false;
	
	protected PropertiesConfig(boolean isSided, String dirName, String configName) {
		this.isSided = isSided;
		if (dirName == null || dirName.isEmpty()) {
			configPath = XplatUtils.getConfigFolder();
		} else {
			configPath = XplatUtils.getConfigFolder().resolve(dirName);
		}
		configPath.toFile().mkdir();
		commonConfigFile = configPath.resolve(configName + ".common.properties").toFile();
		serverConfigFile = configPath.resolve(configName + ".server.properties").toFile();
		clientConfigFile = configPath.resolve(configName + ".client.properties").toFile();
		if (throwExceptionIfMoreThanOneInstance()) doThrowException();
		registerConfigInstance();
		CompletableFuture.runAsync(() -> {
			try {
				loadConfig();
			} catch (IOException e) {
				HmmmLibrary.LOGGER.warn("Failed to load %s config!".formatted(this.getClass()), e);
			}
		});
	}
	
	@Override
	public void loadConfig() throws IOException {
	
	}
	
	@Override
	public void saveConfig() throws IOException {
	
	}
	
	@Override
	public void unloadConfig() throws IOException {
		
	}
	
	@Override
	public boolean isLoaded() {
		return isLoaded;
	}
	
	@Override
	public IterableMap<String, String> getCommonValues() {
		IterableMap<String, String> map = new IterableHashMap<>();
		commonValues.stringPropertyNames().forEach(s -> map.put(s, commonValues.getProperty(s)));
		return map;
	}
	
	@Override
	public IterableMap<String, String> getServerValues() {
		IterableMap<String, String> map = new IterableHashMap<>();
		serverValues.stringPropertyNames().forEach(s -> map.put(s, serverValues.getProperty(s)));
		return map;
	}
	
	@Override
	public IterableMap<String, String> getClientValues() {
		IterableMap<String, String> map = new IterableHashMap<>();
		clientValues.stringPropertyNames().forEach(s -> map.put(s, clientValues.getProperty(s)));
		return map;
	}
}
