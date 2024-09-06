package dev.progames723.hmmm.events.event.runner;

import java.lang.reflect.Method;
import java.util.Collection;

class Events {
	private final Collection<Class<?>> listeners;
	
	private final Collection<Method> listenersMethods;
	
	public Events(Collection<Class<?>> listeners, Collection<Method> listenersMethods) {
		this.listeners = listeners;
		this.listenersMethods = listenersMethods;
	}
}
