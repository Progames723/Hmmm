package dev.progames723.hmmm.event.api;

public interface AsyncEvent {
	/**
	 * a double check for someone
	 * @return if the event can be executed asynchronously
	 */
	default boolean isAsync() {
		return false;
	}
}
