package dev.progames723.hmmm.misc;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ArrayLikeMapImpl<K, V> extends AbstractMap<K, V> implements ArrayLikeMap<K, V> {
	private final ListSet<Entry<K, V>> entrySet = new ListSet<>();
	private final ListSet<Entry<K, V>> backupEntrySet = entrySet;
	private int currentElementIndex = 0;
	
	public ArrayLikeMapImpl(K key, V value) {
		this(new SimpleEntry<>(key, value));
	}
	
	public ArrayLikeMapImpl(Entry<K, V>[] entries) {
		this(List.of(entries));
	}
	
	public ArrayLikeMapImpl(Entry<K, V> entry) {
		entrySet.add(entry);
	}
	
	public ArrayLikeMapImpl(Collection<Entry<K, V>> entries) {
		entrySet.addAll(entries);
	}
	
	public ArrayLikeMapImpl(Map<K, V> map) {
		entrySet.addAll(map.entrySet());
	}
	
	@Override
	public @NotNull Set<Entry<K, V>> entrySet() {
		return entrySet;
	}
	
	@Override
	public boolean hasMoreElements() {
		return !entrySet.isEmpty();
	}
	
	@Override
	public Entry<K, V> nextElement() {
		ListSet<Entry<K, V>> listSet = entrySet;
		entrySet.clear();
		assert !listSet.equals(entrySet) : "how the fuck";
		entrySet.addAll(listSet.subList(currentElementIndex++ > listSet.size() ? listSet.size() : currentElementIndex, listSet.size()));
		return !entrySet.isEmpty() ? entrySet.get(0) : null;
	}
	
	@Override
	public boolean hasNext() {
		return currentElementIndex + 1 < entrySet.size();
	}
	
	@Override
	public Entry<K, V> next() {
		return nextElement();
	}
	
	@Override
	public void remove() {
		entrySet.remove(currentElementIndex);
	}
	
	@Override
	public boolean tryAdvance(Consumer<? super Entry<K, V>> action) {
		Entry<K, V> entry = nextElement();
		if (entry == null) return false;
		action.accept(entry);
		return true;
	}
	
	@Override
	public Spliterator<Entry<K, V>> trySplit() {
		return entrySet.subList(currentElementIndex, entrySet.size()).spliterator().trySplit();
	}
	
	@Override
	public long estimateSize() {
		return entrySet.size();
	}
	
	@Override
	public int characteristics() {
		return ORDERED;
	}
	
	@Override
	public Stream<Entry<K, V>> filter(Predicate<? super Entry<K, V>> predicate) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().filter(predicate);
	}
	
	@Override
	public <R> Stream<R> map(Function<? super Entry<K, V>, ? extends R> mapper) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().map(mapper);
	}
	
	@Override
	public IntStream mapToInt(ToIntFunction<? super Entry<K, V>> mapper) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().mapToInt(mapper);
	}
	
	@Override
	public LongStream mapToLong(ToLongFunction<? super Entry<K, V>> mapper) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().mapToLong(mapper);
	}
	
	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super Entry<K, V>> mapper) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().mapToDouble(mapper);
	}
	
	@Override
	public <R> Stream<R> flatMap(Function<? super Entry<K, V>, ? extends Stream<? extends R>> mapper) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().flatMap(mapper);
	}
	
	@Override
	public IntStream flatMapToInt(Function<? super Entry<K, V>, ? extends IntStream> mapper) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().flatMapToInt(mapper);
	}
	
	@Override
	public LongStream flatMapToLong(Function<? super Entry<K, V>, ? extends LongStream> mapper) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().flatMapToLong(mapper);
	}
	
	@Override
	public DoubleStream flatMapToDouble(Function<? super Entry<K, V>, ? extends DoubleStream> mapper) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().flatMapToDouble(mapper);
	}
	
	@Override
	public Stream<Entry<K, V>> distinct() {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().distinct();
	}
	
	@Override
	public Stream<Entry<K, V>> sorted() {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().sorted();
	}
	
	@Override
	public Stream<Entry<K, V>> sorted(Comparator<? super Entry<K, V>> comparator) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().sorted(comparator);
	}
	
	@Override
	public Stream<Entry<K, V>> peek(Consumer<? super Entry<K, V>> action) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().peek(action);
	}
	
	@Override
	public Stream<Entry<K, V>> limit(long maxSize) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().limit(maxSize);
	}
	
	@Override
	public Stream<Entry<K, V>> skip(long n) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().skip(n);
	}
	
	@Override
	public void forEachOrdered(Consumer<? super Entry<K, V>> action) {
		entrySet.subList(currentElementIndex, entrySet.size()).forEach(action);
	}
	
	@Override
	public Object @NotNull [] toArray() {
		return entrySet.subList(currentElementIndex, entrySet.size()).toArray();
	}
	
	@Override
	public <A> A @NotNull [] toArray(IntFunction<A[]> generator) {
		return entrySet.subList(currentElementIndex, entrySet.size()).toArray(generator);
	}
	
	@Override
	public Entry<K, V> reduce(Entry<K, V> identity, BinaryOperator<Entry<K, V>> accumulator) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().reduce(identity, accumulator);
	}
	
	@Override
	public @NotNull Optional<Entry<K, V>> reduce(BinaryOperator<Entry<K, V>> accumulator) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().reduce(accumulator);
	}
	
	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super Entry<K, V>, U> accumulator, BinaryOperator<U> combiner) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().reduce(identity, accumulator, combiner);
	}
	
	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super Entry<K, V>> accumulator, BiConsumer<R, R> combiner) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().collect(supplier, accumulator, combiner);
	}
	
	@Override
	public <R, A> R collect(Collector<? super Entry<K, V>, A, R> collector) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().collect(collector);
	}
	
	@Override
	public @NotNull Optional<Entry<K, V>> min(Comparator<? super Entry<K, V>> comparator) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().min(comparator);
	}
	
	@Override
	public @NotNull Optional<Entry<K, V>> max(Comparator<? super Entry<K, V>> comparator) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().max(comparator);
	}
	
	@Override
	public long count() {
		return entrySet.size() - currentElementIndex;
	}
	
	@Override
	public boolean anyMatch(Predicate<? super Entry<K, V>> predicate) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().anyMatch(predicate);
	}
	
	@Override
	public boolean allMatch(Predicate<? super Entry<K, V>> predicate) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().allMatch(predicate);
	}
	
	@Override
	public boolean noneMatch(Predicate<? super Entry<K, V>> predicate) {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().noneMatch(predicate);
	}
	
	@Override
	public @NotNull Optional<Entry<K, V>> findFirst() {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().findFirst();
	}
	
	@Override
	public @NotNull Optional<Entry<K, V>> findAny() {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().findAny();
	}
	
	@Override
	public boolean isParallel() {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().isParallel();
	}
	
	@Override
	public @NotNull Stream<Entry<K, V>> sequential() {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream();
	}
	
	@Override
	public @NotNull Stream<Entry<K, V>> parallel() {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().parallel();
	}
	
	@Override
	public @NotNull Stream<Entry<K, V>> unordered() {
		return entrySet.subList(currentElementIndex, entrySet.size()).stream().unordered();
	}
	
	@Override
	public @NotNull Stream<Entry<K, V>> onClose(@NotNull Runnable closeHandler) {
		closeHandler.run();
		close();
		return Stream.empty();
	}
	
	@Override
	public void close() {
		entrySet.clear();
	}
	
	@Override
	public V put(K key, V value) {
		entrySet.add(new SimpleEntry<>(key, value));
		return null;
	}
	
	@Override
	public void restoreEntrySet() {
		entrySet.clear();
		entrySet.addAll(backupEntrySet);
	}
}
