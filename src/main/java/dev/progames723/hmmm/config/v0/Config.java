package dev.progames723.hmmm.config.v0;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.misc.IterableHashMap;
import dev.progames723.hmmm.misc.IterableMap;
import dev.progames723.hmmm.utils.XplatUtils;
import org.jetbrains.annotations.ApiStatus;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class Config {
	protected final String fileExt;
	protected final String configName;
	protected final boolean createSided;
	protected final Path configPath;
	
	private volatile boolean loaded = false;
	private volatile File commonConfig = null;
	private volatile File clientConfig = null;
	private volatile File serverConfig = null;
	
	@ApiStatus.Internal
	private static final List<Config> instances = new ArrayList<>();
	
	protected Config(String configName, String dirName, boolean createSided, String fileExt) throws IllegalStateException {
		this.fileExt = fileExt;
		this.configName = configName;
		this.createSided = createSided;
		if (!dirName.isEmpty()) {
			configPath = XplatUtils.getConfigFolder().resolve(dirName);
		} else {
			configPath = XplatUtils.getConfigFolder();
		}
		configPath.toFile().mkdir();
		IllegalStateException exception;
		if ((exception = onlyOneInstanceAllowed()) != null)
			throw new HmmmException("Some class tried to get a second instance of a singleton config!", exception);
		instances.add(this);
		//probably fix crashing on startup
		CompletableFuture.delayedExecutor(1, TimeUnit.MILLISECONDS).execute(() -> {
			try {
				loadConfig();
			} catch (IOException e) {
				HmmmLibrary.LOGGER.warn("Failed to load config {}", this.getClass().getSimpleName());
			}
		});
	}
	
	@ApiStatus.Internal
	@CallerSensitive
	public static List<Config> getConfigInstances() {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		return instances;
	}
	
	public final boolean isLoaded() {
		return loaded;
	}
	
	protected final void setLoaded0(boolean loaded) {
		this.loaded = loaded;
	}
	
	protected final File getCommonConfig0() {
		return commonConfig;
	}
	
	protected final File getClientConfig0() {
		return clientConfig;
	}
	
	protected final File getServerConfig0() {
		return serverConfig;
	}
	
	protected final void setCommonConfig0(File commonConfig) {
		this.commonConfig = commonConfig;
	}
	
	protected final void setClientConfig0(File clientConfig) {
		this.clientConfig = clientConfig;
	}
	
	protected final void setServerConfig0(File serverConfig) {
		this.serverConfig = serverConfig;
	}
	
	protected enum FileType {COMMON, CLIENT, SERVER}
	
	protected abstract Map<String, String> createDefaultConfigValues(FileType type);
	
	abstract void internalInitValues(FileType type);
	
	protected File createCommonFile() throws IOException {
		File file = configPath.resolve(configName + ".common." + fileExt).toFile();
		if (!file.exists()) {
			file.createNewFile();
			internalInitValues(FileType.COMMON);
		}
		return file;
	}
	
	protected File createServerFile() throws IOException {
		File file = configPath.resolve(configName + ".server." + fileExt).toFile();
		if (!file.exists()) {
			file.createNewFile();
			internalInitValues(FileType.SERVER);
		}
		return file;
	}
	
	protected abstract void writeComments(Writer writer, FileType type) throws IOException;
	
	protected File createClientFile() throws IOException {
		File file = configPath.resolve(configName + ".client." + fileExt).toFile();
		if (!file.exists()) {
			file.createNewFile();
			internalInitValues(FileType.CLIENT);
		}
		return file;
	}
	
	/**
	 * internal value getter impl
	 * @param key the key
	 * @param defaultValue default value
	 * @return a config value
	 */
	protected abstract String getConfigValue0(String key, String defaultValue);
	
	/**
	 * it is what it is
	 * @return exception if implementation only allows one config instance at a time
	 */
	protected IllegalStateException onlyOneInstanceAllowed() {return null;}
	
	public abstract void loadConfig() throws IOException;
	
	public void reloadConfig() throws IOException {
		unloadConfig();
		loadConfig();
	}
	
	public abstract void unloadConfig();
	
	public abstract void saveConfig() throws IOException;
	
	protected void createFilesIfNeeded() throws IOException {
		if (getCommonConfig0() == null)
			setCommonConfig0(createCommonFile());
		if (!createSided) return;
		if (getServerConfig0() == null)
			setServerConfig0(createServerFile());
		if (getClientConfig0() == null)
			setClientConfig0(createClientFile());
	}
	
	public String getConfigValue(String key, String defaultValue) {
		try {
			return getConfigValue0(key, defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public IterableMap<String, String> getAllValues() {
		IterableMap<String, String> map = new IterableHashMap<>(getAllCommonValues());
		map.putAll(getAllClientValues());
		map.putAll(getAllServerValues());
		return map;
	}
	
	public abstract IterableMap<String, String> getAllCommonValues();
	
	public abstract IterableMap<String, String> getAllClientValues();
	
	public abstract IterableMap<String, String> getAllServerValues();
	
	public boolean getBoolean(String key, boolean defaultValue) {
		try {
			return Boolean.parseBoolean(getConfigValue0(key, Boolean.toString(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public long getLong(String key, long defaultValue) {
		try {
			return Long.parseLong(getConfigValue0(key, Long.toString(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public int getInt(String key, int defaultValue) {
		return (int) getLong(key, defaultValue);
	}
	
	public double getDouble(String key, double defaultValue) {
		try {
			return Double.parseDouble(getConfigValue0(key, Double.toString(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public float getFloat(String key, float defaultValue) {
		return (float) getDouble(key, defaultValue);
	}
	
	public <E extends Enum<E>> E getEnum(String key, Class<E> enumClass, E defaultValue) {
		try {
			return Enum.valueOf(enumClass, getConfigValue0(key, defaultValue != null ? defaultValue.name() : null));
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
