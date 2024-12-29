package dev.progames723.hmmm.misc;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface ArrayLike<T> extends Stream<T>, Iterable<T>, Enumeration<T>, Iterator<T>, Spliterator<T> {
	@Override
	boolean hasMoreElements();
	
	@Override
	T nextElement();
	
	@Override
	default Iterator<T> asIterator() {
		return this;
	}
	
	@Override
	boolean hasNext();
	
	@Override
	T next();
	
	@Override
	void remove();
	
	@Override
	default void forEachRemaining(Consumer<? super T> action) {
		Iterator.super.forEachRemaining(action);
	}
	
	@Override
	boolean tryAdvance(Consumer<? super T> action);
	
	@Override
	Spliterator<T> trySplit();
	
	@Override
	long estimateSize();
	
	@Override
	int characteristics();
	
	@Override
	Stream<T> filter(Predicate<? super T> predicate);
	
	@Override
	<R> Stream<R> map(Function<? super T, ? extends R> mapper);
	
	@Override
	IntStream mapToInt(ToIntFunction<? super T> mapper);
	
	@Override
	LongStream mapToLong(ToLongFunction<? super T> mapper);
	
	@Override
	DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper);
	
	@Override
	<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
	
	@Override
	IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);
	
	@Override
	LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper);
	
	@Override
	DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper);
	
	@Override
	Stream<T> distinct();
	
	@Override
	Stream<T> sorted();
	
	@Override
	Stream<T> sorted(Comparator<? super T> comparator);
	
	@Override
	Stream<T> peek(Consumer<? super T> action);
	
	@Override
	Stream<T> limit(long maxSize);
	
	@Override
	Stream<T> skip(long n);
	
	@Override
	default void forEach(Consumer<? super T> action) {
		forEachRemaining(action);
	}
	
	@Override
	void forEachOrdered(Consumer<? super T> action);
	
	@Override
	Object @NotNull [] toArray();
	
	@Override
	<A> A @NotNull [] toArray(IntFunction<A[]> generator);
	
	@Override
	T reduce(T identity, BinaryOperator<T> accumulator);
	
	@Override
	@NotNull
	Optional<T> reduce(BinaryOperator<T> accumulator);
	
	@Override
	<U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);
	
	@Override
	<R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner);
	
	@Override
	<R, A> R collect(Collector<? super T, A, R> collector);
	
	@Override
	@NotNull
	Optional<T> min(Comparator<? super T> comparator);
	
	@Override
	@NotNull
	Optional<T> max(Comparator<? super T> comparator);
	
	@Override
	long count();
	
	@Override
	boolean anyMatch(Predicate<? super T> predicate);
	
	@Override
	boolean allMatch(Predicate<? super T> predicate);
	
	@Override
	boolean noneMatch(Predicate<? super T> predicate);
	
	@Override
	@NotNull
	Optional<T> findFirst();
	
	@Override
	@NotNull
	Optional<T> findAny();
	
	@Override
	@NotNull
	default Iterator<T> iterator() {
		return this;
	}
	
	@Override
	@NotNull
	default Spliterator<T> spliterator() {
		return this;
	}
	
	@Override
	boolean isParallel();
	
	@Override
	@NotNull
	Stream<T> sequential();
	
	@Override
	@NotNull
	Stream<T> parallel();
	
	@Override
	@NotNull
	Stream<T> unordered();
	
	@Override
	@NotNull
	Stream<T> onClose(@NotNull Runnable closeHandler);
	
	@Override
	void close();
}
