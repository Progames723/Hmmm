package dev.progames723.hmmm.event.api;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.utils.ReflectUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@ApiStatus.Experimental
public abstract class Event {
	private final AtomicLong eventCancelViolations = new AtomicLong(0);
	private final boolean isCancellable;
	private EventResult eventResult = EventResult.PASS;
	
	protected Event(boolean isCancellable) {
		this.isCancellable = isCancellable;
	}
	
	public boolean isCancellable() {
		return isCancellable;
	}
	
	public final void setEventResult(EventResult value) {
		if (value == null) throw new NullPointerException("Cannot use null in Event#isCanceled()!");
		if (!isCancellable && value.cancelsEvents()) {
			if (eventCancelViolations.get() < 12) {
				HmmmLibrary.LOGGER.warn("Cancellable event violation! {} violations.", eventCancelViolations.incrementAndGet(), new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), "Some event is faulty! %s cancellation violations!".formatted(eventCancelViolations.get())));
			} else {
				throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), "Some event is faulty! %s cancellation violations!".formatted(eventCancelViolations.incrementAndGet()));
			}
		}
		eventResult = value;
	}
	
	@NotNull
	public EventResult getEventResult() {
		return eventResult;
	}
	
	public boolean isCancelled() {
		//sanity checks because that shit is very possible
		return Objects.requireNonNullElse(eventResult, EventResult.PASS).cancelsEvents();
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
		HIGHEST(4),
		HIGH(3),
		DEFAULT(2),
		LOW(1),
		LOWEST(0),
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
