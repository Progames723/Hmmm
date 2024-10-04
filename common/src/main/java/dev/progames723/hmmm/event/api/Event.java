package dev.progames723.hmmm.event.api;

import dev.progames723.hmmm.HmmmException;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

/**
 * Do not use as forge like events!
 */
@ApiStatus.Experimental
public abstract class Event {
	private final boolean isCancellable;
	private TriBoolean isCancelled;
	
	protected Event(boolean isCancellable) {
		this.isCancellable = isCancellable;
		isCancelled = TriBoolean.DEFAULT;
	}
	
	public boolean isCancellable() {
		return isCancellable;
	}
	
	/**
	 * please override this for other things
	 */
	public void setCancelled(TriBoolean value) {
		if (!isCancellable && value.equals(TriBoolean.FALSE)) throw new HmmmException("Cannot cancel a non-cancellable event!");
		if (value == null) throw new HmmmException("Cannot use null to set Event#isCanceled");
		isCancelled = value;
	}
	
	public TriBoolean isCancelled() {
		//sanity checks because that shit is very possible
		return Objects.requireNonNullElse(isCancelled, TriBoolean.DEFAULT);
	}
}
