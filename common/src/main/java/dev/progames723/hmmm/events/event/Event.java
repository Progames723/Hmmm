package dev.progames723.hmmm.events.event;

public abstract class Event {
	protected boolean cancelled = false;
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	void unCancel() {
		cancelled = false;//no unless you really want to
	}
	
	public boolean isCancellable() {
		return false;
	}
}
