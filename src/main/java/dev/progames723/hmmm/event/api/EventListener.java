package dev.progames723.hmmm.event.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
	/**
	 * @return an {@link dev.progames723.hmmm.event.api.Event.EventPriority} instance
	 */
	Event.EventPriority priority() default Event.EventPriority.DEFAULT;
	
	/**
	 * @return {@code true} if the event should be invoked if cancelled
	 */
	boolean receiveEventWhenCancelled() default false;
}
