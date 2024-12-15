package dev.progames723.hmmm.misc;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ListSet<E> extends ArrayList<E> implements List<E>, Set<E> {
	public ListSet() {
		super();
	}
	
	public ListSet(Collection<E> collection) {
		super(collection);
	}
	
	public ListSet(int initialCapacity) {
		super(initialCapacity);
	}
	
	@Override
	public Object @NotNull [] toArray() {
		return super.toArray();
	}
	
	@Override
	public <T> T @NotNull [] toArray(T @NotNull [] array) {
		return super.toArray(array);
	}
	
	@Override
	public boolean addAll(int index, @NotNull Collection<? extends E> c) {
		return super.addAll(index, c);
	}
	
	@Override
	public boolean addAll(@NotNull Collection<? extends E> c) {
		return super.addAll(c);
	}
	
	@Override
	public boolean retainAll(@NotNull Collection<?> c) {
		return super.retainAll(c);
	}
	
	@Override
	public boolean removeAll(@NotNull Collection<?> c) {
		return super.removeAll(c);
	}
}
