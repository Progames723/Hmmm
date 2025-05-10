package dev.progames723.hmmm.event.api;

import dev.progames723.hmmm.HmmmException;
import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.InternalUtils;
import dev.progames723.hmmm.utils.MiscUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"unchecked"})
public final class Events {
	private static boolean finalized = false;
	private static final Map<Class<? extends Event>, TreeSet<Listener<Event>>> eventListeners = new HashMap<>();
	
	private Events() {MiscUtil.instantiationOfUtilClass();}
	
	public static final class Listener<E extends Event> implements Comparable<Listener<E>> {
		private final Method method;
		private final EventListener listener;
		
		public Listener(Method method) {
			this(method, method.getAnnotation(EventListener.class));
		}
		
		public Listener(Method method, EventListener listener) {
			if (method == null || listener == null) throw new HmmmException(new NullPointerException("Arguments must not be null!"));
			checkIfCorrectMethod(method);//just for the exception
			this.method = method;
			this.listener = listener;
		}
		
		@NotNull
		Method getMethod() {
			return method;
		}
		
		@NotNull
		EventListener getListener() {
			return listener;
		}
		
		public void invoke(E event) {
			if (event.isCancelled() && !this.listener.receiveEventWhenCancelled()) return;
			try {
				event.setCurrentListener0(this);
				method.setAccessible(true);
				method.invoke(null, event);
			} catch (IllegalArgumentException e) {
				HmmmLibrary.LOGGER.debug(HmmmLibrary.EVENT, "Method(%s, from %s) has invalid generics for the event!".formatted(method, method.getDeclaringClass()), e);
			} catch (Exception e) {
				HmmmLibrary.LOGGER.warn(HmmmLibrary.EVENT, "Method(%s, from %s) threw an exception when handling event!".formatted(method, method.getDeclaringClass()), e);
			} finally {
				event.setCurrentListener0(null);
				method.setAccessible(false);
			}
		}
		
		@Override
		public int compareTo(@NotNull Events.Listener<E> o) {
			return listener.priority().compareTo(o.listener.priority());
		}
	}
	
	private static Set<Listener<Event>> getEveryListener(Class<? extends Event> eventClass) {
		Set<Listener<Event>> set = eventListeners.get(eventClass);
		if (set != null) {
			Class<?> superClass = eventClass.getSuperclass();
			while (superClass != Event.class) {
				Set<Listener<Event>> tempSet = eventListeners.get(superClass);
				if (!tempSet.isEmpty()) set.addAll(tempSet);
				superClass = superClass.getSuperclass();
			}
			set.addAll(eventListeners.get(Event.class));
		}
		return set;
	}
	
	public static <E extends Event> E invokeEvent(E event) {
		Set<Listener<Event>> listeners = getEveryListener(event.getClass());
		if (listeners == null) return event;
		if (AsyncEvent.class.isAssignableFrom(event.getClass()) && ((AsyncEvent) event).isAsync()) {
			//run asynchronously
			CompletableFuture.supplyAsync(() -> {
				listeners.forEach(eventListener -> eventListener.invoke(event));
				return event;
			});
		} else {
			for (Listener<? extends Event> listener : listeners) {
				((Listener<E>) listener).invoke(event);
			}
		}
		return event;
	}
	
	public static synchronized void registerEventListenerClass(Class<?> eventHandlerClass) {
		List<Method> methods = List.of(eventHandlerClass.getDeclaredMethods());
		for (Method method : methods) {
			try {
				registerEventListener(new Listener<>(method));
			} catch (Exception e) {
				HmmmLibrary.LOGGER.debug(HmmmLibrary.EVENT, "Listener {} cannot be registered\n{}", method, e.toString());
			}
		}
	}
	
	public static synchronized void unregisterEventListenerClass(Class<?> eventHandlerClass) {
		List<Method> methods = List.of(eventHandlerClass.getDeclaredMethods());
		for (Method method : methods) {
			try {
				unregisterEventListener(new Listener<>(method));
			} catch (Exception e) {
				HmmmLibrary.LOGGER.debug(HmmmLibrary.EVENT, "Listener {} cannot be unregistered\n{}", method, e.toString());
			}
		}
	}
	
