package dev.progames723.hmmm;

import java.util.*;

public abstract class MappingsImpl implements MappingsExtended {
	protected final Collection<String> from;
	
	protected final Collection<String> to;
	
	public MappingsImpl() {
		from = Collections.emptyList();
		to = Collections.emptyList();
	}
	
	public MappingsImpl(Collection<String> in, Collection<String> out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null lists not allowed!");
		}
		if (in.size() != out.size()) {
			throw new RuntimeException("List sizes dont match!");
		}
		from = in;
		to = out;
	}
	
	public MappingsImpl(String[] in, String[] out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null lists not allowed!");
		}
		if (in.length != out.length) {
			throw new RuntimeException("Array sizes dont match!");
		}
		from = List.of(in);
		to = List.of(out);
	}
	
	public MappingsImpl(Enumeration<String> in, Enumeration<String> out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null lists not allowed!");
		}
		ArrayList<String> inList = new ArrayList<>();
		ArrayList<String> outList = new ArrayList<>();
		in.asIterator().forEachRemaining(inList::add);
		out.asIterator().forEachRemaining(outList::add);
		if (inList.size() != outList.size()) {
			throw new RuntimeException("Enumeration sizes dont match!");
		}
		from = inList;
		to = outList;
	}
	
	public MappingsImpl(Iterator<String> in, Iterator<String> out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null lists not allowed!");
		}
		ArrayList<String> inList = new ArrayList<>();
		ArrayList<String> outList = new ArrayList<>();
		in.forEachRemaining(inList::add);
		out.forEachRemaining(outList::add);
		if (inList.size() != outList.size()) {
			throw new RuntimeException("Iterator sizes dont match!");
		}
		from = inList;
		to = outList;
	}
	
	public MappingsImpl(Map<String, String> mappingMap) {
		if (mappingMap == null) {
			throw new RuntimeException("Map is null!");
		}
		Collection<String> in = mappingMap.keySet();
		Collection<String> out = mappingMap.values();
		from = in;
		to = out;
	}
	
	@Override
	public String map(String string) {
		for (int i = 0; i < from.size(); i++) {
			if (from.stream().toList().get(i).equals(string)) {
				return to.stream().toList().get(i);
			}
		}
		return string;
	}
	
	@Override
	public String unmap(String string) {
		for (int i = 0; i < to.size(); i++) {
			if (to.stream().toList().get(i).equals(string)) {
				return from.stream().toList().get(i);
			}
		}
		return string;
	}
	
	/**
	 * mapping a class to obfuscated(whatever is used)
	 */
	public abstract String mapClassName(String className);
	
	/**
	 * mapping a field to obfuscated(whatever is used)
	 */
	public abstract String mapField(String className, String field, String descriptor);
	
	/**
	 * mapping a method to obfuscated(whatever is used)
	 */
	public abstract String mapMethod(String className, String method, String descriptor);
	
	/**
	 * unmapping a class from obfuscated(whatever is used)
	 */
	public abstract String unmapClassName(String className);
	
	/**
	 * unmapping a field from obfuscated(whatever is used)
	 */
	public abstract String unmapField(String className, String field, String descriptor);
	
	/**
	 * unmapping a method from obfuscated(whatever is used)
	 */
	public abstract String unmapMethod(String className, String method, String descriptor);
}
