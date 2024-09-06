package dev.progames723.hmmm.events.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
	EventPriority priority() default EventPriority.NORMAL;
}
