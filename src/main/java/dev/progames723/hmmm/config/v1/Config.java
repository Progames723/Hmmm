package dev.progames723.hmmm.config.v1;

import dev.progames723.hmmm.config.v1.internal.ConfigUtils;
import dev.progames723.hmmm.misc.IterableHashMap;
import dev.progames723.hmmm.misc.IterableMap;

import java.io.IOException;

public interface Config<K, V> {
	void loadConfig() throws IOException;
	
	void saveConfig() throws IOException;
	
	void unloadConfig() throws IOException;
	
	default void reloadConfig() throws IOException {
		unloadConfig();
		loadConfig();
	}
	
	boolean isLoaded();
	
	boolean throwExceptionIfMoreThanOneInstance();
	
	default void doThrowException() {
		throw new IllegalStateException("Only one config instance allowed!");
	}
	
	default IterableMap<K, V> getAllValues() {
		IterableMap<K, V> map = new IterableHashMap<>();
		map.putAll(getCommonValues());
		map.putAll(getServerValues());
		map.putAll(getClientValues());
		return map;
	}
	
	IterableMap<K, V> getCommonValues();
	
	IterableMap<K, V> getServerValues();
	
	IterableMap<K, V> getClientValues();
	
	default void registerConfigInstance() {
		registerConfigInstance(this);
	}
	
	static void registerConfigInstance(Config<?, ?> config) {
		if (config.throwExceptionIfMoreThanOneInstance()) return;
		ConfigUtils.registerConfig(config);
	}
}
