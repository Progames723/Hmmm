package dev.progames723.hmmm;

import java.util.ArrayList;
import java.util.Collection;

public class LimitedCapacityArrayList<E> extends ArrayList<E> {
	private int MAX_CAPACITY = Integer.MAX_VALUE;
	
	public LimitedCapacityArrayList(int maxCapacity) {
		super();
		this.MAX_CAPACITY = maxCapacity;
	}
	
	public LimitedCapacityArrayList(int maxCapacity, int initialCapacity) {
		super(initialCapacity);
		this.MAX_CAPACITY = maxCapacity;
	}
	
	public LimitedCapacityArrayList() {
		super();
	}
	
	@Override
	public void ensureCapacity(int minCapacity) {
		if (MAX_CAPACITY > super.size()) {
			super.ensureCapacity(minCapacity);
		} else {
			throw new RuntimeException("List capacity exceeded");
		}
	}
	
	private boolean checkCapacity() {
		return size()+1 <= MAX_CAPACITY;
	}
	
	private void throwExceptionIfCapacityExceeded() {
		if (!checkCapacity()) {
			throw new RuntimeException("List capacity exceeded, %1$s out of %2$s possible elements".formatted(size()+1, MAX_CAPACITY));
		}
	}
	
	private void throwExceptionIfCapacityExceeded(Collection<? extends E> collection) {
		if (!(size() <= MAX_CAPACITY + collection.size())) {
			throw new RuntimeException("List capacity exceeded, %1$s out of %2$s possible elements".formatted(size()+collection.size(), MAX_CAPACITY));
		}
	}
	
	@Override
	public boolean add(E e) {
		throwExceptionIfCapacityExceeded();
		return super.add(e);
	}
	
	@Override
	public void add(int index, E element) {
		throwExceptionIfCapacityExceeded();
		super.add(index, element);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		throwExceptionIfCapacityExceeded(c);
		return super.addAll(c);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throwExceptionIfCapacityExceeded(c);
		return super.addAll(index, c);
	}
}

