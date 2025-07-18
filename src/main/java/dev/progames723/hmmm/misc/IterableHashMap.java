package dev.progames723.hmmm.misc;

import java.util.HashMap;
import java.util.Map;

public class IterableHashMap<K, V> extends HashMap<K, V> implements IterableMap<K, V> {
	public IterableHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
	
	public IterableHashMap(int initialCapacity) {
		super(initialCapacity);
	}
	
	public IterableHashMap() {
		super();
	}
	
	public IterableHashMap(Map<? extends K, ? extends V> m) {
		super(m);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (!(o instanceof IterableHashMap<?,?>)) return false;
		IterableHashMap<K, V> map;
		try {
			map = (IterableHashMap<K, V>) o;
		} catch (ClassCastException e) {return false;}
		for (Map.Entry<K, V> entry : this) {
			V value = entry.getValue();
			V otherValue = map.get(entry.getKey());
			if (value == null || otherValue == null) {
				if (value == otherValue) continue;
				return false;
			} else {
				if (!otherValue.equals(value)) return false;
			}
		}
		return true;
	}
}
