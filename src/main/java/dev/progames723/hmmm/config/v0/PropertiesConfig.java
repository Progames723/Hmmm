package dev.progames723.hmmm.config.v0;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.misc.IterableHashMap;
import dev.progames723.hmmm.misc.IterableMap;
import dev.progames723.hmmm.utils.XplatUtils;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * barebones config impl
 */
public abstract class PropertiesConfig extends Config {
	private final Properties commonProperties = new Properties();
	private final Properties clientProperties = new Properties();
	private final Properties serverProperties = new Properties();
	
	protected PropertiesConfig(String configName, String dirName, boolean createSided) throws IllegalStateException {
		super(configName, dirName, createSided, "properties");
	}
	
	public void loadConfig() throws IOException {
		if (isLoaded()) {
			return;
		}
		load0();
		setLoaded0(true);
	}
	
	private void load0() throws IOException {
		createFilesIfNeeded();
		try (Reader reader = new BufferedReader(new FileReader(getCommonConfig0()))) {
			commonProperties.load(reader);
		} catch (IOException e) {
			HmmmLibrary.LOGGER.warn("Failed to load common config for {}!", this.getClass());
		}
		if (!createSided) return;
		try (Reader reader = new BufferedReader(new FileReader(getClientConfig0()))) {
			clientProperties.load(reader);
		} catch (IOException e) {
			HmmmLibrary.LOGGER.warn("Failed to load client config for {}!", this.getClass());
		}
		try (Reader reader = new BufferedReader(new FileReader(getServerConfig0()))) {
			serverProperties.load(reader);
		} catch (IOException e) {
			HmmmLibrary.LOGGER.warn("Failed to load server config for {}!", this.getClass());
		}
	}
	
	public void unloadConfig() {
		if (!isLoaded()) return;
		commonProperties.clear();
		clientProperties.clear();
		serverProperties.clear();
		setLoaded0(false);
	}
	
	@Override
	final void internalInitValues(FileType type) {
		Map<String, String> map = createDefaultConfigValues(type);
		switch (type) {
			case COMMON -> {
				commonProperties.clear();
				commonProperties.putAll(map);
			}
			case SERVER -> {
				serverProperties.clear();
				serverProperties.putAll(map);
			}
			case CLIENT -> {
				clientProperties.clear();
				clientProperties.putAll(map);
			}
		}
	}
	
	@Override
	public void saveConfig() throws IOException {
		File file = getCommonConfig0();
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			writeComments(writer, FileType.COMMON);
			writer.flush();
			for (Map.Entry<String, String> entry : getAllCommonValues()) {
				if (entry.getValue() == null || entry.getKey() == null) return;
				writer.println(entry.getKey() + "=" + entry.getValue());
			}
		}
		if (!createSided) return;
		file = getClientConfig0();
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			writeComments(writer, FileType.CLIENT);
			writer.flush();
			for (Map.Entry<String, String> entry : getAllClientValues()) {
				if (entry.getValue() == null || entry.getKey() == null) return;
				writer.println(entry.getKey() + "=" + entry.getValue());
			}
		}
		file = getServerConfig0();
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			for (Map.Entry<String, String> entry : getAllServerValues()) {
				if (entry.getValue() == null || entry.getKey() == null) return;
				writer.println(entry.getKey() + "=" + entry.getValue());
			}
		}
	}
	
	protected final String getConfigValue0(String key, String defaultValue) {
		String result;
		if (XplatUtils.getSide().isServer()) {
			result = serverProperties.getProperty(key, defaultValue);
		} else {
			result = clientProperties.getProperty(key, defaultValue);
		}
		if (result != null && !result.equals(defaultValue)) return result;
		return commonProperties.getProperty(key, defaultValue);
	}
	
	private IterableMap<String, String> iterateThroughProperties(final Properties properties) {
		IterableMap<String, String> map = new IterableHashMap<>();
		properties.keySet().forEach(o -> {
			if (!(o instanceof String s)) return;
			String val = properties.getProperty(s);
			if (val == null) return;
			map.put(s, val);
		});
		return map;
	}
	
	@Override
	public IterableMap<String, String> getAllCommonValues() {
		return new IterableHashMap<>(iterateThroughProperties(commonProperties));
	}
	
	@Override
	public IterableMap<String, String> getAllClientValues() {
		return new IterableHashMap<>(iterateThroughProperties(clientProperties));
	}
	
	@Override
	public IterableMap<String, String> getAllServerValues() {
		return new IterableHashMap<>(iterateThroughProperties(serverProperties));
	}
}
