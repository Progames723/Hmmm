package dev.progames723.hmmm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("unused")
public class LimitedCapacityArrayList<E> extends ArrayList<E> {//idk but funny
	private final long maxCapacity;
	
	public LimitedCapacityArrayList(long maxCapacity) {
		super();
		this.maxCapacity = maxCapacity;
	}
	
	public LimitedCapacityArrayList(long maxCapacity, int initialCapacity) {
		super(initialCapacity);
		this.maxCapacity = maxCapacity;
	}
	
	public LimitedCapacityArrayList() {
		super();
		maxCapacity = Long.MAX_VALUE;
	}
	
	public LimitedCapacityArrayList(Collection<? extends E> collection) {
		super(collection);
		maxCapacity = Long.MAX_VALUE;
	}
	
	public LimitedCapacityArrayList(Collection<? extends E> collection, long maxCapacity) {
		super(collection);
		if (maxCapacity < collection.size()) {
			throw new HmmmException("Max capacity cannot be less than the collection size");
		}
		this.maxCapacity = maxCapacity;
	}
	
	@Override
	public void ensureCapacity(int minCapacity) {
		if (maxCapacity > super.size()) {
			super.ensureCapacity(minCapacity);
		} else {
			throw new HmmmException("List capacity exceeded");
		}
	}
	
	private boolean checkCapacity() {
		return size() + 1 <= maxCapacity;
	}
	
	private boolean checkCapacity(Collection<? extends E> collection) {
		return size() + collection.size() + 1 <= maxCapacity;
	}
	
	private void throwExceptionIfCapacityExceeded() {
		if (!checkCapacity()) {
			throw new HmmmException("List capacity exceeded, %1$s out of %2$s possible elements".formatted(size()+1, maxCapacity));
		}
	}
	
	private void throwExceptionIfCapacityExceeded(Collection<? extends E> collection) {
		if (!checkCapacity(collection)) {
			throw new HmmmException("List capacity exceeded, %1$s out of %2$s possible elements".formatted(size()+collection.size(), maxCapacity));
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
	public boolean addAll(Collection<? extends E> collection) {
		throwExceptionIfCapacityExceeded(collection);
		return super.addAll(collection);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		throwExceptionIfCapacityExceeded(collection);
		return super.addAll(index, collection);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof LimitedCapacityArrayList<?> list) return super.equals(list) && list.maxCapacity == this.maxCapacity;
		return super.equals(o);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), maxCapacity);
	}
}

