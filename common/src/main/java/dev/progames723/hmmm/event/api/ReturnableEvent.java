package dev.progames723.hmmm.event.api;

import dev.progames723.hmmm.utils.ReflectUtil;
import org.jetbrains.annotations.Nullable;

@Deprecated(forRemoval = true)
public abstract class ReturnableEvent<T> extends Event {
	private final boolean nullable;
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
		if (!canChangeEvent(ReflectUtil.CALLER_CLASS.getCallerClass())) return;
		this.cancel();
		if (!isVoid) this.value = value;
	}
}
