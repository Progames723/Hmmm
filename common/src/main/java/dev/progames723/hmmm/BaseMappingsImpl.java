package dev.progames723.hmmm;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class BaseMappingsImpl<A, B> implements Mappings<A, B> {
	private final Map<A, B> mappings = new HashMap<>();
	
	public BaseMappingsImpl(Collection<A> in, Collection<B> out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null args not allowed!");
		}
		if (in.size() != out.size()) {
			throw new RuntimeException("Collection sizes dont match!");
		}
		List<A> inList = List.copyOf(in);
		List<B> outList = List.copyOf(out);
		putValues(inList, outList);
	}
	
	public BaseMappingsImpl(A[] in, B[] out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null args not allowed!");
		}
		if (in.length != out.length) {
			throw new RuntimeException("Array sizes dont match!");
		}
		List<A> inList = List.of(in);
		List<B> outList = List.of(out);
		putValues(inList, outList);
	}
	
	public BaseMappingsImpl(Enumeration<A> in, Enumeration<B> out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null lists not allowed!");
		}
		ArrayList<A> inList = new ArrayList<>();
		ArrayList<B> outList = new ArrayList<>();
		in.asIterator().forEachRemaining(inList::add);
		out.asIterator().forEachRemaining(outList::add);
		if (inList.size() != outList.size()) {
			throw new RuntimeException("Enumeration sizes dont match!");
		}
		putValues(inList, outList);
	}
	
	public BaseMappingsImpl(Iterator<A> in, Iterator<B> out) {
		if (in == null || out == null) {
			throw new NullPointerException("Null lists not allowed!");
		}
		ArrayList<A> inList = new ArrayList<>();
		ArrayList<B> outList = new ArrayList<>();
		in.forEachRemaining(inList::add);
		out.forEachRemaining(outList::add);
		if (inList.size() != outList.size()) {
			throw new RuntimeException("Iterator sizes dont match!");
		}
		putValues(inList, outList);
	}
	
	public BaseMappingsImpl(Map<A, B> mappingMap) {
		if (mappingMap == null) {
			throw new RuntimeException("Map is null!");
		}
		mappings.putAll(mappingMap);
	}
	
	private void putValues(List<A> in, List<B> out) {
		for (int i = 0; i < in.size(); i++) {
			mappings.put(in.get(i), out.get(i));
		}
	}
	
	@Override
	public B map(A a) {
		if (a == null) return null;
		return mappings.get(a);
	}
	
	@Override
	public A unmap(B b) {
		if (b == null) return null;
		AtomicReference<A> returned = new AtomicReference<>();
		mappings.forEach((a, b1) -> {
			if (b.equals(b1)) returned.set(a);
		});
		return returned.get();
	}
}
