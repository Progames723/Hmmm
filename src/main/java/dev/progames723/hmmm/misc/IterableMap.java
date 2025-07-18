package dev.progames723.hmmm.misc;

import dev.progames723.hmmm.HmmmException;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public interface IterableMap<K, V> extends Map<K, V>, Iterable<Map.Entry<K, V>> {
	@Override
	@NotNull
	default Iterator<Entry<K, V>> iterator() {
		return entrySet().iterator();
	}
	
	@Override
	default void forEach(Consumer<? super Entry<K, V>> action) {
		if (action == null) throw new HmmmException((Class<?>) null, new NullPointerException("Null consumer not permitted!"));
		this.forEach(((k, v) -> action.accept(new AbstractMap.SimpleEntry<>(k, v))));
	}
	
	@SafeVarargs
	static <K, V> IterableMap<K, V> ofEntries(Map.Entry<K, V>... entries) {
		IterableMap<K, V> map = new IterableHashMap<>();
		for (Map.Entry<K, V> entry : entries) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}
}
