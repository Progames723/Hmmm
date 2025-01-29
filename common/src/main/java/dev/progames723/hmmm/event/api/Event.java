package dev.progames723.hmmm.event.api;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.utils.ReflectUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Do not use as forge like events!
 */
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
	
	/**
	 * please override this for other things
	 */
	public void setEventResult(EventResult value) {
		if (value == null) throw new NullPointerException("Cannot use null in Event#isCanceled()!");
		if (!isCancellable && value.cancelsEvents()) {
			if (eventCancelViolations.get() < 15) {
				HmmmLibrary.LOGGER.warn("Cancellable event violation! {} violations.", eventCancelViolations.incrementAndGet() , new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), "Some event is faulty! %s cancellation violations!".formatted(eventCancelViolations.get())));
			} else {
				throw new HmmmException(ReflectUtil.CALLER_CLASS.getCallerClass(), "Some event is faulty! %s cancellation violations!".formatted(eventCancelViolations.get()));
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
		PASS(null, false);
		
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
}
