package dev.progames723.hmmm.event.api;

import org.jetbrains.annotations.Nullable;

/**
 * quite useless once you consider that extending the default event gives the same results
 */
@Deprecated(forRemoval = true)
public abstract class ReturnableEvent<T> extends Event {
	protected final boolean nullable;
	protected final boolean isVoid;
	protected T value;
	
	protected ReturnableEvent(boolean isVoid, boolean isCancellable, boolean nullable) {
		super(isCancellable);
		this.isVoid = isVoid;
		this.nullable = nullable;
	}
	
	public abstract boolean returnsNull();
	
	public boolean isNullable() {
		return nullable;
	}
	
	@Nullable
	public T getValue() {
		return value;
	}
	
	public void returnEvent(T value) {
		if (!canChangeEvent()) return;
		this.cancel();
		if (!isVoid) this.value = value;
	}
}
