package dev.progames723.hmmm.misc.extendable_enum;

import com.google.common.collect.Maps;
import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.ReflectUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ExtendableEnumManager<T extends ExtendableEnum> {
	private final Int2ObjectMap<T> enumMapByOrdinal = new Int2ObjectLinkedOpenHashMap<>();
	private final Map<String, T> enumMapByName = Maps.newLinkedHashMap();
	private final Map<String, Class<?>> enums = Maps.newConcurrentMap();
	private final String enumName;
	private int lastOrdinal = 0;
	
	public ExtendableEnumManager(String enumName) {
		this.enumName = enumName;
	}
	
	@CallerSensitive
	public ExtendableEnumManager() {
		this(ReflectUtil.getCaller().getDeclaringClass().getSimpleName());
	}
	
	public void registerEnumClass(String modid, Class<?> cls) {
		if (this.enums.containsKey(modid)) {
			HmmmLibrary.LOGGER.error("{} is already registered in {}", modid, this.enumName);
		}
		
		HmmmLibrary.LOGGER.debug("Registered Extendable Enum {} in {}", cls, this.enumName);
		this.enums.put(modid, cls);
	}
	
	public void loadEnum() {
		List<String> orderByModid = new ArrayList<>(this.enums.keySet());
		Collections.sort(orderByModid);
		Class<?> cls = null;
		
		try {
			for (String modid : orderByModid) {
				cls = this.enums.get(modid);
				Method m = cls.getMethod("values");
				m.invoke(null);
				HmmmLibrary.LOGGER.debug("Loaded enums in {}", cls);
			}
		} catch (ClassCastException e) {
			HmmmLibrary.LOGGER.error("%s is not an ExtendableEnum!".formatted(cls.getCanonicalName()), e);
		} catch (NoSuchMethodException e) {
			HmmmLibrary.LOGGER.error("%s is not an Enum class!".formatted(cls.getCanonicalName()), e);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			HmmmLibrary.LOGGER.warn("Error while loading extendable enum %s".formatted(cls.getCanonicalName()), e);
		}
		
		HmmmLibrary.LOGGER.debug("All enums are loaded: {} {}", this.enumName, this.enumMapByName.values());
	}
	
	public int assign(T value) {
		int lastOrdinal = this.lastOrdinal;
		String enumName = value.toString().toLowerCase(Locale.ROOT);
		if (this.enumMapByName.containsKey(enumName)) {
			throw new HmmmException("Enum name " + enumName + " already exists in " + this.enumName);
		} else {
			this.enumMapByOrdinal.put(lastOrdinal, value);
			this.enumMapByName.put(enumName, value);
			++this.lastOrdinal;
			return lastOrdinal;
		}
	}
	
	public T getOrThrow(int id) {
		if (!this.enumMapByOrdinal.containsKey(id)) {
			throw new HmmmException("Enum id " + id + " does not exist in " + this.enumName);
		} else {
			return this.enumMapByOrdinal.get(id);
		}
	}
	
	public T getOrThrow(String name) {
		String key = name.toLowerCase(Locale.ROOT);
		if (!this.enumMapByName.containsKey(key)) {
			throw new HmmmException("Enum name " + key + " does not exist in " + this.enumName);
		} else {
			return this.enumMapByName.get(key);
		}
	}
	
	public T get(int id) {
		return this.enumMapByOrdinal.get(id);
	}
	
	public T get(String name) {
		return this.enumMapByName.get(name.toLowerCase(Locale.ROOT));
	}
	
	public Collection<T> values() {
		return this.enumMapByOrdinal.values();
	}
}