	public static synchronized void registerEventListener(Listener<Event> listener) {
		Class<? extends Event> event = checkIfCorrectMethod(listener.method);
		TreeSet<Listener<Event>> listeners = eventListeners.get(event);
		try {
			assert listeners != null : "unregistered event!";
		} catch (AssertionError e) {
			throw new HmmmException((Class<?>) null, e);
		}
		if (!listeners.add(listener)) throw new HmmmException(null, "Listener already registered!");
		eventListeners.put(event, listeners);
	}
	
	public static synchronized void unregisterEventListener(Listener<? extends Event> listener) {
		Class<? extends Event> event = checkIfCorrectMethod(listener.method);
		TreeSet<Listener<Event>> listeners = eventListeners.get(event);
		try {
			assert listeners != null : "unregistered event!";
		} catch (AssertionError e) {
			throw new HmmmException((Class<?>) null, e);
		}
		listeners.remove(listener);
		eventListeners.put(event, listeners);
	}
	
	private static synchronized Class<? extends Event> checkIfCorrectMethod(Method eventHandler) {
		List<HmmmException> list = new LinkedList<>();
		if (!Modifier.isStatic(eventHandler.getModifiers())) list.add(new HmmmException(null, "method must be static!"));
		if (eventHandler.getAnnotation(EventListener.class) == null) list.add(new HmmmException(null, "method must have an EventListener annotation!"));
		if (eventHandler.getParameterTypes().length != 1) list.add(new HmmmException(null, "method has malformed(or incorrect) arguments"));
		try {
			if (!Event.class.isAssignableFrom(eventHandler.getParameterTypes()[0])) list.add(new HmmmException(null, "expected %s(or subclass) in argument, got %s".formatted(Event.class, eventHandler.getParameterTypes()[0])));
		} catch (Exception e) {
			list.add(new HmmmException((Class<?>) null, e));
		}
		if (!list.isEmpty()) {
			list.add(0, new HmmmException(null, "Cannot use incorrect methods!"));
			HmmmException exception = null;
			for (HmmmException e : list) {
				if (exception == null) exception = e;
				else exception.addSuppressed(e);
			}
			throw exception;
		}
		return (Class<? extends Event>) eventHandler.getParameterTypes()[0];
	}
	
	public static synchronized void registerEvent(Class<? extends Event> event) {
		if (eventListeners.put(event, new TreeSet<>(Comparator.comparingInt(value -> -value.listener.priority().ordinal()))) != null)
			throw new HmmmException("Event already exists!");
	}
	
	public static synchronized void unregisterEvent(Class<? extends Event> event) {
		eventListeners.remove(event);
	}
	
	@ApiStatus.Internal
	@CallerSensitive
	public static synchronized void startEventRegistration() {
		if (finalized) return;
		finalized = true;
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		HmmmLibrary.LOGGER.debug("Registering events...");
		List<Class<?>> events = InternalUtils.scanClassesFor(Event.class, InternalUtils.ScanType.SUB_CLASSES, true);
		if (!events.isEmpty()) events.add(Event.class);
		for (Class<?> event : events) {
			try {
				registerEvent((Class<? extends Event>) event);
			} catch (Exception e) {
				HmmmLibrary.LOGGER.error(HmmmLibrary.EVENT, "Failed to register %s event!".formatted(event), e);
			}
		}
		HmmmLibrary.LOGGER.debug("Scanning for registering callbacks... ");
		List<Class<RegisterEventCallback>> list = InternalUtils.scanClassesForGenerics(RegisterEventCallback.class, InternalUtils.ScanType.INTERFACE_IMPL, true);
		if (!list.isEmpty()) HmmmLibrary.LOGGER.debug("Scan successful, entries: {}.", list); else HmmmLibrary.LOGGER.debug("No callback entries have been found.");
		list.forEach(cls -> {
			try {
				Constructor<? extends RegisterEventCallback> constructor = cls.getDeclaredConstructor();
				constructor.setAccessible(true);
				constructor.newInstance().registrationStart();
				constructor.setAccessible(false);
			} catch (Exception e) {
				HmmmLibrary.LOGGER.error(HmmmLibrary.EVENT, "failed to instantiate %s! Make sure to have a public no args constructor!".formatted(cls), e);
			}
		});
		List<Class<?>> list1 = InternalUtils.scanForAnnotatedClassesWith(AutoRegisterEvents.class);
		list1.forEach(Events::registerEventListenerClass);
	}
}
