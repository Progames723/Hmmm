package dev.progames723.hmmm;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class BaseMappingsImpl<I, O> implements Mappings<I, O> {
	private final Map<I, O> mappings = new HashMap<>();
	
	public BaseMappingsImpl(Collection<I> in, Collection<O> out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null args not allowed!");
		}
		if (in.size() != out.size()) {
			throw new RuntimeException("Collection sizes dont match!");
		}
		List<I> inList = List.copyOf(in);
		List<O> outList = List.copyOf(out);
		putValues(inList, outList);
	}
	
	public BaseMappingsImpl(I[] in, O[] out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null args not allowed!");
		}
		if (in.length != out.length) {
			throw new RuntimeException("Array sizes dont match!");
		}
		List<I> inList = List.of(in);
		List<O> outList = List.of(out);
		putValues(inList, outList);
	}
	
	public BaseMappingsImpl(Enumeration<I> in, Enumeration<O> out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null lists not allowed!");
		}
		ArrayList<I> inList = new ArrayList<>();
		ArrayList<O> outList = new ArrayList<>();
		in.asIterator().forEachRemaining(inList::add);
		out.asIterator().forEachRemaining(outList::add);
		if (inList.size() != outList.size()) {
			throw new RuntimeException("Enumeration sizes dont match!");
		}
		putValues(inList, outList);
	}
	
	public BaseMappingsImpl(Iterator<I> in, Iterator<O> out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null lists not allowed!");
		}
		ArrayList<I> inList = new ArrayList<>();
		ArrayList<O> outList = new ArrayList<>();
		in.forEachRemaining(inList::add);
		out.forEachRemaining(outList::add);
		if (inList.size() != outList.size()) {
			throw new RuntimeException("Iterator sizes dont match!");
		}
		putValues(inList, outList);
	}
	
	public BaseMappingsImpl(Map<I, O> mappingMap) {
		if (mappingMap == null) {
			throw new RuntimeException("Map is null!");
		}
		mappings.putAll(mappingMap);
	}
	
	private void putValues(List<I> in, List<O> out) {
		for (int i = 0; i < in.size(); i++) {
			mappings.put(in.get(i), out.get(i));
		}
	}
	
	@Override
	public O map(I input) {
		if (input == null) return null;
		return mappings.get(input);
	}
	
	@Override
	public I unmap(O output) {
		if (output == null) return null;
		AtomicReference<I> returned = new AtomicReference<>();
		mappings.forEach((i, o1) -> {
			if (output.equals(o1)) returned.set(i);
		});
		return returned.get();
	}
}
