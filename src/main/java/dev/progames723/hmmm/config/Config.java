package dev.progames723.hmmm.config;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.XplatUtils;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * barebones config impl
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class Config {
	private final String configName;
	private final Path configPath;
	private final boolean createSided;
	private final Properties commonProperties = new Properties();
	private final Properties clientProperties = new Properties();
	private final Properties serverProperties = new Properties();
	
	private boolean loaded = false;
	private File commonConfig = null;
	private File clientConfig = null;
	private File serverConfig = null;
	
	@ApiStatus.Internal
	private static final List<Config> instances = new ArrayList<>();
	
	public Config(String configName) {
		this(configName,  "", false);
	}
	
	public Config(String configName, String dirName, boolean createSided) throws IllegalStateException {
		this.configName = configName;
		this.createSided = createSided;
		if (!dirName.isEmpty()) {
			configPath = XplatUtils.getConfigFolder().resolve(dirName);
		} else {
			configPath = XplatUtils.getConfigFolder();
		}
		configPath.toFile().mkdir();
		IllegalStateException throwable = onlyOneInstanceAllowed();
		if (throwable != null) throw throwable;
		instances.add(this);
		try {
			loadConfig();
		} catch (Exception e) {
			HmmmLibrary.LOGGER.warn("Failed to load config!", e);
		}
	}
	
	@ApiStatus.Internal
	@CallerSensitive
	public static List<Config> getInstances() {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		return instances;
	}
	
	/**
	 * it is what it is
	 * @return exception if implementation only allows one config instance at a time
	 */
	public abstract IllegalStateException onlyOneInstanceAllowed();
	
	public void loadConfig() throws IOException {
		if (loaded) {
			return;
		}
		load0();
		loaded = true;
	}
	
	private void load0() throws IOException {
		String yes = !loaded ? "re" : "";
		createFilesIfNeeded();
		try (FileReader fileReader = new FileReader(commonConfig)) {
			commonProperties.load(fileReader);
		} catch (IOException e) {
			HmmmLibrary.LOGGER.warn("Failed to {}load common config for {}!", yes, this.getClass());
		}
		if (!createSided) return;
		try (FileReader fileReader = new FileReader(clientConfig)) {
			clientProperties.load(fileReader);
		} catch (IOException e) {
			HmmmLibrary.LOGGER.warn("Failed to {}load client config for {}!", yes, this.getClass());
		}
		try (FileReader fileReader = new FileReader(serverConfig)) {
			serverProperties.load(fileReader);
		} catch (IOException e) {
			HmmmLibrary.LOGGER.warn("Failed to {}load server config for {}!", yes, this.getClass());
		}
	}
	
	public void unloadConfig() {
		if (!loaded) return;
		commonProperties.forEach(commonProperties::remove);
		clientProperties.forEach(clientProperties::remove);
		serverProperties.forEach(serverProperties::remove);
		loaded = false;
	}
	
	public void saveConfig(String comment) throws IOException {
		saveConfig(comment, comment, comment);
	}
	
	public void saveConfig(String commonComment, String serverComment, String clientComment) throws IOException {
		if (commonConfig.delete()) commonConfig.createNewFile();
		commonProperties.store(new FileWriter(commonConfig), commonComment);
		
		if (!createSided) return;
		
		if (serverConfig.delete()) serverConfig.createNewFile();
		if (clientConfig.delete()) clientConfig.createNewFile();
		serverProperties.store(new FileWriter(serverConfig), serverComment);
		clientProperties.store(new FileWriter(clientConfig), clientComment);
	}
	
	public void createFilesIfNeeded() throws IOException {
		if (commonConfig == null)
			commonConfig = createCommonFile();
		if (createSided) {
			if (serverConfig == null)
				serverConfig = createServerFile();
			if (clientConfig == null)
				clientConfig = createClientFile();
		}
	}
	
	protected abstract void onFileInit(FileInitType type, File file) throws IOException;
	
	protected enum FileInitType {
		COMMON,
		SERVER,
		CLIENT
	}
	
	protected File createCommonFile() throws IOException {
		File file = configPath.resolve(configName + "_common.properties").toFile();
		if (!file.exists()) {
			file.createNewFile();
			onFileInit(FileInitType.COMMON, file);
		}
		return file;
	}
	
	protected File createServerFile() throws IOException {
		File file = configPath.resolve(configName + "_server.properties").toFile();
		if (!file.exists()) {
			file.createNewFile();
			onFileInit(FileInitType.SERVER, file);
		}
		return file;
	}
	
	protected File createClientFile() throws IOException {
		File file = configPath.resolve(configName + "_client.properties").toFile();
		if (!file.exists()) {
			file.createNewFile();
			onFileInit(FileInitType.CLIENT, file);
		}
		return file;
	}
	
	public String getConfigValue(String key, String defaultValue) {
		try {
			return getConfigValue0(key, defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	protected final String getConfigValue0(String key, String defaultValue) {
		String result;
		if (XplatUtils.getSide().isServer()) {
			result = serverProperties.getProperty(key, defaultValue);
		} else {
			result = clientProperties.getProperty(key, defaultValue);
		}
		if (result != null) return result;
		return commonProperties.getProperty(key, defaultValue);
	}
	
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
