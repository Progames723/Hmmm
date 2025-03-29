package dev.progames723.hmmm.event.api;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.utils.ReflectUtil;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.concurrent.atomic.AtomicLong;

public abstract class Event {
	private final AtomicLong eventCancelViolations = new AtomicLong(0);
	private final boolean isCancellable;
	private boolean cancelled;
	
	protected Event(boolean isCancellable) {
		this.isCancellable = isCancellable;
	}
	
	public final boolean isCancellable() {
		return isCancellable;
	}
	
	@MustBeInvokedByOverriders
	public final void cancel() {
		if (cancelled || !canChangeEvent(ReflectUtil.CALLER_CLASS.getCallerClass())) return;
		if (!isCancellable) {
			if (eventCancelViolations.get() < 12) {
				HmmmLibrary.LOGGER.warn("Event cancel violation! {}/13 violations.", eventCancelViolations.incrementAndGet(), new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), "Some event is faulty! %s/13 cancellation violations!".formatted(eventCancelViolations.get())));
			} else {
				throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), "Some event is faulty! %s cancellation violations!".formatted(eventCancelViolations.incrementAndGet()));
			}
		} else cancelled = true;
	}
	
	protected final boolean canChangeEvent(Class<?> caller) {
		EventListener instance = caller.getDeclaredAnnotation(EventListener.class);
		if (instance == null) return false;
		return instance.priority().canModifyEvent();
	}
	
	public boolean isCancelled() {
		//sanity checks because that shit is very possible
		return cancelled;
	}
	
	@Override
	public final String toString() {
		return this.getClass().getName();
	}
	
	public interface HasEventResult {
		EventResult getEventResult();
		
		void setEventResult(EventResult eventResult);
	}
	
	public enum EventResult {
		SUCCESS(true, true),
		FAILURE(false, true),
		PASS(null, false);//continues execution
		
		private final Boolean representation;
		private final boolean cancelsEvents;
		
		EventResult(Boolean representation, boolean cancelsEvents) {
			this.representation = representation;
			this.cancelsEvents = cancelsEvents;
		}
		
		public Boolean representation() {
			return representation;
		}
		
		public boolean cancelsEvents() {
			return cancelsEvents;
		}
	}
	
	public enum EventPriority {
		LOWEST(0),
		LOW(1),
		DEFAULT(2),
		HIGH(3),
		HIGHEST(4),
		MONITOR(999, false);
		
		private final int priority;
		private final boolean canModifyEvent;
		
		EventPriority(int priority, boolean canModifyEvent) {
			this.canModifyEvent = canModifyEvent;
			this.priority = priority;
		}
		
		EventPriority(int priority) {
			this(priority, true);
		}
		
		public int priority() {
			return priority;
		}
		
		public boolean canModifyEvent() {
			return canModifyEvent;
		}
	}
}
