package dev.progames723.hmmm.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Binding<T> {
	private final Supplier<T> getter;
	private final Consumer<T> setter;
	private final T defaultValue;
	
	/**
	 * add a comment
	 * @param getter getter
	 * @param setter setter
	 * @param defaultValue default value
	 */
	public Binding(Supplier<T> getter, Consumer<T> setter, T defaultValue) {
		this.getter = getter;
		this.setter = setter;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * gets value
	 * @return {@link T} aka the class that this instance uses
	 */
	public T getValue() {
		return getter.get();
	}
	
	/**
	 * sets value
	 * @param value value
	 */
	public void setValue(T value) {
		setter.accept(value);
	}
	
	/**
	 * gets default value
	 * @return {@link T} aka the class that this instance uses
	 */
	public T getDefaultValue() {
		return defaultValue;
	}
}
