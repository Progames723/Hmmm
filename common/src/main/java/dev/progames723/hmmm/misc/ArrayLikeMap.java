package dev.progames723.hmmm.misc;

import java.util.Map;

public interface ArrayLikeMap<K, V> extends Map<K, V>, ArrayLike<Map.Entry<K, V>> {
	void restoreEntrySet();
}
