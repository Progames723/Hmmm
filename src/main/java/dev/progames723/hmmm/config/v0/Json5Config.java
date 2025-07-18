package dev.progames723.hmmm.config.v0;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.progames723.hmmm.event.utils.DoubleValue;
import dev.progames723.hmmm.misc.IterableHashMap;
import dev.progames723.hmmm.misc.IterableMap;

import java.io.*;
import java.util.Map;

public abstract class Json5Config extends Config {
	private final IterableMap<String, String> commonValues = new IterableHashMap<>();
	private final IterableMap<String, String> clientValues = new IterableHashMap<>();
	private final IterableMap<String, String> serverValues = new IterableHashMap<>();
	
	protected Json5Config(String configName, String dirName, boolean createSided) throws IllegalStateException {
		super(configName, dirName, createSided, "json5");
	}
	
	@Override
	protected final String getConfigValue0(String key, String defaultValue) {
		String value = commonValues.getOrDefault(key, defaultValue);
		if (!createSided) return value;
		if (value.equals(defaultValue))
			value = clientValues.getOrDefault(key, defaultValue);
		if (value.equals(defaultValue))
			value = serverValues.getOrDefault(key, defaultValue);
		return value;
	}
	
	@Override
	public final void loadConfig() throws IOException {
		if (isLoaded()) {
			return;
		}
		load0();
		setLoaded0(true);
	}
	
	private void load0() throws IOException {
		createFilesIfNeeded();
		Gson gson = new Gson();
		TypeToken<IterableHashMap<String, String>> type = new TypeToken<>() {};
		File currentFile = getCommonConfig0();
		try (JsonReader reader = new JsonReader(new BufferedReader(new FileReader(currentFile)))) {
			reader.setLenient(true);
			commonValues.putAll(gson.fromJson(reader, type));
		}
		if (!createSided) return;
		currentFile = getClientConfig0();
		try (JsonReader reader = new JsonReader(new BufferedReader(new FileReader(currentFile)))) {
			reader.setLenient(true);
			clientValues.putAll(gson.fromJson(reader, type));
		}
		currentFile = getServerConfig0();
		try (JsonReader reader = new JsonReader(new BufferedReader(new FileReader(currentFile)))) {
			reader.setLenient(true);
			serverValues.putAll(gson.fromJson(reader, type));
		}
	}
	
	@Override
	public final void unloadConfig() {
		if (!isLoaded()) return;
		setLoaded0(false);
		commonValues.clear();
		clientValues.clear();
		serverValues.clear();
	}
	
	@Override
	final void internalInitValues(FileType type) {
		Map<String, String> map = createDefaultConfigValues(type);
		switch (type) {
			case COMMON -> {
				commonValues.clear();
				commonValues.putAll(map);
			}
			case SERVER -> {
				clientValues.clear();
				clientValues.putAll(map);
			}
			case CLIENT -> {
				serverValues.clear();
				serverValues.putAll(map);
			}
		}
	}
	
	@Override
	public final void saveConfig() throws IOException {
		File currentFile = getCommonConfig0();
		{
			DoubleValue<JsonWriter, Writer> writer = getWriters(currentFile);
			try {
				writeJson(writer, commonValues);
				writeComments(writer.getB(), FileType.COMMON);
				writer.getB().flush();
			} finally {
				writer.getA().close();
			}
		}
		if (!createSided) return;
		currentFile = getClientConfig0();
		{
			DoubleValue<JsonWriter, Writer> writer = getWriters(currentFile);
			try {
				writeJson(writer, clientValues);
				writeComments(writer.getB(), FileType.CLIENT);
				writer.getB().flush();
			} finally {
				writer.getA().close();
			}
		}
		currentFile = getServerConfig0();
		{
			DoubleValue<JsonWriter, Writer> writer = getWriters(currentFile);
			try {
				writeJson(writer, serverValues);
				writeComments(writer.getB(), FileType.SERVER);
				writer.getB().flush();
			} finally {
				writer.getA().close();
			}
		}
	}
	
	protected final DoubleValue<JsonWriter, Writer> getWriters(File file) throws IOException {
		return getWriters(new BufferedWriter(new FileWriter(file)));
	}
	
	protected final DoubleValue<JsonWriter, Writer> getWriters(Writer writer) {
		JsonWriter jsonWriter = new JsonWriter(writer);
		jsonWriter.setLenient(true);
		return new DoubleValue<>(jsonWriter, writer);
	}
	
	protected final JsonWriter getJsonWriter(File file) throws IOException {
		return getWriters(file).getA();
	}
	
	protected final JsonWriter getJsonWriter(Writer writer) {
		return getWriters(writer).getA();
	}
	
	private void writeJson(DoubleValue<JsonWriter, Writer> writers, IterableMap<String, String> values) throws IOException {
		JsonWriter writer = writers.getA();
		writer.beginObject();
		for (Map.Entry<String, String> entry : values) {
			writer.flush();
			writeValueToJson(writer, entry.getKey(), entry.getValue());
		}
		writer.endObject();
	}
	
	@SuppressWarnings({"ConstantValue"})
	private void writeValueToJson(JsonWriter writer, String key, String value) throws IOException {
		Boolean isBoolean = null;
		Long isLong = null;
		Double isDouble = null;
		try {
			isBoolean = Boolean.parseBoolean(value);
			isLong = Long.parseLong(value);
			isDouble = Double.parseDouble(value);
		} catch (Exception ignored) {}
		writer.name(key);
		if (isBoolean != null) {
			writer.value(isBoolean);
			return;
		}
		if (isDouble != null && isLong == null) {
			writer.value(isDouble.doubleValue());
			return;
		} else if (isDouble != null && isLong != null) {
			writer.value(isLong.longValue());
			return;
		} else if (isLong != null) {
			writer.value(isLong.longValue());
			return;
		}
		writer.value(value);
	}
	
	@Override
	public IterableMap<String, String> getAllCommonValues() {
		return commonValues;
	}
	
	@Override
	public IterableMap<String, String> getAllClientValues() {
		return clientValues;
	}
	
	@Override
	public IterableMap<String, String> getAllServerValues() {
		return serverValues;
	}
}
