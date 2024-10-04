package dev.progames723.hmmm.event.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * Do not use as forge like events!
 */
@ApiStatus.Experimental
public abstract class ReturnableEvent extends Event {
	private final boolean nullable;
	protected final boolean isVoid;
	
	protected ReturnableEvent(boolean isVoid, boolean isCancellable, boolean nullable) {
		super(isCancellable);
		this.isVoid = isVoid;
		this.nullable = nullable;
	}
	
	public abstract boolean returnsNull();
	
	public boolean isNullable() {
		return nullable;
	}
}
